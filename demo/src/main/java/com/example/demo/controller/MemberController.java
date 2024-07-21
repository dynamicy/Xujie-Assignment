package com.example.demo.controller;

import com.example.demo.bo.MemberRequest;
import com.example.demo.entity.Member;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.response.BaseResponse;
import com.example.demo.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@Tag(name = "Member Management", description = "API for managing members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping
    @Operation(summary = "Create a new member")
    public ResponseEntity<BaseResponse<Member>> createMember(@RequestBody MemberRequest memberRequest) {
        Member savedMember = memberService.save(memberRequest);
        BaseResponse<Member> response = new BaseResponse<>(HttpStatus.CREATED.value(), "Member created successfully", savedMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a member by ID")
    public ResponseEntity<BaseResponse<Member>> getMemberById(@PathVariable String id) {
        try {
            Member member = memberService.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("Member not found with id " + id));
            BaseResponse<Member> response = new BaseResponse<>(HttpStatus.OK.value(), "Member retrieved successfully", member);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            BaseResponse<Member> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a member by ID")
    public ResponseEntity<BaseResponse<Void>> deleteMember(@PathVariable String id) {
        try {
            memberService.delete(id);
            BaseResponse<Void> response = new BaseResponse<>(HttpStatus.OK.value(), "Member deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            BaseResponse<Void> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a member by ID")
    public ResponseEntity<BaseResponse<Member>> updateMember(@PathVariable String id, @RequestBody MemberRequest memberRequest) {
        try {
            Member updatedMember = memberService.update(id, memberRequest);
            BaseResponse<Member> response = new BaseResponse<>(HttpStatus.OK.value(), "Member updated successfully", updatedMember);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            BaseResponse<Member> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping
    @Operation(summary = "Get members with pagination")
    public ResponseEntity<BaseResponse<Page<Member>>> getAllMembers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> members = memberService.findAll(pageable);
        BaseResponse<Page<Member>> response = new BaseResponse<>(HttpStatus.OK.value(), "Members retrieved successfully", members);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}