package com.wwp.controller;

import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.common.constant.CommonConstant;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.RedisUtil;
import com.wwp.common.util.SaltUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysDepart;
import com.wwp.entity.SysRole;
import com.wwp.entity.SysUser;
import com.wwp.sevice.ISysDepartService;
import com.wwp.sevice.ISysRoleService;
import com.wwp.sevice.ISysUserService;
import com.wwp.vo.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysDepartService sysDepartService;

    @Resource
    private ISysRoleService sysRoleService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户查询.
     * @return
     */
    @AutoLog(value = "查询所有账户",logType = 2)
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    @RequiresPermissions("userInfo:view")//权限管理; throw new CustomException("fuck");
    public Result<List<Map<String, Object>>> userList()
    {
        Result<List<Map<String, Object>>> result = new Result();
        List<Map<String, Object>> users = sysUserService.getAllUsers();
        result.setSuccess(true);
        result.setResult(users);
        result.setCode(200);
        return result;
    }

    @AutoLog(value = "查询部门下的所有用户",logType = 2)
    @RequestMapping(value = "/getUsersByDepId", method = RequestMethod.GET)
    public Result<List<SysUser>> userList(@RequestParam("departId") String departId)
    {
        Result<List<SysUser>> result = new Result();
        List<SysUser> users = sysUserService.queryUsersByDepId(departId);
        result.setSuccess(true);
        result.setResult(users);
        result.setCode(200);
        return result;
    }

    /**
     * 用户添加;
     * @return
     */

    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:add")
    @AutoLog(value = "注册账户",logType = 2)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> userAdd(@RequestBody JSONObject jsonObject,@RequestParam(name="departId") String departId) throws Exception{
        Result<SysUser> result = new Result<SysUser>();
        String selectedAccount = jsonObject.getString("account");
        if(oConvertUtils.isEmpty(selectedAccount)) throw new CustomException("No account selected");

        String selectedName= jsonObject.getString("name");
        if(oConvertUtils.isEmpty(selectedName)) throw new CustomException("No name selected");

        String selectedPassword = jsonObject.getString("password");
        if(oConvertUtils.isEmpty(selectedPassword)) throw new CustomException("No password selected");

        String selectedPhone = jsonObject.getString("phone");
        if(oConvertUtils.isEmpty(selectedPhone)) throw new CustomException("No phone selected");

        SysUser existUser = sysUserService.queryUserByAccount(selectedAccount);
        if(existUser != null) return Result.error("用户名: " + selectedAccount + "的账户已存在");

        try {
            //SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
           // if(user.getName().isEmpty()){throw new Exception("Name is empty");}
            SysUser user = new SysUser();

            String hashAlgorithmName = "MD5";//加密方式
            Object crdentials = selectedPassword;//密码原值
            String salt = SaltUtils.getSalt(8);;//8位随机盐
            int hashIterations = 1;//加密1次
            Object newPasswd = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
//
            user.setSalt(salt);
            user.setPassword(newPasswd.toString());
            user.setState(1);
            user.setName(selectedName);
            user.setAccount(selectedAccount);
            user.setType(0);
            user.setPhone(selectedPhone);

            // 保存用户走一个service 保证事务
            sysUserService.saveUser(user,departId);
            result.setResult(user);
            result.success200("添加用户成功！");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.error500("添加用户失败: "+e.getMessage());
            //throw e; 此处throw  就会被全局@ExceptionHandler(Exception.class) 捕获
        }
        return result;
    }

    /**
     * 用户信息;
     * @return
    */
    @RequestMapping("/info")
    public Result<?> userInfo(){

        SysUser sysUser = new SysUser();
        Result result = new Result();

        try{
            PropertyUtils.copyProperties(sysUser, SecurityUtils.getSubject().getPrincipal());
        }
        catch (Exception e){
            return result.error500(e.getMessage());
        }

        if(sysUser.getType()==0){//
            // 获取用户部门信息
            JSONObject obj = new JSONObject();
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            List<String> roles = sysRoleService.queryUserRoles(sysUser.getId());
            System.out.println(roles+sysUser.getId());
            SysDepart depart = departs.size()>0 ? departs.get(0) : null;
            obj.put("name", sysUser.getName());
            obj.put("depart", depart);

            if(roles.size()>0)
                obj.put("roles",roles );
            else obj.put("roles",null );
            result.setResult(obj);
            result.success200("获取信息成功");
        }else if(sysUser.getType()==1){
            //获取商户信息
            JSONObject obj = new JSONObject();
            // obj.put("merInfo", merchantsService.getById(sysUser.getMerId()));
            obj.put("userInfo", sysUser);
            result.setResult(obj);
            result.success200("获取信息成功");
        }else if(sysUser.getType()==2){
            //获取车主信息
            JSONObject obj = new JSONObject();
            //obj.put("merInfo", merchantsService.getById(sysUser.getMerId()));
            obj.put("userInfo", sysUser);
            result.setResult(obj);
            result.success200("获取信息成功");
        }
        else  result.error500("获取用户信息失败");
        return result;
    }

    @AutoLog(value = "删除用户",logType = 2)
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result<?> delete(@RequestParam("userId") String userId)
    {
        SysUser sysUser = sysUserService.queryUserById(userId);
        if(sysUser!=null) {
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + sysUser.getAccount());
            redisUtil.del("shiro:cache:authenticationCache:" + sysUser.getAccount());
            redisUtil.del("shiro:cache:authorizationCache:" + sysUser.getAccount());
        }
        sysUserService.deleteById(userId);
        return Result.OK();
    }

    @AutoLog(value = "编辑用户",logType = 2)
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<SysUser> edit(@RequestBody SysUser user)
    {
        SysUser sysUser = sysUserService.queryUserById(user.getId());

        if(sysUser == null) throw new CustomException("没有此用户");
        if(oConvertUtils.isNotEmpty(user.getName())) {
            sysUser.setName(user.getName());
        }
        if(oConvertUtils.isNotEmpty(user.getPassword())) {

            String hashAlgorithmName = "MD5";//加密方式
            Object crdentials = user.getPassword();//密码原值
            String salt = SaltUtils.getSalt(8);;//8位随机盐
            int hashIterations = 1;//加密1次
            Object newPasswd = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
            sysUser.setPassword(newPasswd.toString());
            sysUser.setSalt(salt);
        }
        if(oConvertUtils.isNotEmpty(user.getPhone())) {
            sysUser.setPhone(user.getPhone());
        }
//        if(oConvertUtils.isNotEmpty(user.getMemo())) {
//            sysUser.setMemo(user.getMemo());
//        }
        sysUserService.updateUser(sysUser);
        return Result.OK(sysUser);
    }

}