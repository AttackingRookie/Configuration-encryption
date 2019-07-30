### 准备工作

本次加密是利用RSA非对称加密算法。利用附件中**RSAEncrypt.java**类生成加密过程中需要的公钥和私钥，以及加密后的密文。执行**main**方法即可，部分代码如下所示：

```java
private static Map<Integer, String> keyMap = new HashMap<Integer, String>();
// 用于封装随机产生的公钥与私钥
public static void main(String[] args) throws Exception {  
// 生成公钥和私钥 
genKeyPair();  
// 加密字符串  
String message = "test";   
System.out.println("随机生成的公钥为:" + keyMap.get(0));   
System.out.println("随机生成的私钥为:" + keyMap.get(1));
String messageEn = encrypt(message, keyMap.get(0));   
System.out.println("加密后的字符串为:" + messageEn);
String messageDe = decrypt(messageEn, keyMap.get(1));
System.out.println("还原后的字符串为:" + messageDe);}
```

执行后，我们需要记住生成的私钥和密文。后面将会用到。



### 加密过程

**一、基于springboot工程的改造**



1-1、在pom文件找那个引入依赖包

```xml
<dependency>
    <groupId>com.github.ulisesbocchio<groupId>
    <artifactId>jasypt-spring-boot-starter<artifactId>
    <version>2.0.0<version>
<dependency>
```

1-2、将准备工作中的密文替换到配置文件（application.properties）中需要加密的值，如本例中加密数据库密码，则修改为：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8
spring.datasource.username=test
spring.datasource.password={RAMBOO}tituII8pQIhU49R01Ta1m9IrZbzqbR/pJE0e+d4nzxAJwn93KpD=
```

**注意：密文前需要加前缀，前缀可以自己定义，示例中的为{RAMBOO}也可以定义为{GC}，注意这个前缀会在接下来用到。**

1-3、为了支持自定义的加密属性前缀，需要提供自己实现的EncryptablePropertyDetector类：

```java
package com.encryption.test.support;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;

public class MyEncryptablePropertyDetector implements EncryptablePropertyDetector {
   
   public static final String ENCODED_PASSWORD_HINT = "{RAMBOO}";

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
```

1-4、有了自定义加密属性的检测方法，我们还需要告诉工具如何进行解密操作，同时将私钥定义在代码中，通过RSA解密算法进行解密，需要提供自己实现的MyEncryptablePropertyResolver类：

```java
package com.encryption.test.support;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import com.encryption.test.support.RSAEncrypt;

public class MyEncryptablePropertyResolver implements EncryptablePropertyResolver {

   String privateKey = “30KK+ykHAk1EfkX8m4RZwZ2drpBHSeco40EFjI2uXlVevHxeiQhtwz0CpQfH";
       
   @Override
   public String resolvePropertyValue(String str) {
       try {
           if (null != str && str.startsWith(MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT)) {
               String str=RSAEncrypt.decrypt(s.substring(MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT.length()),privateKey);
               return str;
          }
      } catch (Exception e) {
           e.printStackTrace();
      }
       return s;
  }
}
```

1-5、最后，我们需要将他们注册为bean，注意：需要在启动类中添加，添加代码如下：

```java
@SpringBootApplication
@EnableEncryptableProperties
public class TestApplication {

   public static void main(String[] args) {
       SpringApplication.run(TestApplication.class, args);
  }

   @Bean(name="encryptablePropertyDetector")
   public EncryptablePropertyDetector encryptablePropertyDetector(){
       return new MyEncryptablePropertyDetector();

  }
   @Bean(name="encryptablePropertyResolver")
   public EncryptablePropertyResolver encryptablePropertyResolver(){
       return new MyEncryptablePropertyResolver();
  }

}
```

**注意，同时需要在启动类上添加@EnableEncryptableProperties注解。**





**二、基于普通spring或者spring web工程的改造**



1-1、将准备工作中的密文替换到配置文件中需要加密的值，如下所示：

```properties
jdbc.url=QZcXVgA+SFyLvx7SaaaM1AEtQrepq/tFWKi9xpAJHlnfjYRanmxO59JVlt7WShlwuv56DXdW8N7auIY=
jdbc.username=QZcXVgA+SFyLvx7SaaaM1AEtQrepq/tFWKi9xpAJHlnfjYRanmxO59JVlt7WShldfdsacvdsfe=
jdbc.password=QZcXVgA+SFyLvx7SaaaM1AEtQrepq/tFWKi9xpAJHlnfjYRanmxO59JVlt7WShldfdsacvdsfe=
```

1-2、修改spring配置文件中的数据源配置为表达式方式，如果已经是这样的，无需修改

```xml
<property name="driverClassName" value="${jdbc.driverClassName}" />
<property name="username" value="${jdbc.username}" />
<property name="password" value="${jdbc.password}" />
<property name="url" value="${jdbc.url}" />
```

1-3、在工程中，增加自定义spring读取配置文件处理类EncrypPropertyPlaceholderConfigurer类，对读取配置文件里密文值进行解密，并回写到配置文件中，同时将私钥定义在代码中，通过RSA解密算法进行解密：

```java
package com.encryption.test.util;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncrypPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{

static String privateKey="";
   static{
       privateKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK55vyO";
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
```

**注意：privateKey为第一步生成的私钥串。**

1-4、修改读取配置文件spring的bean为自定义处理类的全路径名：

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer"><property 
 name="location" value="classpath:META-INF/res/resource-${profiles.active}.properties"/>
</bean>
```

修改为

```xml
<bean class="com.encryption.test.util.EncrypPropertyPlaceholderConfigurer">
<property
name="location" value="classpath:META-INF/res/resource-${profiles.active}.properties" />
</bean>
```

**注意：请根据项目实际情况修改对应的包路径和工程配置文件名称等信息。**
