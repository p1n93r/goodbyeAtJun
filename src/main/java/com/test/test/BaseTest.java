package com.test.test;


import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.test.domain.User;
import com.test.utils.DESUtil;
import com.test.utils.TodayAppUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * 基于基础java环境的单元测试
 * @author P1n93r
 */
public class BaseTest {



    @Test
    public void testOther() throws Exception {


    }



    public static void main(String[] a) throws HttpProcessException {
        User user = new User();
        user.setNum("20174041");
        user.setPwd("088454cg_CG");
        Boolean aa = TodayAppUtils.isUserUseful(user);
        user.setPwd("088454cg_CG");
        Boolean b = TodayAppUtils.isUserUseful(user);
        user.setPwd("09123");
        Boolean c = TodayAppUtils.isUserUseful(user);
        System.out.println(aa);
        System.out.println(b);
        System.out.println(c);
    }



}
