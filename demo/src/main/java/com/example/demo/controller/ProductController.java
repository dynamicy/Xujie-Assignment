package com.example.demo.controller;

import com.example.demo.bo.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.response.BaseResponse;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Management", description = "API for managing products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<BaseResponse<Product>> createProduct(@RequestBody ProductRequest productRequest) {
        Product savedProduct = productService.save(productRequest);
        BaseResponse<Product> response = new BaseResponse<>(HttpStatus.CREATED.value(), "Product created successfully", savedProduct);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<BaseResponse<Product>> getProductById(@PathVariable String id) {
        Product product = productService.findById(id);
        BaseResponse<Product> response = new BaseResponse<>(HttpStatus.OK.value(), "Product retrieved successfully", product);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID")
    public ResponseEntity<BaseResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.delete(id);
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.OK.value(), "Product deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product by ID")
    public ResponseEntity<BaseResponse<Product>> updateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        Product updatedProduct = productService.update(id, productRequest);
        BaseResponse<Product> response = new BaseResponse<>(HttpStatus.OK.value(), "Product updated successfully", updatedProduct);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<BaseResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.findAll();
        BaseResponse<List<Product>> response = new BaseResponse<>(HttpStatus.OK.value(), "Products retrieved successfully", products);
        return ResponseEntity.ok(response);
    }
}