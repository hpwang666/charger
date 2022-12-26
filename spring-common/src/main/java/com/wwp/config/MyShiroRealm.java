package com.wwp.config;

import com.wwp.common.exception.CustomException;
import com.wwp.common.util.JwtUtil;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysPermission;
import com.wwp.entity.SysRole;
import com.wwp.entity.SysUser;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.mapper.SysUserMapper;
import com.wwp.mapper.SysUserRoleMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser userInfo  = (SysUser)principals.getPrimaryPrincipal();

        for(SysRole role: sysUserRoleMapper.getRoleByUserName(userInfo.getUsername())){
            authorizationInfo.addRole(role.getRole());
            System.out.println("role: "+role.getRole());
            for(SysPermission p: sysPermissionMapper.queryByUser(userInfo.getUsername())){
                System.out.println("permission: "+p.getPermission());
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
            throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String token = (String) auth.getCredentials();
        if(oConvertUtils.isEmpty(token)) throw new AuthenticationException("token 为空");
        String username = JwtUtil.getUsername(token);
        System.out.println("username: "+ username);

        //TODO 需要判断  更新 token有效期
        System.out.println("需要判断  更新 token有效期");

        SysUser userInfo = sysUserMapper.getUserByUsername(username);
       System.out.println("----->>userInfo="+userInfo.getPassword());
        if(userInfo == null){
            throw new AuthenticationException("用户不存在!");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                token,
                getName()  //realm name
        );//这里使用的是不带hash的token，最终没有进行密码比对，token无法默认得到密码原文(因为这时候token是这个JwtToken)
        return authenticationInfo;
    }

}