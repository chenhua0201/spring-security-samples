# spring-security-samples-304
使用Token保持会话。  
客户端通过HTTP header传递token。  
自定义`UserDetailsService`，从MySQL读取用户数据。  
RESTful请求和响应。  
角色。  
权限。  
动态对URL进行授权，授权数据存在MySQL。
用ConcurrentHashMap缓存授权数据，并订阅redis消息，实现缓存刷新。发布刷新事件：`127.0.0.1:6379> publish authz:refresh 1`

# 1. 会话
## 1.1 禁用session

## 1.2 使用Token保持会话
  - 自定义生成规则。本例用UUID，见`security304.auth.token.TokenGeneratorUuid`
  - 自定义header名称、header值前缀、token有效期、token在redis的key前缀， 见`security304.auth.token.TokenProperties`
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
  - `UserDetails`的`getAuthorities()`代表角色
  - 角色是字符串，即AuthRole的identifier
  - 超级角色

# 5. 授权
  - 权限是：URL + HTTP method
    + HTTP method：逗号分隔，不区分大小写，`*`表示全部
    + 所有URL均要求
  - 角色关联权限
  - 超级角色允许访问所有URL
  
## 5.1 现象
  - zhangsan有超级角色ROLE_ADMIN，允许所有URL
  - lisi有角色ROLE_STUDENT，允许访问首页`/`，无权访问`/hello`

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
insert  into `auth_role`(`id`,`name`,`identifier`,`super_role`) values ('22d0295a-49b4-40f5-8f09-8d52ab5c40de','学生','ROLE_STUDENT',0);
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
-- zhangsan角色是管理员
insert  into `auth_account_role`(`id`,`account_id`,`role_id`) values ('2cdd1d1b-6b2c-4fc9-bfb3-e65ffa12ca69','4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','5558ab2d-4c61-4a18-a71c-ad73c48bb8cf');

-- lisi角色是学生
insert  into `auth_account_role`(`id`,`account_id`,`role_id`) values ('d8468b96-c1c1-4a03-acd6-27337d888887','b2516679-0e54-4390-b877-198a1678c09a','22d0295a-49b4-40f5-8f09-8d52ab5c40de');
```

## 4. 权限表
CREATE TABLE `auth_permission` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `url` varchar(50) NOT NULL COMMENT 'ANT风格URL',
  `method` varchar(50) NOT NULL COMMENT 'HTTP方法，逗号分隔，不区分大小写。*表示全部',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_name` (`name`)
) COMMENT='权限';

### 4.1 初始化权限数据
```
insert  into `auth_permission`(`id`,`name`,`url`,`method`) values ('66d7652a-1fe0-40e6-86d8-a0b0997e4fb3','hello','/hello/*','GET');
insert  into `auth_permission`(`id`,`name`,`url`,`method`) values ('f53d561e-409d-4907-a955-97de66fe674a','首页','/','GET');
```


## 5. 角色与权限的关联表
CREATE TABLE `auth_role_permission` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `role_id` varchar(36) NOT NULL COMMENT '角色ID，关联auth_role表',
  `permission_id` varchar(36) NOT NULL COMMENT '权限ID，关联auth_permission表',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_role_permission` (`role_id`,`permission_id`)
) COMMENT='角色与权限的关联';

### 5.1 初始化角色与权限关联数据
```
-- 学生角色允许访问首页
insert  into `auth_role_permission`(`id`,`role_id`,`permission_id`) values ('677c1e0d-80f4-489a-b4c1-fa7d7ef56b3d','22d0295a-49b4-40f5-8f09-8d52ab5c40de','f53d561e-409d-4907-a955-97de66fe674a');
```
