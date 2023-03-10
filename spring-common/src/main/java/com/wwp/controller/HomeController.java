package com.wwp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wwp.common.constant.CommonConstant;
import com.wwp.common.util.JwtUtil;
import com.wwp.common.util.RedisUtil;
import com.wwp.common.util.SaltUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.config.JwtToken;
import com.wwp.entity.LoginUser;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysUser;
import com.wwp.model.Oauth2Token;
import com.wwp.sevice.ISysDepartService;
import com.wwp.sevice.ISysRoleService;
import com.wwp.sevice.ISysUserService;
import com.wwp.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HomeController {
    private  final Logger logger = LoggerFactory.getLogger(HomeController.class);

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
        System.out.println("HomeController.login()  " + loginUser.getAccount()+" "+ loginUser.getPassword());
        Result<JSONObject> result = new Result<>();
        SysUser sysUser = sysUserService.queryUserByAccount(loginUser.getAccount());
        if(oConvertUtils.isEmpty(sysUser)){
            result.error500("用户名密码错误");
            return result;
        }

        if(sysUser.getType()==1){
            result.error500("微信车主用户无权登录");
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
            JwtToken jwtToken = new JwtToken(userToken(sysUser, result));
            subject.login(jwtToken);//这个最终就是调用的doGetAuthenticationInfo

            //以下验证是否打开了session
            //Session session = subject.getSession();
            //System.out.println("sessionId:" + session.getId());
            //System.out.println("sessionHost:" + session.getHost());
            //System.out.println("sessionTimeout:" + session.getTimeout());
            //session.setAttribute("info", "session 测试");

            SysUser sysUser1 = new SysUser();
            PropertyUtils.copyProperties(sysUser1,SecurityUtils.getSubject().getPrincipal());
            sysUserService.updateLoginTimeById(sysUser1.getId());
            System.out.println("user:  "+sysUser1.getAccount());
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
        SysUser sysUser = sysUserService.queryUserByAccount(username);
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
            return Result.error("token 为空");
        }
        return Result.error("错误");
    }

    @GetMapping("/403")
    public Result<?> noauth()  {
        return Result.error("没有权限，请联系管理员授权");
    }


    private String userToken(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getAccount();
        Object cachedToken =  redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + username);
        String token;

        //这里有个问题需要规避的就是  token更新后，传入认证token和cached auth信息里面包含的token会不一致而导致认证失败
        //可以在Jwttoken里面直接getCredentials()  里面直接返回token，导致shiro:cache:authenticationCache:**** 每次都是新的
        // 那么找不到缓存，就直接走认证了

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
        JSONObject obj = new JSONObject();
        obj.put("token", token);

        result.setResult(obj);
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


    @ApiOperation("微信用户登录")
    @RequestMapping(value = "/weChatLogin", method = RequestMethod.POST)
    public Result<JSONObject> weixinLogin(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "type", required = false) String type) throws Exception {

        Result<JSONObject> result = new Result<>();
        System.out.println("****************code:"+code);
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            Oauth2Token oauth2Token = getOauth2AccessToken(code);
            // 用户标识
            String openId = oauth2Token.getOpenId();
            logger.info("*************oauth2Token信息："+oauth2Token.toString()+"XX_"+openId);

            //1. 校验用户是否有效
            SysUser weChatUser =  sysUserService.queryUserByAccount(openId);

            if(!oConvertUtils.isEmpty(weChatUser)){

                //用户登录信息
                userToken(weChatUser, result);
                logger.info("openId: " + openId + ",登录成功！", CommonConstant.LOG_TYPE_1, null);
                weChatUser.setLoginTime(new Date());
                sysUserService.updateUser(weChatUser);
            }else{
                SysUser user=new SysUser();
                user.setPassword("WX111111");
                user.setAccount(openId);
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);

                String hashAlgorithmName = "MD5";//加密方式
                Object crdentials = "WX111111";//密码原值

                int hashIterations = 1;//加密1次
                Object passwordEncode = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);

                user.setPassword(passwordEncode.toString());
                user.setState(1);

                user.setOpenid(openId);
                user.setType(1);
                user.setLoginTime(new Date());
                sysUserService.saveUser(user,"1");
                userToken(user, result);
                return result;
            }
        }
        return result;
    }

    /**
     * 获取网页授权凭证
     * @param code
     * @return WeixinAouth2Token
     */
    public  Oauth2Token getOauth2AccessToken(String code) {
        Oauth2Token wat = null;


        boolean test=true;
        String appId=test?"wx09a13be6962ab9a8":"wx653ad587382d8bf5";
        String appSecret=test?"ab3ab13948a5887621cfbc0894ba50b9":"f5bb88a6ea418b3193261e833822e785";

        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);

        logger.info("XXXXXXXXXXrequestUrl:", requestUrl);
        // 获取网页授权凭证
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(getUrl(requestUrl));
        if (null != jsonObject) {
            try {
                wat = new Oauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInteger("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));

                //String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
                //log.info("XXXXXXXXXXtokenUrl:", tokenUrl+"xxxxxxxxxxxxxxx");
                //SNSUserInfo snsUserInfo=getSNSUserInfo(getAccessToken(tokenUrl),jsonObject.getString("openid"));
                //log.info("XXXXXXXXXXsnsUserInfo:", snsUserInfo.getHeadImgUrl());


            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wat;
    }

    public static String getUrl(String url){
        StringBuffer sb = new StringBuffer();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(httpGet);           //1
            HttpEntity entity = response.getEntity();
            InputStreamReader reader = new InputStreamReader(entity.getContent(),"utf-8");
            char [] charbufer;
            while (0<reader.read(charbufer=new char[10])){
                sb.append(charbufer);
            }
        }catch (IOException e){//1
            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
        }
        return sb.toString();
    }


}