package com.example.demo.service;

import com.example.demo.bo.ProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        ObjectId userId = new ObjectId(id);
        Product product = productRepository.findById(userId).orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "TWD");
        return productRepository.save(product);
    }

    public void delete(String id) {
        ObjectId productId = new ObjectId(id);
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with id " + id);
        }
        productRepository.deleteById(productId);
    }

    public Product findById(String id) {
        ObjectId userId = new ObjectId(id);
        return productRepository.findById(userId).orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}