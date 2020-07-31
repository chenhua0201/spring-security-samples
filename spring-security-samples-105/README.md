# spring-security-samples-105
设置URI是否需要认证。

# 1. 设置URI是否需要认证
  - 首页接口（`/`）不需要认证
  - hello接口（`/hello`）需要认证

# 2. 路径匹配规则
  - `AntPathRequestMatcher`是关键