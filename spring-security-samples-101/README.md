# spring-security-samples-101
示例101。

# 说明
## 1. 完全使用默认配置：
  - 启动时自动创建一个用户
    - 用户名：`user`
    - 随机密码，见INFO级别的日志：`Using generated security password`
  - 表单登录
    - 登录：`/login`
    - 注销：`/logout`
  - 密码编解码器是`BCrypt`
  - 用cookie保持会话


## 2. 现象
  - 请求的content type是`text/html  `
    在浏览器打开`http://localhost:8080/hello`，重定向到`/login`，显示登录表单。

  - 请求的content type是`applicaion/json  `  
  `curl -i -X GET 'http://localhost:8080/hello'`  
  返回状态码401及：  
```
{
    "timestamp": "2020-07-24T20:16:48.974+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "",
    "path": "/hello"
}
```

- 登录成功  
  在浏览器登录页面输入用户名和密码，提交表单，跳转到首页`http://localhost:8080`，显示  
  `这是首页`
  
- 会话保持  
  浏览器端保存了一个cookie，主要属性如下：  
  name：`JSESSIONID`  
  Path：`/`  
  Expires： `Session`  
  HttpOnly： `true`  

  
- 再次访问`/hello`，正确显示：  
``
org.springframework.security.core.userdetails.User@36ebcb: Username: user; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Not granted any authorities
``
  