# spring-security-samples-106
使用Spring Session Redis实现分布式session。

# 1. 分布式session
  - Session存储在Redis
  - 使用Spring Session Redis

# 2. 自定义session超时时长
  - 见`application.yml`：`spring.servlet.session.timeout`

# 3. 客户端通过cookie保持会话
  - cookie名称为`SESSION`