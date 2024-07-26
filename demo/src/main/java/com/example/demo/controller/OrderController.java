package com.example.demo.controller;

import com.example.demo.bo.OrderRequest;
import com.example.demo.bo.OrderSearchCriteria;
import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.response.BaseResponse;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "API for managing orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<BaseResponse<Order>> createOrder(@RequestBody OrderRequest orderRequest) {
        Order savedOrder = orderService.save(orderRequest);
        BaseResponse<Order> response = new BaseResponse<>(HttpStatus.CREATED.value(), "Order created successfully", savedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order by ID")
    public ResponseEntity<BaseResponse<Order>> getOrderById(@PathVariable String id) {
        Order order = orderService.findById(id);
        BaseResponse<Order> response = new BaseResponse<>(HttpStatus.OK.value(), "Order retrieved successfully", order);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order by ID")
    public ResponseEntity<BaseResponse<Void>> deleteOrder(@PathVariable String id) {
        orderService.delete(id);
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.OK.value(), "Order deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order by ID")
    public ResponseEntity<BaseResponse<Order>> updateOrder(@PathVariable String id, @RequestBody OrderRequest orderRequest) {
        Order updatedOrder = orderService.updateOrder(id, orderRequest);
        BaseResponse<Order> response = new BaseResponse<>(HttpStatus.OK.value(), "Order updated successfully", updatedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get orders with pagination and optional filters")
    public ResponseEntity<BaseResponse<Page<Order>>> getOrders(OrderSearchCriteria criteria, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.findOrders(criteria, pageable);
        BaseResponse<Page<Order>> response = new BaseResponse<>(HttpStatus.OK.value(), "Orders retrieved successfully", orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get members with more than n orders")
    public ResponseEntity<BaseResponse<List<Member>>> getOrderStats(@RequestParam int n) {
        List<Member> members = orderService.findMembersWithOrdersGreaterThan(n);
        BaseResponse<List<Member>> response = new BaseResponse<>(HttpStatus.OK.value(), "Get members with more than n orders successfully", members);
        return ResponseEntity.ok(response);
    }
}