# spring-security-samples-302
使用Token保持会话。  
客户端通过HTTP header传递token。  
自定义`UserDetailsService`，从MySQL读取用户数据。  
RESTful请求和响应。  
增加角色。
在Java方法上授权。

# 1. 会话
## 1.1 禁用session

## 1.2 使用Token保持会话
  - 自定义生成规则。本例用UUID，见`security302.auth.token.TokenGeneratorUuid`
  - 自定义header名称、header值前缀、token有效期、token在redis的key前缀， 见`security302.auth.token.TokenProperties`
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

# 4. 角色
## 4.1 Authority
  - `UserDetails`的`getAuthorities()`代表角色
  - 角色是字符串

## 4.2 设置静态角色
  - `AuthAccountUserDetailsServiceImpl`加载 `UserDetails`时，从MySQL的`auth_role`表读取角色

# 5. 授权
  - 启用`@EnableGlobalMethodSecurity(prePostEnabled = true)`
  - 在Java方法上增加`@PreAuthorize("hasRole('ADMIN')")`

## 5.1 现象
  - zhangsan有角色ADMIN，所以有权访问`/hello`；而lisi无权访问，返回状态码403
  - zhangsan和lisi都有权访问`/index`接口

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

-- 登录名：lisi；密码：88888888
insert  into `auth_account`(`id`,`username`,`password`) values
('b2516679-0e54-4390-b877-198a1678c09a','lisi','{bcrypt}$2y$10$X/l.uHOi36hnQiRM0xzfT.lbsu1untgBtXg3CAsMlhcGZtfetpnru');
```

## 2. 角色表`auth_role`
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

### 2.1 初始化角色数据
```
insert  into `auth_role`(`id`,`name`,`identifier`,`super_role`) values ('5558ab2d-4c61-4a18-a71c-ad73c48bb8cf','管理员','ROLE_ADMIN',1);
```

## 3. 账号与角色关联表
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

### 3.1 初始化账号与角色关联数据
```
--  zhangsan有ADMIN角色；lisi没有任何角色
insert  into `auth_account_role`(`id`,`account_id`,`role_id`) values ('2cdd1d1b-6b2c-4fc9-bfb3-e65ffa12ca69','4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','5558ab2d-4c61-4a18-a71c-ad73c48bb8cf');
```