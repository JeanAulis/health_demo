package com.health.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.health.domain.entity.Member;
import com.health.domain.vo.Result;
import com.health.mapper.MemberMapper;
import com.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Override
    public Result login(Member member) {
        log.info("会员登录，手机号：{}", member.getPhoneNumber());
        
        // 参数校验
        if (StrUtil.isBlank(member.getPhoneNumber())) {
            return Result.error("手机号不能为空");
        }
        
        // 根据手机号查找会员
        Member existMember = findByPhoneNumber(member.getPhoneNumber());
        
        if (existMember == null) {
            // 会员不存在，自动注册
            log.info("会员不存在，开始自动注册，手机号：{}", member.getPhoneNumber());
            Result registerResult = autoRegister(member.getPhoneNumber());
            if (!registerResult.isFlag()) {
                return registerResult;
            }
            existMember = (Member) registerResult.getData();
        }
        
        log.info("会员登录成功，会员ID：{}", existMember.getId());
        return Result.success(existMember);
    }
    
    @Override
    public Member findByPhoneNumber(String phoneNumber) {
        if (StrUtil.isBlank(phoneNumber)) {
            return null;
        }
        
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getPhoneNumber, phoneNumber);
        
        return memberMapper.selectOne(wrapper);
    }
    
    @Override
    public Result autoRegister(String phoneNumber) {
        log.info("开始自动注册会员，手机号：{}", phoneNumber);
        
        // 参数校验
        if (StrUtil.isBlank(phoneNumber)) {
            return Result.error("手机号不能为空");
        }
        
        // 检查手机号是否已存在
        Member existMember = findByPhoneNumber(phoneNumber);
        if (existMember != null) {
            log.info("手机号已存在，返回现有会员信息，手机号：{}", phoneNumber);
            return Result.success(existMember);
        }
        
        try {
            // 创建新会员
            Member newMember = Member.builder()
                    .phoneNumber(phoneNumber)
                    .regTime(LocalDate.now())
                    .build();
            
            // 保存到数据库
            int result = memberMapper.insert(newMember);
            
            if (result > 0) {
                log.info("会员自动注册成功，会员ID：{}", newMember.getId());
                return Result.success(newMember);
            } else {
                log.error("会员自动注册失败，手机号：{}", phoneNumber);
                return Result.error("注册失败");
            }
            
        } catch (Exception e) {
            log.error("会员自动注册异常，手机号：{}，异常信息：{}", phoneNumber, e.getMessage(), e);
            return Result.error("注册失败：" + e.getMessage());
        }
    }
}