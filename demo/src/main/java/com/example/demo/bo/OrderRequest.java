package com.example.demo.bo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String memberId;
    private String productId;
    private Integer quantity;
    private String currency;
}