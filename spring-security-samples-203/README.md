# spring-security-samples-203
使用Token保持会话。  
客户端通过HTTP header传递token。  
自定义`UserDetailsService`，从MySQL读取用户数据。  
RESTful请求和响应。  

# 1. 会话
## 1.1 禁用session

## 1.2 使用Token保持会话
  - 自定义生成规则。本例用UUID，见`security203.auth.token.TokenGeneratorUuid`
  - 自定义header名称、header值前缀、token有效期、token在redis的key前缀， 见`security203.auth.token.TokenProperties`
  - 自定义存储。本例用redis
  - `RedisTemplate`的值序列化改为JSON

## 1.3 客户端通过HTTP header保持会话
  - `TokenProperties`定义header名称。本例是`Authorization`
  - `TokenProperties`定义header值前缀。本例是`Bearer ${token}`

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
# 1. 账号表`auth_account`
```
CREATE TABLE `auth_account` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `username` varchar(30) NOT NULL COMMENT '登录名',
  `password` varchar(80) NOT NULL COMMENT '登录密码',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_username` (`username`)
) COMMENT='账号'
```

# 2. 初始化账号数据
```
--  登录名：zhangsan； 密码：87654321
insert  into `auth_account`(`id`,`username`,`password`,`enabled`,`deleted`) values
('4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','zhangsan','{bcrypt}$2y$10$mwZYr0mQlXIfaAwBezsBVuEK2cC2zZjJzWGhd.m0dX1iTHDusd3u6',1,0);
```