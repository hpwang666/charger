package com.wwp;

import com.wwp.common.aspect.annotation.AutoLog;
import com.wwp.mapper.SysPermissionMapper;
import com.wwp.mapper.SysUserRoleMapper;
import com.wwp.model.TreeModel;
import com.wwp.sevice.ISysPermissionService;
import com.wwp.sevice.ISysUserRoleService;
import com.wwp.sevice.impl.SysLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages={"com.wwp.common.aspect.annotation"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes= MainApplication.class)
public class MainApplicationTests {

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ISysPermissionService sysPermissionService;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Test
    @AutoLog(value = "jojo")
    public void contextLoads() throws Exception{

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        List<TreeModel> permissions = sysPermissionService.queryListByParentId("1");
        if(permissions.size()>0){
            for(TreeModel p : permissions)
                System.out.println(p.getTitle());
        }
    }

}
