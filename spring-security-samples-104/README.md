# spring-security-samples-104
设置登录接口地址、注销接口地址。

# 1. 设置登录接口地址
  - `/login`改为`/sys/signin`

# 2. 设置注销接口地址
  - `/logout`改为`/sys/signout`

# 3. HttpSecurity
  - `HttpSecurity`是核心配置，绝大多数对Spring Security的自定义都通过它来完成。
