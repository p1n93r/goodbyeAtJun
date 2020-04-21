package com.test.web.controller;


import com.test.domain.User;
import com.test.domain.ValidationRes;
import com.test.service.UserService;
import com.test.utils.TodayAppUtils;
import com.test.utils.ValidateUtil;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author P1n93r
 * 用于与前台数据交互的控制器类
 */
@RestController
public class UserController {

    @Resource
    UserService userService;


    @RequestMapping(value = "/addUser",produces = "application/json;charset=utf-8")
    public String addUser(@RequestBody User user) throws Exception {
        JSONObject res = new JSONObject();
        res.put("result",false);
        res.put("msg","已存在此学号的记录！");
        ValidationRes validationRes = ValidateUtil.validateEntity(user);
        if(validationRes.isHasErrors()){
            res.put("msg",validationRes.getMessage().trim());
            //验证不通过
            return res.toString();
        }
        //先验证一下是否能登录
        Boolean userUseful = TodayAppUtils.isUserUseful(user);
        if(userUseful){
            int i = userService.addUser(user);
            if(i>0){
                res.put("result",true);
                res.put("msg","成功！系统将在每天下午3点为你自动打卡！如果题目变化了，将暂停打卡并给你发送邮件提醒~");
            }
        }else{
            res.put("msg","此账号无法登录，请确认是否正确，或者稍后再试试~");
        }
        return res.toString();
    }


    @RequestMapping(value = "/delUser",produces = "application/json;charset=utf-8")
    public String delUser(@RequestBody User user) throws Exception {
        JSONObject res = new JSONObject();
        res.put("result",false);
        res.put("msg","请不要乱取消别人的嗷~");
        ValidationRes numRes = ValidateUtil.validateProperty(user,"num");
        ValidationRes pwdRes = ValidateUtil.validateProperty(user,"pwd");
        if(numRes.isHasErrors()||pwdRes.isHasErrors()){
            res.put("msg",(numRes.getMessage()+pwdRes.getMessage()).trim());
            //验证不通过
            return res.toString();
        }
        int i = userService.deleteUser(user);
        if(i>0){
            res.put("result",true);
            res.put("msg","成功！你已取消本系统下的自动打卡~");
        }
        return res.toString();
    }



}
