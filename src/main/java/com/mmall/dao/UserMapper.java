package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    //传递多个参数，需要@Param注解
    User selectLogin(@Param("username") String username, @Param("password") String password);

    int checkEmail(String email);

    String forgetGetQuestion(String username);


    int forgetCheckAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int forgetResetPassword(@Param("username") String username,@Param("passwordNew") String passwordNew);

    int checkPassword(@Param("password") String password,@Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);

}