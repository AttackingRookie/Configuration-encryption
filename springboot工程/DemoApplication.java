package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.example.demo.util.MyEncryptablePropertyDetector;
import com.example.demo.util.MyEncryptablePropertyResolver;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class DemoApplication {
	
	public static void main(String[] args) {
		ApplicationContext ctx=SpringApplication.run(DemoApplication.class, args);
	}
	
    @Bean(name = "encryptablePropertyDetector")
    public EncryptablePropertyDetector encryptablePropertyDetector() {
    	System.out.println("encryptablePropertyDetector方法");
        return new MyEncryptablePropertyDetector();
    }
    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
    	System.out.println("encryptablePropertyResolver方法");
        return new MyEncryptablePropertyResolver();
    }    
	
    
}
