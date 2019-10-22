package com.example.demo.util;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;


public class MyEncryptablePropertyResolver implements EncryptablePropertyResolver {
	static String privateKey="";
    static{
    	privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALGPjtrWF2M9YIdDMz3X/ocncw1zs/AOSnmx4VEBOGfiRy1NrjzYIQRf4XxBQC9vFXZG3fbUfXP3gKrXnTJJKSS9meee85sDAlDHXt8RB1ggYpzg04voHK8wYQ7zYykypovU2DMBFNX7LlGCjWJhAn2HV75j9aJ7ICYTRh+8MnP5AgMBAAECgYBKg7NwtO3lsMlEmnfzmSRwtaZ727WEpYqtnW+wHfH87qHuKi5XzF+2xUIMchmsjlKUa1MVFcF8HD778zY99yPzWzOP0Gx5TIOmU6EtE1kF06NVGWjZ/ujf1H9mlHZtWU19KS/0SUhWABQqWWeykMw/DUBHwEs9FUKHcZSX7gVVHQJBANukATMX66bW7kHZn2AQNLoSNzhxRHkyNVwl3tzh0JRh2xd4MHPfRRx4R7idiWHfGwe3OYQ/LeUG7w0UKnYTrhMCQQDO9EfD60547pE+2VJzk+TaQQNnc4OJqMNn6TX9kaCz1i479rsxlYUs125/M7HJUhgVzOjDRtH11e37kwSZPSdDAkEAqnxVCgnJOsmeSrLXSMOLidrVzhPtaH2WeZ5TrPQC6QpD+6WpRmfJx119nkl59+QroQORu5smzp1hNK+wfdKszQJACGpklLH34fEJlP2vXaXQ85MbOVS4L5vePlZ8bJBEcauu+58/43dKEBW+l4uZmi8tbY2ElZRGq3GEmOtUj4quEwJAaxZ4lMVk1/GQT/fv2ji9g/NC9B6Hitfbbv/A8WwlU18E9lwFNAF1gU9uWUykez6Vz5b5Hg7RXbNu+g15uX9iaQ==";
    }
    //自定义解密方法
    @Override
    public String resolvePropertyValue(String s) {
    	try {
    		if (null != s && s.startsWith(MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT)) {
    			String str=RSAEncrypt.decrypt(s.substring(MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT.length()),privateKey);
    			System.out.println("》》》》》MyEncryptablePropertyResolver的解密后的明文："+str);
    			return str;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
    }
}