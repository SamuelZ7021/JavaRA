package com.example1.demo.repository;

import com.example1.demo.module.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.price > :minPrice")
    List<Product> findProductsAbovePrice(@Param("minPrice") double priceLimit);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.name = :categoryName")
    List<Product> findByCategoryName (@Param("categoryName") String categoryName);
}
