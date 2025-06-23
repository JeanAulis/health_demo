package com.health.web;

import cn.hutool.core.util.StrUtil;
import com.health.domain.entity.Member;
import com.health.domain.vo.Result;
import com.health.service.CodeService;
import com.health.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class CodeControll {
    
    @Autowired
    private CodeService codeService;
    
    @Autowired
    private MemberService memberService;
    
    @PostMapping("/validateCode/send4Login")
    public Result sendValidateCode(@RequestParam String telephone) {
        log.info("发送验证码请求，手机号：{}", telephone);
        
        try {
            // 参数校验
            if (StrUtil.isBlank(telephone)) {
                return Result.error("手机号不能为空");
            }
            
            // 简单的手机号格式校验
            if (!telephone.matches("^1[3-9]\\d{9}$")) {
                return Result.error("手机号格式不正确");
            }
            
            // 生成并发送验证码
            String code = codeService.generateAndSendCode(telephone);
            
            if (StrUtil.isNotBlank(code)) {
                log.info("验证码发送成功，手机号：{}", telephone);
                return Result.success("验证码发送成功");
            } else {
                log.error("验证码发送失败，手机号：{}", telephone);
                return Result.error("验证码发送失败");
            }
            
        } catch (Exception e) {
            log.error("发送验证码异常，手机号：{}，异常信息：{}", telephone, e.getMessage(), e);
            return Result.error("发送验证码失败：" + e.getMessage());
        }
    }

    @PostMapping("/member/login")
    public Result memberLogin(@RequestBody Map<String, String> loginData) {
        String telephone = loginData.get("telephone");
        String validateCode = loginData.get("validateCode");
        
        log.info("会员登录请求，手机号：{}，验证码：{}", telephone, validateCode);
        
        try {
            // 参数校验
            if (StrUtil.isBlank(telephone)) {
                return Result.error("手机号不能为空");
            }
            
            if (StrUtil.isBlank(validateCode)) {
                return Result.error("验证码不能为空");
            }
            
            // 校验验证码
            boolean isValidCode = codeService.validateCode(telephone, validateCode);
            if (!isValidCode) {
                return Result.error("验证码错误或已过期");
            }
            
            // 创建会员对象
            Member member = Member.builder()
                    .phoneNumber(telephone)
                    .build();
            
            // 会员登录（包含自动注册逻辑）
            Result loginResult = memberService.login(member);
            
            if (loginResult.isFlag()) {
                Member loginMember = (Member) loginResult.getData();
                log.info("会员登录成功，会员ID：{}，手机号：{}", loginMember.getId(), loginMember.getPhoneNumber());
                return Result.success(loginMember);
            } else {
                log.error("会员登录失败，手机号：{}，失败原因：{}", telephone, loginResult.getMessage());
                return loginResult;
            }
            
        } catch (Exception e) {
            log.error("会员登录异常，手机号：{}，异常信息：{}", telephone, e.getMessage(), e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }
}
