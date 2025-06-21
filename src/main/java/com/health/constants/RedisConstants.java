package com.health.constants;

public class RedisConstants {

    public static final String USER_TOKEN_PREFIX = "user:token:";

    public static final long TOKEN_EXPIRE_HOURS = 2L;

    private RedisConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }
}