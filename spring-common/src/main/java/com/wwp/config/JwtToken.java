package com.wwp.config;
 
import com.wwp.common.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author Scott
 * @create 2018-07-12 15:19
 * @desc
 **/
public class JwtToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private String token;
    private String username;

 
    public JwtToken(String token) {
        this.token = token;
        this.username = JwtUtil.getUsername(token);;
    }
 
    @Override
    public Object getPrincipal() {
        return   this.username;
    }
 
    @Override
    public Object getCredentials() {

        return token;//
    }

    public String getUsername() {
        return this.username;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.username);


        return sb.toString();
    }



}
