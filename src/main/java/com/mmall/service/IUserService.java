package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {



    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    public ServerResponse<String> checkValid(String str, String type);

    public ServerResponse<String> forgetGetQuestion(String username);

    public ServerResponse<String> forgetCheckAnswer(String username,String question ,String answer);

    public ServerResponse<String> forgetResetPassword(String username,String passwordNew ,String forgetToken);

    ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew);

    ServerResponse<User> updateInformation(User user);


    ServerResponse<User> getInformation(Integer id);
}
