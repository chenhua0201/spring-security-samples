# spring-security-samples-102
设置内存用户的用户名、密码、密码编码器。

# 1. 示例101的用户名、密码来自哪里？
- SecurityProperties
  创建了用户：用户名、密码、角色。
- UserDetailsServiceAutoConfiguration
  创建了`InMemoryUserDetailsManager`。

# 2. 设置用户名和密码
- 明文密码  
编辑application.yml  
```yaml
  spring:
    security:
      user:
        name: zhangsan
        password: 87654321
```

- 设置密码编码器  
密码前加前缀，对密码进行编码。前缀见` PasswordEncoderFactories`  
```yaml
spring:
  security:
    user:
      name: zhangsan
      # password: 87654321
      password: "{bcrypt}$2y$10$mwZYr0mQlXIfaAwBezsBVuEK2cC2zZjJzWGhd.m0dX1iTHDusd3u6"
```

# 3. 现象
登录成功后，访问`/hello`，显示：  
``
{"authorities":[],"details":{"remoteAddress":"0:0:0:0:0:0:0:1","sessionId":"555497AC00ED54051D5F44F6FE56065E"},"authenticated":true,"principal":{"password":null,"username":"zhangsan","authorities":[],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"credentials":null,"name":"zhangsan"}
``
