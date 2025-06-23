package com.health.service;

import com.health.domain.entity.Member;
import com.health.domain.vo.Result;

public interface MemberService {
    
    Result login(Member member);
    
    Member findByPhoneNumber(String phoneNumber);

    Result autoRegister(String phoneNumber);
}