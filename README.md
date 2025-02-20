Cấu hình: 
- Sử dụng FormLogin (UsernamePasswordAuthenticationFilter)
- Custom UserDetailsService giả lập lấy user từ DB
- Implement UserDetails, sử dụng email để login: 
```java
@Override
public String getUsername() {
    return this.getEmail();
}
```