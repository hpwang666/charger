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

//@ComponentScan(basePackages={"com.wwp.common.aspect.annotation"})
@RunWith(SpringRunner.class)
//@SpringBootTest(classes= MainApplication.class)
public class MainApplicationTests {

    @Test
    public void testFeeModel() throws Exception
    {
        String fee="000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        if(fee.length()!=96) throw new ParseException("字段错误");

        S
    }

}
