# spring-security-samples-201
示例201。

# 说明
## 1. 用户在MySQL
  - 创建用户`AuthAccount`

## 2. 自定义`UserDetails`
  - 创建`AuthAccountUserDetails`

## 2. 自定义`UserDetailService`
  - 创建`AuthAccountUserDetailsServiceImpl`

# 数据库
## 1. 账号表`auth_account`
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

## 2. 初始化账号数据
```
--  登录名：zhangsan； 密码：111111
insert  into `auth_account`(`id`,`username`,`password`,`enabled`,`deleted`) values
('4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','zhangsan','{bcrypt}$2y$10$zx1/azyHgk94H0vFH/4OV.k7Nvh7MwB6G.1zMxqscc6B.kuIi.I9q',1,0);
```