package com.example1.demo.service;

import com.example1.demo.exceptions.ResoucerNotFoundException;
import com.example1.demo.module.Product;
import com.example1.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFindAll(){
        return productRepository.findAll();
    }

    public Product getFindById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResoucerNotFoundException("Producto no encontrado con el ID " + id));
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
