package com.wwp.config;

import com.wwp.common.constant.CommonConstant;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.JwtUtil;
import com.wwp.common.util.RedisUtil;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysPermission;
import com.wwp.entity.SysRole;
import com.wwp.entity.SysUser;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.mapper.SysUserMapper;
import com.wwp.mapper.SysUserRoleMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
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
import org.springframework.context.annotation.Lazy;


public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    @Lazy
    private RedisUtil redisUtil;

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
        SysUser sysUser = new SysUser();
        try{
            PropertyUtils.copyProperties(sysUser, SecurityUtils.getSubject().getPrincipal());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for(SysRole role: sysUserRoleMapper.getRoleByUserName(sysUser.getUsername())){
            authorizationInfo.addRole(role.getRole());
            System.out.println("role: "+role.getRole());
            for(SysPermission p: sysPermissionMapper.queryByUser(sysUser.getUsername())){
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
       System.out.println("----->>userInfo.passwd="+userInfo.getPassword());
        if(userInfo == null){
            throw new AuthenticationException("用户不存在!");
        }
        if (!jwtTokenRefresh(token, username, userInfo.getPassword())) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户名
                token,
                getName()  //realm name
        );
        //这里使用的是不带hash的token，最终没有进行密码比对，token无法默认得到密码原文(因为这时候token是这个JwtToken)
        //因为没有使用HashedCredentialsMatcher  最终使用的是SimpleCredentialsMatcher  也就是单纯的比较 auth.getCredentials和
        //authenticationInfo里面的token.getCredentials 这两个就是一个值  验证当然通过
        return authenticationInfo;
    }

    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)，缓存有效期设置为Jwt有效时间的2倍
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 4、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 注意： 前端请求Header中设置Authorization保持不变，校验有效性以缓存中的token为准。
     *       用户过期时间 = Jwt有效时间 * 2。
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String passWord) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + userName));
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                return false;
            }
            //update-begin--Author:scott  Date:20191005  for：解决每次请求，都重写redis中 token缓存问题
		else {
				// 设置超时时间
				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + userName, JwtUtil.EXPIRE_TIME / 1000);
			}
            return true;
        }
        return false;
    }

    /**
     * 清除当前用户的权限认证缓存
     *
     * @param principals 权限信息
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }


}