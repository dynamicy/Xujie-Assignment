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

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member save(MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getName(), memberRequest.getEmail());
        return memberRepository.save(member);
    }

    public Member update(String id, MemberRequest memberRequest) {
        ObjectId userId = new ObjectId(id);
        Member member = memberRepository.findById(userId).orElseThrow(() -> new MemberNotFoundException("Member not found with id " + id));
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        return memberRepository.save(member);
    }

    public void delete(String id) {
        ObjectId userId = new ObjectId(id);
        if (!memberRepository.existsById(userId)) {
            throw new MemberNotFoundException("Member not found with id " + id);
        }
        memberRepository.deleteById(new ObjectId(id));
    }

    public Member findById(String id) {
        ObjectId userId = new ObjectId(id);
        return memberRepository.findById(userId).orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}