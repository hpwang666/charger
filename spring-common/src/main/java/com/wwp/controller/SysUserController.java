package com.wwp.controller;

import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.common.exception.CustomException;
import com.wwp.common.util.SaltUtils;
import com.wwp.common.util.oConvertUtils;
import com.wwp.entity.SysUser;
import com.wwp.sevice.ISysUserService;
import com.wwp.vo.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;
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

    /**
     * 用户添加;
     * @return
     */

    //@RequiresRoles({"admin"})
    //@RequiresPermissions("user:add")
    @AutoLog(value = "注册账户",logType = 2)
    @RequestMapping(value = "/userAdd", method = RequestMethod.POST)
    public Result<SysUser> userAdd(@RequestBody JSONObject jsonObject) throws Exception{
        Result<SysUser> result = new Result<SysUser>();
        String selectedRoles = jsonObject.getString("selectedRoles");
        if(oConvertUtils.isEmpty(selectedRoles)) throw new CustomException("No Roles selected");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        if(oConvertUtils.isEmpty(selectedDeparts)) throw new CustomException("No selectedDeparts selected");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            if(user.getName().isEmpty()){throw new Exception("Name is empty");}

            String hashAlgorithmName = "MD5";//加密方式
            Object crdentials = user.getPassword();//密码原值
            String salt = SaltUtils.getSalt(8);;//8位随机盐
            int hashIterations = 1;//加密1次
            Object newPasswd = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);

            user.setSalt(salt);
            user.setPassword(newPasswd.toString());
            user.setState(1);

            //user.setDelFlag(CommonConstant.DEL_FLAG_0);
            // 保存用户走一个service 保证事务
            sysUserService.saveUser(user,selectedRoles,selectedDeparts);
            result.success200("添加用户成功！");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.error500("添加用户失败: "+e.getMessage());
            //throw e; 此处throw  就会被全局@ExceptionHandler(Exception.class) 捕获
        }
        return result;
    }

    /**
     * 用户删除;
     * @return
     */
    @RequestMapping("/userDel")
    @RequiresPermissions("userInfo:del")//权限管理;
    public String userDel(){

        return "userInfoDel";
    }
}