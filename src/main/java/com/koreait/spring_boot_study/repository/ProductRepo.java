package com.koreait.spring_boot_study.repository;

import com.koreait.spring_boot_study.entity.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ProductRepo {


    // 1. 다건조회(전체조회)
     List<Product> findAllProducts();


    // 2. 단건조회(상품 하나만 조회)
     String findProductNameById(int id);

    // 상품 추가
     int insertProduct(String name, int price);

    // 단건 삭제
    int deleteProductById(int id);

    // 단건 업데이트
    int updateProduct(int id, String name, int price);



    }

