# spring-security-samples-oauth2-authz-server
OAuth2授权服务器，基于已废弃的spring-security-oauth2-autoconfigure。
自定义`UserDetailsService`，从MySQL读取用户数据。  

# 账号
## 账号在MySQL
- 见`AuthAccount`

## 自定义`UserDetails`
- 见`AuthAccountUserDetails`

## 自定义`UserDetailsService`
- 见`AuthAccountUserDetailsServiceImpl`

# Token操作
## 认证
- 表单
```
curl -L -X POST 'http://localhost:8080/oauth/token' -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'grant_type=password' --data-urlencode 'username=zhangsan' --data-urlencode 'password=87654321' --data-urlencode 'scope=all' --data-urlencode 'client_id=sample-app' --data-urlencode 'client_secret=62jxxRnbs7rBwB5F'
```

- Basic认证
```
curl -L -X POST 'http://localhost:8080/oauth/token' -H 'Authorization: Basic c2FtcGxlLWFwcDo2Mmp4eFJuYnM3ckJ3QjVG' -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'grant_type=password' --data-urlencode 'username=' --data-urlencode 'password=87654321' --data-urlencode 'scope=all'
```

- 返回
```
{
    "access_token": "CtjPS4NtkPdgAmZBsHn78Zf34Rg=",
    "token_type": "bearer",
    "refresh_token": "8FQK3xMlB0IbzOOWQFuh9pF1I9U=",
    "expires_in": 31535999,
    "scope": "all",
    "userId": "4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38",
    "username": "zhangsan"
}
```

## 刷新Token
```
curl -L -X POST 'http://localhost:8080/oauth/token' -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'grant_type=refresh_token' --data-urlencode 'username=zhangsan' --data-urlencode 'password=87654321' --data-urlencode 'client_id=sample-app' --data-urlencode 'client_secret=62jxxRnbs7rBwB5F' --data-urlencode 'refresh_token=f50459a6-3e2f-49d3-9f5f-6b4c0254af05'
```

- 返回
```
{
    "access_token": "Ypi7GL0QtItqrVQaBQSUzldeCME=",
    "token_type": "bearer",
    "refresh_token": "Wk51HllszSB5YFV/hDqxVVTNyQo=",
    "expires_in": 31535999,
    "scope": "all",
    "userId": "4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38",
    "username": "zhangsan"
}
```

## 检查Token
```
curl -L -X POST 'http://localhost:8080/oauth/check_token?token=Ypi7GL0QtItqrVQaBQSUzldeCME=' -H 'Authorization: Basic c2FtcGxlLWFwcDo2Mmp4eFJuYnM3ckJ3QjVG'
```

- 返回
```
{
    "user_name": "zhangsan",
    "scope": [
        "all"
    ],
    "active": true,
    "exp": 1631260363,
    "userId": "4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38",
    "client_id": "sample-app",
    "username": "zhangsan"
}
```

# 数据库
## 账号表`auth_account`
```
CREATE TABLE `auth_account` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `username` varchar(30) NOT NULL COMMENT '登录名',
  `password` varchar(80) NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_username` (`username`)
) COMMENT='账号'
```

### 初始化账号数据
```
--  登录名：zhangsan； 密码：87654321
insert  into `auth_account`(`id`,`username`,`password`) values
('4e4000ba-4c36-4cd0-8a02-4bd7d38e8f38','zhangsan','{bcrypt}$2y$10$mwZYr0mQlXIfaAwBezsBVuEK2cC2zZjJzWGhd.m0dX1iTHDusd3u6');

```

## 客户端表`oauth_client_details`
```
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB;'
```

### 初始化客户端数据
```
--  client_secret=bcrypt("62jxxRnbs7rBwB5F")。 其中"62jxxRnbs7rBwB5F"是client_id为sample-app的密钥。
insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`) values 
('sample-app',NULL,'{bcrypt}$2y$10$yah8yIHUtcedUtD4xj3p8exbNXoYRnGV4Udi79YgjrqaiNGECllnG','all','password,refresh_token',NULL,NULL,31536000,NULL,NULL,'true');
```