package com.encryption.bwp.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncrypPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
	
	static String privateKey="";
    static{
    	privateKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK55vyOVv7qpiE8R+ctTeKerZ7GUbzNURUOyBe19RddWwrCxNuwmDTGlVz45V30CQwAaViHkW4SgqqE7bWOS9iKPuPLAXLhTIRN3mAex6Hx8L4BJHuqvXdK8kxstrQWMMOSy4lHNT9/pFCjW/czUncUKelwXteuOLaRWaqh+Mq9hAgMBAAECgYEAjM+9L18AKgoA9K+xRiPL0KZJPcxqIqb0b7oRhqX/49clsayUguoC4/po+tMUhNN6d27J7Ph71AJHM3Dey+ojsk9TbtyqDCTkATaeGzVVTsdxUqftbV1TeL8BAfDyYRsIPKH13HZ8T8SI3iZ8FrDZbMQecq6671nnxrO/iyGCqUECQQD40DLhvRo/7H4CRTUWky6zhYUDMWFUQd0ZSO0M0/wOs5Hi77ZkDWKnlf5qalO6HPTFV3eqs+Dlr3px2IYAjBT1AkEAs4PfSm+jlBXoNeIoJJNBhnOboLAoj5c3Rr1Pjc40bJ7MhO8mM+sp8dx5w/AWKxgk+5wroH+QKX59ZU4qnfRNPQJBAL2RgvLSrQk/GdKJACWvBcnpVKpWGR2lANue4F0Bte1Niz0n/gLE4AFnGmvhjuEh5qvkNFxuqE7XH6dVnOMV13ECQAiCsPxTbJrhqv+a+DI7tPiN1Iv6rgGYtqso4HOlmgCoSqPmvpXpiTSJMlCiujpSy7YOAld6lLgS4hkCaqzQXzUCQQDUwL/fdLMo8tD0kzmIvlyZU1n4Jb734wIHDD1iQp1F7sosUIXbXDchB8v2Vam3snWKSc5Lc2pIbkRqcrrRkg8T";
    }
    
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,Properties props) throws BeansException {
    	try {
        	String username = props.getProperty("jdbc.username");
        	if (username != null) {
        		props.setProperty("jdbc.username",
        				RSAEncrypt.decrypt(username,privateKey));
        	}
        	String url = props.getProperty("jdbc.url");
        	if (url != null) {
        		props.setProperty("jdbc.url",
        				RSAEncrypt.decrypt(url,privateKey));
        	}
            String password = props.getProperty("jdbc.password");
            if (password != null) {
                props.setProperty("jdbc.password",
                		RSAEncrypt.decrypt(password,privateKey));
            }
            super.processProperties(beanFactoryToProcess, props);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
