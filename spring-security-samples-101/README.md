# spring-security-samples-101
完全使用默认配置。

# 1. 完全使用默认配置
- 启动时自动创建一个用户
  + 用户名：`user`
   + 随机密码，见INFO级别的日志：`Using generated security password`
- 表单登录
  + 登录：`/login`
  + 注销：`/logout`
- 无密码编解码器，相当于NoOpPasswordEncoder
  - 用cookie保持会话

# 2. 现象
- 请求的content type是`text/html`  
    在浏览器打开`http://localhost:8080/hello`，重定向到页面`/login`，显示登录表单。

- 请求的content type是`applicaion/json`  
  `curl -i -X GET 'http://localhost:8080/hello'`  
  返回状态码401及：
```
{
    "timestamp": "2099-01-01T00:00:01.001+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "",
    "path": "/hello"
}
```

- 登录成功  
  在浏览器登录页面输入用户名和密码，提交表单，跳转到首页`http://localhost:8080`，显示  
```
这是首页
```

- 会话保持  
  浏览器端保存了一个cookie，主要属性如下：  
```
name：`JSESSIONID`
Path：`/`
Expires： `Session`
HttpOnly： `true`
```
- 再次访问`/hello`，显示：  
``
{"authorities":[],"details":{"remoteAddress":"0:0:0:0:0:0:0:1","sessionId":"EC814548475ECBE71E5E5135C5BA542F"},"authenticated":true,"principal":{"password":null,"username":"user","authorities":[],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"credentials":null,"name":"user"}
``
