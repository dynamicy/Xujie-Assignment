package com.example.demo.service;

import com.example.demo.bo.MemberRequest;
import com.example.demo.entity.Member;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.repository.MemberRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member save(MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getName(), memberRequest.getEmail());
        return memberRepository.save(member);
    }

    public Member update(String id, MemberRequest memberRequest) {
        Optional<Member> memberOptional = memberRepository.findById(new ObjectId(id));
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setName(memberRequest.getName());
            member.setEmail(memberRequest.getEmail());
            return memberRepository.save(member);
        } else {
            throw new MemberNotFoundException("Member not found with id " + id);
        }
    }

    public void delete(String id) {
        Optional<Member> memberOptional = memberRepository.findById(new ObjectId(id));
        if (memberOptional.isPresent()) {
            memberRepository.deleteById(new ObjectId(id));
        } else {
            throw new MemberNotFoundException("Member not found with id " + id);
        }
    }

    public Optional<Member> findById(String id) {
        return memberRepository.findById(new ObjectId(id));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}