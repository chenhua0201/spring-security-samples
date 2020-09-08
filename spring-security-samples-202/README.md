# spring-security-samples-202
使用Spring Session Redis实现分布式session。  
客户端通过HTTP header传递session id。  
自定义`UserDetailsService`，从MySQL读取用户数据。  
RESTful请求和响应。  

# 1. 会话
## 1.1 分布式session
  - Session存储在Redis
  - 使用Spring Session Redis
  - `RedisTemplate`的值序列化改为JSON
  
## 1.2 自定义session超时时长
  - 见`application.yml`：`spring.servlet.session.timeout`

## 1.3 客户端通过HTTP header保持会话
  - 见SpringHttpSessionConfig：Header名称默认为`X-Auth-Token`
  - 访问需要认证的接口时，必须加上该header
  - 从Spring security提供的登录页面登录成功后，在跳转到`/`时，由于请求里没有该header，所以会再次跳转到登录页面
  - 从登录响应里取出`X-Auth-Token`，通过AJAX或postman之类的客户端设置header，才能认证成功

## 1.4 关闭csrf
  - 关闭csrf，因为postman之类的客户端无法获得csrf token而导致没有权限访问接口

# 2. 用户
## 2.1 用户在MySQL
  - 创建用户`AuthAccount`

## 2.2 自定义`UserDetails`
  - 创建`AuthAccountUserDetails`

## 2.3 自定义`UserDetailsService`
  - 创建`AuthAccountUserDetailsServiceImpl`

# 3. RESTful
## 3.1 登录请求
  - 见`restful.LoginForm`

## 3.2 认证成功响应
  - 见`restful.LoginResult`

## 3.3 认证失败响应
  - 见`restful.RestAuthenticationFailureHandler`
  - 返回状态码400和JSON格式消息
```
  {
    "message": "账号或密码错误",
    "code": "LoginError.FAIL"
  }
```

## 3.4 未认证401响应
  - 见`HttpStatusEntryPoint`，返回状态码401

## 3.5 未授权403响应
  - 见`restful.RestAccessDeniedHandler`，返回状态码403

# 数据库
## 1. 账号表`auth_account`
```
CREATE TABLE `auth_account` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `username` varchar(30) NOT NULL COMMENT '登录名',
  `password` varchar(80) NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_username` (`username`)
) COMMENT='账号'
```

### 1.1 初始化账号数据
```
--  登录名：zhangsan； 密码：87654321
insert  into `auth_account`(`id`,`username`,`password`) values
('4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','zhangsan','{bcrypt}$2y$10$mwZYr0mQlXIfaAwBezsBVuEK2cC2zZjJzWGhd.m0dX1iTHDusd3u6');
```