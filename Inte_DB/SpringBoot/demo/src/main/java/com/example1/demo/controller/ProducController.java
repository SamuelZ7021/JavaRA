package com.example1.demo.controller;

import com.example1.demo.dtos.ProductDTO;
import jakarta.validation.Valid;
import com.example1.demo.module.Product;
import com.example1.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class ProducController {
    @Autowired
    private ProductService productService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Product> getFindAll(){
        return productService.getFindAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Product> findById(@PathVariable Long id){
        Product product = productService.getFindById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@Valid @RequestBody ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());

        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO){
        Product productEx = productService.getFindById(id);
        productEx.setName(productDTO.getName());
        productEx.setPrice(productDTO.getPrice());
        return ResponseEntity.ok(productService.saveProduct(productEx));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.getFindById(id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();

    }
}
