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
  - Chưa có token JWT nên vẫn sử dụng session để lưu lại phiên: 
```java
HttpSession session = servletRequest.getSession(true);
session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
// Hoặc 
SecurityContext context = SecurityContextHolder.createEmptyContext();
context.setAuthentication(authentication);
SecurityContextHolder.setContext(context);
securityContextRepository.saveContext(context, servletRequest, servletResponse);
```
- Sử dụng AuthenticationManager với DaoAuthenticationProvider
- Thêm logout clear session 