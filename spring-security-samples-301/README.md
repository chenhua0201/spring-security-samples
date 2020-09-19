# spring-security-samples-301
使用Token保持会话。  
客户端通过HTTP header传递token。  
自定义`UserDetailsService`，从MySQL读取用户数据。  
RESTful请求和响应。  
角色。
在配置里对URI授权。

# 会话
## 禁用session

## 使用Token保持会话
- 自定义生成规则。本例用UUID，见`security301.auth.token.TokenGeneratorUuid`
- 自定义header名称、header值前缀、token有效期、token在redis的key前缀， 见`security301.auth.token.TokenProperties`
- 自定义存储。本例用redis
- `RedisTemplate`的值序列化改为JSON

## 1.3 客户端通过HTTP header保持会话
- `TokenProperties`定义header名称。本例是`Authorization`
- `TokenProperties`定义header值前缀。本例是`Bearer ${token}`

## 1.4 关闭csrf
- 关闭csrf，因为postman之类的客户端无法获得csrf token而导致没有权限访问接口

# 2. 账号
## 2.1 账号在MySQL
- 见`AuthAccount`

## 2.2 自定义`UserDetails`
- 见`AuthAccountUserDetails`

## 2.3 自定义`UserDetailsService`
- 见`AuthAccountUserDetailsServiceImpl`

# RESTful
## 登录请求
- 见`restful.LoginForm`  
```json
{
    "username": "zhangsan",
    "password": "87654321"
}
```

## 认证成功响应
- 见`restful.LoginResult`  
```json
{
    "accountId": "4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38",
    "username": "zhangsan"
}
```
- 响应header
```
X-Auth-Token: e72b2bd1-0cb7-4f40-81b9-c7b6f4a36f4a
```

## 认证失败响应
- 见`restful.RestAuthenticationFailureHandler`
- 返回状态码400和JSON格式消息  
```
  {
    "message": "账号或密码错误",
    "code": "LoginError.FAIL"
  }
```

## 未认证401响应
- 见`HttpStatusEntryPoint`，返回状态码401

## 未授权403响应
- 见`restful.RestAccessDeniedHandler`，返回状态码403

# 角色
## Authority
- `UserDetails`的`getAuthorities()`代表角色
- 角色是字符串

## 设置静态角色
- `AuthAccountUserDetailsServiceImpl`加载 `UserDetails`时，从MySQL的`auth_role`表读取角色

#  授权
- 在`HttpSecurity`设置URI所需的角色

## 现象
- zhangsan有角色ADMIN，所以有权访问`/hello`；而lisi无权访问，返回状态码403
- zhangsan和lisi都有权访问`/index`接口

# 数据库
## 账号表`auth_account`
```
CREATE TABLE `auth_account` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `username` varchar(30) NOT NULL COMMENT '登录名',
  `password` varchar(80) NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_username` (`username`)
) COMMENT='账号';
```

### 初始化账号数据
```
--  登录名：zhangsan； 密码：87654321
insert  into `auth_account`(`id`,`username`,`password`) values
('4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','zhangsan','{bcrypt}$2y$10$mwZYr0mQlXIfaAwBezsBVuEK2cC2zZjJzWGhd.m0dX1iTHDusd3u6');

-- 登录名：lisi；密码：88888888
insert  into `auth_account`(`id`,`username`,`password`) values
('b2516679-0e54-4390-b877-198a1678c09a','lisi','{bcrypt}$2y$10$X/l.uHOi36hnQiRM0xzfT.lbsu1untgBtXg3CAsMlhcGZtfetpnru');
```

## 角色表`auth_role`
```
CREATE TABLE `auth_role` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `identifier` varchar(50) NOT NULL COMMENT '标识',
  `super_role` tinyint NOT NULL DEFAULT '0' COMMENT '是否是超级用户',
  `description` varchar(100) NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_identifier` (`identifier`)
) COMMENT='角色';

```

### 初始化角色数据
```
insert  into `auth_role`(`id`,`name`,`identifier`,`super_role`) values ('5558ab2d-4c61-4a18-a71c-ad73c48bb8cf','管理员','ROLE_ADMIN',1);
```

## 账号与角色关联表
```
CREATE TABLE `auth_account_role` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) NOT NULL COMMENT '账号ID，关联auth_account表',
  `role_id` varchar(36) NOT NULL COMMENT '角色ID，关联auth_role表',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_account_role` (`account_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) COMMENT='账号与角色的关联';
```

### 初始化账号与角色关联数据
```
--  zhangsan角色是“管理员”
insert  into `auth_account_role`(`id`,`account_id`,`role_id`) values ('2cdd1d1b-6b2c-4fc9-bfb3-e65ffa12ca69','4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','5558ab2d-4c61-4a18-a71c-ad73c48bb8cf');

```