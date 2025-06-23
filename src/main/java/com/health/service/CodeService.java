package com.health.service;

public interface CodeService {
    
    String generateAndSendCode(String telephone);

    boolean validateCode(String telephone, String code);
}
