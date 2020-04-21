package com.test.service;

import com.test.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author P1n93r
 */
@Service
public interface UserService {

    /**
     * 添加一个自动打卡的user
     * @return 数据影响行数
     * @throws Exception 数据库异常
     */
    public int addUser(User user) throws Exception;


    /**
     * 删除user记录
     * @param user 学生信息
     * @return 影响行数
     * @throws Exception mysql异常
     */
    public int deleteUser(User user) throws Exception;


    /**
     * 查询所有的user
     */
    public List<User> findAllUser()throws Exception;


    /**
     * 更新User的wid
     */
    public int updateUserWid(User user)throws Exception;



}
