package com.test.service.impl;

import com.test.domain.User;
import com.test.domain.UserExample;
import com.test.mapper.UserMapper;
import com.test.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.test.utils.DESUtil.ENCRYPTMethod;
import static com.test.utils.DESUtil.decrypt;

/**
 * @author P1n93r
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    private static String key;
    static {
        String path = Objects.requireNonNull(UserServiceImpl.class.getClassLoader().getResource("api/key.properties")).getPath();
        Properties properties=new Properties();
        try (FileInputStream inputStream=new FileInputStream(path);){
            properties.load(inputStream);
            key = properties.getProperty("des.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加一个自动打卡的user
     *
     * @return 数据影响行数
     * @throws Exception 数据库异常
     */
    @Override
    public int addUser(User user) throws Exception {
        //先查询是否存在了
        UserExample userExample = new UserExample();
        userExample.or().andNumEqualTo(user.getNum());
        List<User> users = userMapper.selectByExample(userExample);
        //如果已存在了，就不能插入
        if(users!=null&&users.size()>0){
            return 0;
        }
        user.setPwd(ENCRYPTMethod(user.getPwd(), key).toUpperCase());
        return userMapper.insertSelective(user);
    }

    /**
     * 删除user记录
     *
     * @param user 学生信息
     * @return 影响行数
     * @throws Exception mysql异常
     */
    @Override
    public int deleteUser(User user) throws Exception {
        user.setPwd(ENCRYPTMethod(user.getPwd(), key).toUpperCase());
        UserExample userExample = new UserExample();
        userExample.or().andNumEqualTo(user.getNum()).andPwdEqualTo(user.getPwd());
        return userMapper.deleteByExample(userExample);
    }

    /**
     * 查询所有的user
     */
    @Override
    public List<User> findAllUser() throws Exception {
        UserExample userExample = new UserExample();
        userExample.or();
        List<User> allUser = userMapper.selectByExample(userExample);
        for(User v:allUser){
            v.setPwd(decrypt(v.getPwd(),key));
        }
        return allUser;
    }

    /**
     * 更新User
     *
     * @param user
     */
    @Override
    public int updateUserWid(User user) throws Exception {
        User temp = new User();
        temp.setUid(user.getUid());
        temp.setLastCollectorWid(user.getLastCollectorWid());
        return userMapper.updateByPrimaryKeySelective(temp);
    }


}
