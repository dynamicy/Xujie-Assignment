package com.example.demo.entity;

import com.example.demo.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private double price;
    private String currency;

    public Product(String name, String description, double price, String currency) {
        this.id = new ObjectId();
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency != null ? currency : "TWD";
    }
}