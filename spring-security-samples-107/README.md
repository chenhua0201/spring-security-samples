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
  - 访问需要认证的接口时，必须加上该header
  - 从Spring security提供的登录页面登录成功后，在跳转到`/`时，由于请求里没有该header，所以会再次跳转到登录页面
  - 从登录响应里取出`X-Auth-Token`，通过AJAX或postman之类的客户端设置header，才能认证成功


### 1.4 关闭csrt
  - 关闭csrf，因为postman之类的客户端无法获得csrf token而导致没有权限访问接口