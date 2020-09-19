# spring-security-samples-107
使用Spring Session Data Redis实现分布式session。  
客户端通过HTTP header传递session id。

# 会话
## 分布式Session
- Session存储在Redis
- 使用Spring Session Data Redis
- `RedisTemplate`的值序列化改为JSON

## 自定义Session超时时长
- 见`application.yml`：`spring.servlet.session.timeout`

## 客户端通过HTTP header保持会话
- 见SpringHttpSessionConfig：header名称默认为`X-Auth-Token`
- 访问需要认证的接口时，必须加上该header
- 从Spring security提供的登录页面登录成功后，在跳转到`/`时，由于请求里没有该header，所以会再次跳转到登录页面
- 从登录响应里取出`X-Auth-Token`，通过AJAX或postman之类的客户端设置header，才能认证成功

## 关闭csrf
- 关闭csrf，因为postman之类的客户端无法获得csrf token而导致没有权限访问接口
