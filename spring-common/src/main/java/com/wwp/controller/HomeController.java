package com.wwp.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwp.common.constant.CommonConstant;
import com.wwp.common.util.JwtUtil;
import com.wwp.common.util.RedisUtil;
import com.wwp.common.util.SaltUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.config.JwtToken;
import com.wwp.entity.LoginUser;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysRole;
import com.wwp.entity.SysUser;
import com.wwp.sevice.ISysDepartService;
import com.wwp.sevice.ISysRoleService;
import com.wwp.sevice.ISysUserService;
import com.wwp.vo.Result;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HomeController {
    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysDepartService sysDepartService;

    @Resource
    private ISysRoleService sysRoleService;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "login ok ";
    }

    @RequestMapping("/login")
    public Result<JSONObject> login(HttpServletRequest request, @RequestBody LoginUser loginUser) throws Exception {
        System.out.println("HomeController.login()  " + loginUser.getUsername()+" "+ loginUser.getPassword());
        Result<JSONObject> result = new Result<>();
        SysUser sysUser = sysUserService.queryUserByUsername(loginUser.getUsername());
        if(oConvertUtils.isEmpty(sysUser)){
            result.error500("用户名密码错误");
            return result;
        }

        String hashAlgorithmName = "MD5";//加密方式
        Object crdentials = loginUser.getPassword();//密码原值
        String salt = sysUser.getSalt();;//8位随机盐
        int hashIterations = 1;//加密1次
        Object passwd = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);

        if(!sysUser.getPassword().equals(passwd.toString())){
            result.error500("用户名密码错误");
            return result;
        }
        Subject subject = SecurityUtils.getSubject();

       // UsernamePasswordToken token = new UsernamePasswordToken(loginUser.getUsername(), loginUser.getPassword());

        try {
            JwtToken jwtToken = new JwtToken(userInfo(sysUser, result));
            subject.login(jwtToken);//这个最终就是调用的doGetAuthenticationInfo
            //Session session = subject.getSession();
            //System.out.println("sessionId:" + session.getId());
            //System.out.println("sessionHost:" + session.getHost());
            //System.out.println("sessionTimeout:" + session.getTimeout());
            //session.setAttribute("info", "session 测试");

            SysUser sysUser1 = new SysUser();
            PropertyUtils.copyProperties(sysUser1,SecurityUtils.getSubject().getPrincipal());
            System.out.println("user:  "+sysUser1.getUsername());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("user", loginUser);
            request.setAttribute("errorMsg", "failure");
            return Result.error(500,"err when login");
        }
    }

    @RequestMapping(value = "/logout",method= RequestMethod.GET)
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if(oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = sysUserService.queryUserByUsername(username);
        if(sysUser!=null) {
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + username);
            redisUtil.del("shiro:cache:authenticationCache:" + username);
            redisUtil.del("shiro:cache:authorizationCache:" + username);

            Subject subject = SecurityUtils.getSubject();
            subject.logout();

            return Result.OK("退出登录OK");
        }
        else {
            return Result.error("Token无效!");
        }
    }

    @GetMapping(value = "/error")
    public Result<Object> error(HttpServletRequest request, HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (token.isEmpty()) {
            return Result.error("退出登录失败！");
        }
        return Result.OK("退出登录OK");
    }

    @GetMapping("/403")
    public Result<?> noauth()  {
        return Result.error("没有权限，请联系管理员授权");
    }


    private String userInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        Object cachedToken =  redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + username);
        String token;
        if(oConvertUtils.isEmpty(cachedToken)){// 生成token
            token = JwtUtil.sign(username, syspassword);
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + username, token);
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + username, JwtUtil.EXPIRE_TIME / 2000);
            redisUtil.del("shiro:cache:authenticationCache:"+username);
            redisUtil.del("shiro:cache:authorizationCache:"+username);
        }

        else {
            token = cachedToken.toString();

        }
        // TODO 设置token缓存有效时间

        //这里有个问题需要规避的就是  token更新后，传入认证token和cached auth信息里面包含的token会不一致而导致认证失败
        //可以在Jwttoken里面直接getCredentials()  里面直接返回token，导致shiro:cache:authenticationCache:**** 每次都是新的
        // 那么找不到缓存，就直接走认证了

        if(true){//sysUser.getType()==0
            // 获取用户部门信息
            JSONObject obj = new JSONObject();
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            List<SysRole> roles = sysRoleService.queryUserRoles(sysUser.getId());
            SysDepart depart = departs.size()>0 ? departs.get(0) : null;
           // SysDepart park = queryCurrentPark(depart);
            obj.put("depart", depart);
            obj.put("token", token);
           // obj.put("userInfo", sysUser);
          //  obj.put("park", park);
            obj.put("role", roles.size()>0 ? roles.get(0) : null);
            result.setResult(obj);
        }else if(sysUser.getType()==1){
            //获取商户信息
            JSONObject obj = new JSONObject();
           // obj.put("merInfo", merchantsService.getById(sysUser.getMerId()));
            obj.put("token", token);
            obj.put("userInfo", sysUser);
            result.setResult(obj);
        }else if(sysUser.getType()==2){
            //获取车主信息
            JSONObject obj = new JSONObject();
            //obj.put("merInfo", merchantsService.getById(sysUser.getMerId()));
            obj.put("token", token);
            obj.put("userInfo", sysUser);
            result.setResult(obj);
        }
        result.success200("登录成功");
        return token;
    }

    private SysDepart queryCurrentPark(SysDepart depart) {
        SysDepart park = null;
        if(depart != null) {  //有组织机构
            if(depart.getOrgCategory() == 4) {  //如果登录是车场，则直接是自己
                park = depart;
            } else {
                List<SysDepart> list = sysDepartService.queryChildDepartById(depart.getId());
                if(list.size()>0) {
                    list = list.stream().filter(t -> t.getOrgCategory() == 4).collect(Collectors.toList());
                    if(list.size()>0) {
                        //list.sort(Comparator.comparing(SysDepart::getCreateTime));
                        park = list.get(0);
                    }
                }
            }
        } else {  //没有组织机构，是平台账号，获取第一个机构就可以
           // LambdaQueryWrapper<SysDepart> departQuery = new LambdaQueryWrapper<>();
           // departQuery.ne(SysDepart::getDelFlag, "1").eq(SysDepart::getOrgCategory, 4).orderByAsc(SysDepart::getCreateTime);
          //  park = sysDepartService.list(departQuery).get(0);
        }
        if(park != null) {
          //  List<SysDepart> parentList = sysDepartService.queryParentDepartById(park.getId());
         //   park.setParentList(parentList);
        }
        return park;
    }

}