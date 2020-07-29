# spring-security-samples-107
示例107。

# 说明
## 1. 分布式session
  - Session存储在Redis
  - 使用Spring Session Redis


## 2. 自定义session超时时长
  - 见`application.yml`：`spring.servlet.session.timeout`

## 3. 客户端通过HTTP header保持会话
  - 见SpringHttpSessionConfig：Header名称默认为`X-Auth-Token`