package com.example1.demo.controller;

import com.example1.demo.module.Product;
import com.example1.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <-- Para pasar datos a la vista
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/view/products")
public class ProductViewController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String showListProducts(Model model){
        List<Product> lists = productService.getFindAll();

        model.addAttribute("products", lists);

        return "lists-products";
    }
}
