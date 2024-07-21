package com.example.demo.service;

import com.example.demo.bo.OrderRequest;
import com.example.demo.bo.OrderSearchCriteria;
import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Order save(OrderRequest orderRequest) {
        Member member = memberRepository.findById(new ObjectId(orderRequest.getMemberId()))
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id " + orderRequest.getMemberId()));
        Product product = productRepository.findById(new ObjectId(orderRequest.getProductId()))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + orderRequest.getProductId()));

        if (orderRequest.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        String currency = orderRequest.getCurrency() != null ? orderRequest.getCurrency() : "TWD";
        double totalAmount = product.getPrice() * orderRequest.getQuantity();
        Order order = new Order(member, product, orderRequest.getQuantity(), currency, totalAmount);
        return orderRepository.save(order);
    }

    public Order updateOrder(String id, OrderRequest orderRequest) {
        if (orderRequest.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Optional<Order> orderOptional = orderRepository.findById(new ObjectId(id));
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Member member = memberRepository.findById(new ObjectId(orderRequest.getMemberId()))
                    .orElseThrow(() -> new MemberNotFoundException("Member not found with id " + orderRequest.getMemberId()));
            Product product = productRepository.findById(new ObjectId(orderRequest.getProductId()))
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id " + orderRequest.getProductId()));

            double totalAmount = product.getPrice() * orderRequest.getQuantity();
            String currency = orderRequest.getCurrency() != null ? orderRequest.getCurrency() : "TWD";
            order.setMember(member);
            order.setProduct(product);
            order.setQuantity(orderRequest.getQuantity());
            order.setCurrency(currency);
            order.setTotalAmount(totalAmount);
            return orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Order not found with id " + id);
        }
    }

    public void delete(String id) {
        Optional<Order> orderOptional = orderRepository.findById(new ObjectId(id));
        if (orderOptional.isPresent()) {
            orderRepository.deleteById(new ObjectId(id));
        } else {
            throw new OrderNotFoundException("Order not found with id " + id);
        }
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(new ObjectId(id));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> findOrders(OrderSearchCriteria criteria, Pageable pageable) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.match(buildCriteria(criteria)));
        operations.add(Aggregation.lookup("products", "product.$id", "_id", "product"));
        operations.add(Aggregation.unwind("product", true));
        operations.add(Aggregation.skip((long) pageable.getOffset()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        Aggregation aggregation = Aggregation.newAggregation(operations);
        AggregationResults<Order> results = mongoTemplate.aggregate(aggregation, "orders", Order.class);
        List<Order> orders = results.getMappedResults();

        Query countQuery = new Query(buildCriteria(criteria));

        return PageableExecutionUtils.getPage(
                orders,
                pageable,
                () -> mongoTemplate.count(countQuery, Order.class)
        );
    }

    private Criteria buildCriteria(OrderSearchCriteria criteria) {
        Criteria criteriaObj = new Criteria();
        if (criteria.getProductName() != null) {
            List<Product> products = productRepository.findByNameRegex(criteria.getProductName(), "i");
            List<ObjectId> productIds = products.stream().map(Product::getId).toList();
            criteriaObj.and("product.$id").in(productIds);
        }
        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            criteriaObj.and("purchaseDate").gte(criteria.getStartDate()).lte(criteria.getEndDate());
        } else if (criteria.getStartDate() != null) {
            criteriaObj.and("purchaseDate").gte(criteria.getStartDate());
        } else if (criteria.getEndDate() != null) {
            criteriaObj.and("purchaseDate").lte(criteria.getEndDate());
        }

        return criteriaObj;
    }

    public List<Member> findMembersWithOrdersGreaterThan(int n) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getMember, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > n)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }
}