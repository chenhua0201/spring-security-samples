# spring-security-samples-106
使用Spring Session Data Redis实现分布式session。

# 分布式Session
  - Session存储在Redis
  - 使用Spring Session Data Redis
  - `RedisTemplate`的值序列化改为JSON

# 自定义Session超时时长
- 见`application.yml`：`spring.servlet.session.timeout`

# 客户端通过Cookie保持会话
- cookie名称为`SESSION`