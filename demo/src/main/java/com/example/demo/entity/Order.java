package com.example.demo.entity;

import com.example.demo.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    @DBRef
    private Member member;

    @DBRef
    private Product product;

    private int quantity;
    private Date purchaseDate;
    private String currency;
    private double totalAmount;

    public Order(Member member, Product product, int quantity, String currency, double totalAmount) {
        this.id = new ObjectId();
        this.member = member;
        this.product = product;
        this.quantity = quantity;
        this.purchaseDate = new Date();
        this.currency = currency != null ? currency : "TWD";
        this.totalAmount = totalAmount;
    }
}