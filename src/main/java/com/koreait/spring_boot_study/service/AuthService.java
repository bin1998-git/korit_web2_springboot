package com.koreait.spring_boot_study.service;

import com.koreait.spring_boot_study.dto.req.SignInReqDto;
import com.koreait.spring_boot_study.dto.req.SignUpReqDto;
import com.koreait.spring_boot_study.dto.res.SignInResDto;
import com.koreait.spring_boot_study.entity.User;
import com.koreait.spring_boot_study.exception.UserException;
import com.koreait.spring_boot_study.jwt.JwtUtil;
import com.koreait.spring_boot_study.repository.mapper.RefreshTokenMapper;
import com.koreait.spring_boot_study.repository.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.jar.JarException;

@Service
@RequiredArgsConstructor
// final 필드만 초기화하는 생성자
// final 필드에 대해서 자동으로 autowired 된다.(다른 생성자가 없을 떄)
public class AuthService {
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-expire-millis}")
    private long refreshExpireMillis;

    // refresh 토큰 저장
    private void saveRefreshToken(int userId, String refreshToken) {
        LocalDateTime expireAt = LocalDateTime.now()
                        .plus(refreshExpireMillis, ChronoUnit.MILLIS);

        int successCount = refreshTokenMapper
                .insertRefreshToken(userId, refreshToken, expireAt);

        if (successCount <= 0) {
            // todo: 예외 던져야함
        }
    }


    // refresh 토큰 업데이트
    private void rotateRefreshToken(String oldToken, String newToken) {
        int successCount = refreshTokenMapper
                .updateRefreshToken(oldToken, newToken);
        if (successCount <= 0) {
            // todo: 예외
        }
    }


    // User 객체만 가져오면 토큰 쌍으로 바꿔서 리턴
    private SignInResDto generateTokenPair(User user) {
        // 토큰에 담을 sub, extraClaims를 User로부터 추출
        String sub = String.valueOf(user.getUserId());

        Map<String, Object> extraClaims = Map.of(
                "role", user.getRole().getRoleName()
        );

        // TokenPair 생성
        String accessToken = jwtUtil.generateAccessToken(sub, extraClaims);
        String refreshToken = jwtUtil.generateRefreshToken(sub);

        return new SignInResDto(accessToken, refreshToken);


    }


    public void signUp(SignUpReqDto dto) {
        // 1. 아이디, 이메일 중복검사
       boolean isDuplicatedUsername = userMapper
                .getUserByUserName(dto.getUsername())
                .isPresent(); // Optional 안에 값이 있으면 true
        if (isDuplicatedUsername) {
            throw new UserException("이미 존재하는 아이디 입니다.", HttpStatus.CONFLICT);
        }
        System.out.println("서비스 옴");
        boolean isDuplicatedEmail = userMapper.getUserByEmail(dto.getEmail())
                .isPresent();
        if (isDuplicatedEmail) {
            throw new UserException("이미 존재하는 이메일 입니다", HttpStatus.CONFLICT);
        }
        // 2. dto -> entity
        User user = dto.toEntity();
        // password를 암호화해서 set해줘야한다
        user.setPassword(encoder.encode(dto.getPassword()));

        // 3. DB에 저장
        int successCount = userMapper.addUser(user);
        if (successCount <= 0) {
            throw new UserException("회원가입 중 에러가 발생하였습니다.",
                    HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    // 로그인
    @Transactional(rollbackFor = Exception.class)
    public SignInResDto signIn(SignInReqDto dto) {
        // 실제 아이디가 있는지 검사
        User user = userMapper.getUserByUserName(dto.getUsername())
                .orElseThrow(() -> new UserException("사용자 정보를 잘못 입력하셨습니다.",
                        HttpStatus.BAD_REQUEST));

        // 비밀번호 확인
        //  encoder.matches(평문암호, 암호화된암호) -> 맞으면 true
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            // 비밀번호 틀렸을때
            throw new UserException("사용자 정보를 잘못 입력하셨습니다.",
                    HttpStatus.BAD_REQUEST);
        }

        // id pw 모두 통과! -> 로그인시켜줘야한다(토큰발급)
        SignInResDto tokenPair = generateTokenPair(user);


        // refresh토큰을 db에 저장 (나중에)
        saveRefreshToken(user.getUserId(),tokenPair.getRefreshToken());


        return tokenPair;
    }

    public SignInResDto refreshToken(String refresh) {
        // 1. 타입검증
        // Refresh 토큰이 아니라면
        if (!jwtUtil.isRefreshToken(refresh)) {
            // todo: 예외를 던져야함
        }
        // 2. db에 실제 있는 토큰인가 검사
        refreshTokenMapper.findByToken(refresh)
                .orElseThrow(); // todo: 예외 던져야함

        // 3. claim 추출 - 쿠키에서 가져온 토큰으로부터
        Claims claims;
        try {
            claims = jwtUtil.getClaims(refresh);
        } catch (ExpiredJwtException e) {
            // 리프레쉬 토큰마저 만료되었을 경우
            // db에서 토큰을 제거해줘야한다. (나중에)
            // 응답으로 에러메세지를 내려준다 -> 프론트에서 로그인창으로 리디렉션
        } catch (JwtException e) {
            // 위조된 경우 - 보험(있으면 안됨)
            // db에서 삭제 (나중에)
        }

        // 4. claim에서 sub 추출
        // 5. 새토큰 발급
        // 6. db 업데이트

        return new SignInResDto("", "");
    }


}
