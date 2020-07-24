# spring-security-samples
Spring Security 5 示例。


# 软件版本
- Spring Boot 2.3.1
- Spring Security 5.3.3
- Spring Framework 5.2.7
- JDK 8+

# 最终形态
- 前后端分离
- 无状态


## 账号密码
- RESTful 登录、注销、未登录、未授权
- 用户数据在MySQL
- 授权数据在MySQL，并缓存在JVM或Redis
- ANT风格URI授权
- RBAC0
- 凭证
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
  完全使用默认配置
- 102  
  自定义密码编解码器
- 103  
  自定义用户
  
## 鉴权+授权

## 鉴权+授权+OAuth2
  
## OAuth2授权服务器