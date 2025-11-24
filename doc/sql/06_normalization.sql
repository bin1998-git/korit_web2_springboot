select
	*
from
	raw_table;
/*
product_names, product_prices
-> 하나의 셀에 여러값이 기록되어 있음
왜 문제일까?
검색 불가능: 마우스만 주문한사람? 같은 쿼리 불가능
집계함수 불가능: sum, avg
-> 모든 칼럼에는 단일값이 들어가야한다: 제1정규화

2. 제2정규형: 부분종속 제거
pk가 두개이상의 칼럼으로 이루어져 있을떄, 일부칼럼에만 종속된 컬럼이 있으면 안된다.
(order-id + customer_name) -> 식별가능
customer_phone, customer_address -> order_id와 상관이 없음
-> 상품과는 아무런 관계 x, 주문과도 아무런 관계x
-> customer_name하고만 관계
해결:
고객 정보 테이블 분리
주문 정보 테이블 분리
주문별 상품 정보 테이블 분리

3. 제3정규형: 이행적 종속 제거
컬럼은 pk말고 다른것에 의존하면 안된다
order_id -> customer_id -> customer_phone, customer_address
customer_phone은 order_id에 이행적으로 종속되어있다.
A -> B -> C일때, A-> B/ B -> C 분리해야한다.
*/

/* customers
customers table
customer_id pk
customer_name
customer_phone
customer_address
*/

/* orders
order_id pk
customer_id fk(외래키 - 다른테이블의 pk)
order_date
*/

/* product
상품이름이 같을 수 도 있음 -> product_name을 pk로 사용x
product_id pk
product_name
product_price
*/

/* order_detail
order_detail_id pk
order_id fk
product_id fk
quantity
--- 그냥 orders에 quantity 넣으면 안 될까?
order_id가 1인 경우, 노트북 1개, 마우스 2개
order_id product_id product_id quantity
1          1            1        1         # 노트북 1개
1           1           2           2         # 마우스 2개 -> order_id가 pk인데, 중복되게 됨
*/

create table order_details(
	order_detail_id int auto_increment primary key,
    order_id int not null,
    product_id int not null,
    quantity int not null check(quantity > 0)
);
/*
raw_table -> 정규화(1,2,3 정규화)
orders -> 누가 언제 주문했는가?
order_detail -> 무슨 상품을 몇 개 주문했는가?
--- quantity가 order에 있으면 안되는 이유: 
1. 실제로 한 주문에 여러 상품이 들어가기 때문
2. order_id가 중복이 되어야한다 -> orders 테이블은 불가능하구나!
3. order_details 테이블을 만들어서 분리하자! 
customer -> 고객정보
product -> 상품정보
*/



# raw_table -> 정규화
# 정규화된 테이블을 다시 비정규화된 table 조회하는 방법?
# -> join(외래키를 기준으로 실행)


insert into
	customers (customer_name, customer_phone, customer_address)
select
	customer_name,
    customer_phone,
    customer_address
from
	raw_table; # select 결과를 그대로 insert 하는 방법

# product 데이터 있음
# orders 데이터 있음
# order_detail 더미데이터 insert
insert into
	order_details (order_id, product_id, quantity)
values
	(1, 1, 1),
	(1, 2, 2),
	(2, 3, 1),
	(3, 4, 1);
    
insert into
	product (product_id, product_name, product_price)
values
	(2, '청소기', 130000);
    
    