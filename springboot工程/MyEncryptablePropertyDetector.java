package com.example.demo.util;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;

public class MyEncryptablePropertyDetector implements EncryptablePropertyDetector {

    public static final String ENCODED_PASSWORD_HINT = "{cipher}";

    // 如果属性的字符开头为"{cipher}"，返回true，表明该属性是加密过的
    @Override
    public boolean isEncrypted(String s) {
        if (null != s) {
            return s.startsWith(ENCODED_PASSWORD_HINT);
        }
        return false;
    }
    // 该方法告诉工具，如何将自定义前缀去除
    @Override
    public String unwrapEncryptedValue(String s) {
        return s.substring(ENCODED_PASSWORD_HINT.length());
    }
}