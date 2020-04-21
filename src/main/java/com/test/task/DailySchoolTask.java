package com.test.task;


import com.test.domain.User;
import com.test.service.UserService;
import com.test.service.impl.UserServiceImpl;
import com.test.utils.EmailUtils;
import com.test.utils.TodayAppUtils;
import java.util.List;

/**
 * @author P1n93r
 * 今日校园自动打卡任务
 * 注意：此类不要使用注解方式，防止Task被实例化多次，会导致Task被多次执行
 */
public class DailySchoolTask {

    UserService userService;


    /**
     * 执行打卡任务
     */
    public void doTask() {
        //每天的15点钟执行今日校园打卡
        //这里考虑可能没啥人用，就先单线程执行数据库中所有user的打卡任务了
        //后期如果人数比较多，需要改成多线程执行打卡任务

        List<User> allUser = null;
        try {
            allUser = userService.findAllUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(allUser!=null&&allUser.size()>0){
            for(User v:allUser){
                //调用工具类
                try {
                    TodayAppUtils currentIns = TodayAppUtils.getInstance(v);
                    Boolean isSuccess = currentIns.submit();
                    if(isSuccess){
                        //更新当前用户的wid
                        userService.updateUserWid(v);
                    }
                } catch (Exception e) {
                    //如果发生了异常
                    EmailUtils.sendEmail(v.getEmail(),"再见吧！@艾特君~","程序异常："+e.getMessage());
                }
            }
        }
    }


    /**
     * 给予管理员提前的提示
     */
    public void sendTip() {
        EmailUtils.sendEmail("1725367974@qq.com","再见吧！@艾特君~","快要打卡了嗷~请注意~");

    }


    /**
     * @param userService user的业务类
     */
    public void setUserService(UserService userService) {
        this.userService=userService;
    }





}
