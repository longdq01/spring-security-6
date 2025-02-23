Cấu hình: 
- ~~Sử dụng FormLogin (UsernamePasswordAuthenticationFilter)~~
- Custom UserDetailsService ~~giả lập~~ lấy user từ DB
- Implement UserDetails, sử dụng email để login: 
```java
@Override
public String getUsername() {
    return this.getEmail();
}
```
- Custom login qua API hoặc form login (đặt thuộc tính http.form-login.enabled trong config):
- Sử dụng AuthenticationManager với DaoAuthenticationProvider
- Thêm logout clear session 
- Sử dụng custom filter: OncePerRequestFilter, GenericBeanFilter, ...
- Thêm jwt token
```java
http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
```
- Thêm refreshToken (hiện tại giống accessToken nhưng khác thời gian hết hạn)
- logout có thể dùng redis để set lại expire của token 