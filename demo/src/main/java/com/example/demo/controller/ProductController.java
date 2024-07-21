package com.example.demo.controller;

import com.example.demo.bo.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<BaseResponse<Product>> getProductById(@PathVariable String id) {
        try {
            Product product = productService.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
            BaseResponse<Product> response = new BaseResponse<>(HttpStatus.OK.value(), "Product retrieved successfully", product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            BaseResponse<Product> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID")
    public ResponseEntity<BaseResponse<Void>> deleteProduct(@PathVariable String id) {
        try {
            productService.delete(id);
            BaseResponse<Void> response = new BaseResponse<>(HttpStatus.OK.value(), "Product deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            BaseResponse<Void> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product by ID")
    public ResponseEntity<BaseResponse<Product>> updateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.update(id, productRequest);
            BaseResponse<Product> response = new BaseResponse<>(HttpStatus.OK.value(), "Product updated successfully", updatedProduct);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            BaseResponse<Product> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<BaseResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.findAll();
        BaseResponse<List<Product>> response = new BaseResponse<>(HttpStatus.OK.value(), "Products retrieved successfully", products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}