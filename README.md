# spring-security-samples
Spring Security 5 示例。

# 软件版本
- Spring Boot 2.3.4
- Spring Security 5.3.4
- Spring Framework 5.2.9
- JDK 8+

# 最终形态
- 前后端分离
- 无状态会话分布式session，或token header

## 1. 账号密码
- RESTful 登录、注销、未登录异常、未授权异常
- 用户数据在MySQL

## 2. 授权
- 授权数据在MySQL，并缓存在JVM
- ANT风格URL授权
- RBAC0

## 3. 会话保持
- Servlet Session  
  使用Spring Session Data Redis实现分布式session
- Token header  
  自定义值的生成方式、存储方式、有效期、header名称

# 子项目说明
**所有项目运行于Servlet而不是WebFlux环境。**

## 认证，无数据库
- 101
  + 完全使用默认配置

- 102
  + 设置内存用户的用户名、密码、密码编码器

- 103
  + 创建多个内存用户

- 104
  + 设置登录页面地址、注销页面地址

- 105
  + 设置URL是否需要认证

- 106
  + 使用Spring Session Data Redis实现分布式session
  + 客户端通过cookie传递session id

- 107
  + 使用Spring Session Data Redis实现分布式session
  + 客户端通过HTTP header传递session id

## 认证，数据源是MySQL
- 201
  + 使用Spring Session Data Redis实现分布式session
  + 客户端通过HTTP header传递session id
  + 自定义`UserDetailsService`，从MySQL读取用户数据

- 202
  + 使用Spring Session Data Redis实现分布式session
  + 客户端通过HTTP header传递session id
  + 自定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应

- 203
  + 使用Token保持会话
  + 客户端通过HTTP header传递token
  + 自定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应

- 204
  + 使用Token保持会话。使用`AuthenticationProvider`实现。
  + 客户端通过HTTP header传递token
  + 自定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应

## 认证+授权
- 301
  + 使用Token保持会话
  + 客户端通过HTTP header传递token
  + 自定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应
  + 角色
  + 在配置里对URI授权

- 302
  + 使用Token保持会话
  + 客户端通过HTTP header传递token
  + 自定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应
  + 角色
  + 在Java方法上授权

- 303
  + 使用Token保持会话
  + 客户端通过HTTP header传递token
  + 自定义`UserDetailsService`，从MySQL读取用户数据
  + RESTful请求和响应
  + 角色
  + 权限
  + 动态对URL进行授权，授权数据存在MySQL
  + 用ConcurrentHashMap缓存授权数据，并订阅redis消息，实现缓存刷新。发布刷新事件：`127.0.0.1:6379> publish authz:refresh 1`

## OAuth2授权服务器
- spring-security-samples-oauth2-authz-server，基于已废弃的spring-security-oauth2-autoconfigure

## OAuth2资源服务器
- spring-security-samples-oauth2-resource-server，基于已废弃的spring-security-oauth2-autoconfigure