package com.test.test;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.test.domain.User;
import com.test.mapper.UserMapper;
import com.test.service.UserService;
import com.test.task.DailySchoolTask;
import com.test.utils.EmailUtils;
import com.test.utils.TodayAppUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 基于Spring环境的单元测试
 * @author P1n93r
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "classpath:spring/*.xml"
})
public class TestInSpring {
    @Resource
    UserService userService;



    @Test
    public void testMapper() {


    }

    @Test
    public void testEx(){



    }




}
