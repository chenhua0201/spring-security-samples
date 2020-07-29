# spring-security-samples
Spring Security 5 示例。


# 软件版本
- Spring Boot 2.3.2
- Spring Security 5.3.3
- Spring Framework 5.2.8
- JDK 8+

# 最终形态
- 前后端分离
- 无状态会话。分布式session，或token header


## 账号密码
- RESTful 登录、注销、未登录、未授权
- 用户数据在MySQL
- 授权数据在MySQL，并缓存在JVM或（和）Redis
- ANT风格URI授权
- RBAC0
- 会话保持
  - Servlet Session
  使用Spring Session Data Redis实现分布式session
  - Token header
  自定义生成、存储、有效期、header名称
  - JWT

## OAuth2
### Opaque Token
- 自定义生成、存储、有效期
### JWT
- TODO


# 子项目说明
**所有项目运行于Servlet而不是WebFlux环境。**

## 鉴权
- 101
  + 完全使用默认配置

- 102
  + 设置内存用户的用户名、密码、密码编码器

- 103
  + 创建多个内存用户

- 104
  + 设置登录接口地址、注销接口地址

- 105
  + 设置URI是否需要鉴权

- 106
  + 使用Spring Session Redis实现分布式session
  + 客户端通过cookie传递session id

- 107
  + 使用Spring Session Redis实现分布式session
  + 客户端通过HTTP header传递session id

- 201
  + 使用Spring Session Redis实现分布式session
  + 客户端通过HTTP header传递session id
  + 自定义`UserDetailsService`，从MySQL读取用户数据

- 202
  + 使用Spring Session Redis实现分布式session
  + 客户端通过HTTP header传递session id
  + 定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应


## 鉴权+授权

## 鉴权+授权+OAuth2

## OAuth2授权服务器