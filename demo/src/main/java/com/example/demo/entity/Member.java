package com.example.demo.entity;

import com.example.demo.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "members")
public class Member {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;

    private String name;
    private String email;

    public Member(String name, String email) {
        this.id = new ObjectId();
        this.name = name;
        this.email = email;
    }
}