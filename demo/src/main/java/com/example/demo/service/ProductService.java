package com.example.demo.service;

import com.example.demo.bo.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product save(ProductRequest productRequest) {
        String currency = productRequest.getCurrency() != null ? productRequest.getCurrency() : "TWD";
        Product product = new Product(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), currency);
        return productRepository.save(product);
    }

    public Product update(String id, ProductRequest productRequest) {
        Optional<Product> productOptional = productRepository.findById(new ObjectId(id));
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "TWD");
            return productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Product not found with id " + id);
        }
    }

    public void delete(String id) {
        Optional<Product> productOptional = productRepository.findById(new ObjectId(id));
        if (productOptional.isPresent()) {
            productRepository.deleteById(new ObjectId(id));
        } else {
            throw new ProductNotFoundException("Product not found with id " + id);
        }
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(new ObjectId(id));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}