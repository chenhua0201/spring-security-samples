# spring-security-samples-102
示例102。

# 说明
## 1. 示例101的用户名、密码来自哪里？
  - SecurityProperties
    创建了用户：用户名、密码、角色。
  - UserDetailsServiceAutoConfiguration
    创建了`InMemoryUserDetailsManager`。

## 2. 设置用户名和密码
编辑application.yml
  ```
  spring:
    security:
      user:
        name: zhangsan
        password: 87654321
  ```

## 3. 设置密码编码器PasswordEncoder
编辑application.yml，password改成经bcrypt编码后的字符串，再加上`org.springframework.security.crypto.password.DelegatingPasswordEncoder`定义的前缀，
  ```
spring:
  security:
    user:
      name: zhangsan
      # password: 87654321
      password: "{bcrypt}$2y$10$mwZYr0mQlXIfaAwBezsBVuEK2cC2zZjJzWGhd.m0dX1iTHDusd3u6"
```
