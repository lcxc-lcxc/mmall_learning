package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }


        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //response返回不再需要password，所以需要置空
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功",user);


    }

    @Override
    public ServerResponse<String> register(User user){
/*        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0){//用户名已存在
            return ServerResponse.createByErrorMessage("用户名已存在");
        }*/
//      复用下面checkValid的代码
        ServerResponse validresponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validresponse.isSuccess()){
            return validresponse;
        }

/*        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0){
            return ServerResponse.createByErrorMessage("email已存在");
        }*/
//      复用下面checkValid的代码
        validresponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validresponse.isSuccess()){
            return validresponse;
        }


        user.setRole(Const.Role.ROLE_CUSTOMER);

        //密码操作MD5
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);

        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");

    }

    //这个是为了前端校验用户名是否合格，比如那种一移出当前框就开始校验，然后弹出一个交叉或勾说明当前是无效或有效
    public ServerResponse<String> checkValid(String str, String type){
        if (StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0){//用户名已存在
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> forgetGetQuestion(String username){

        //必须先校验一下用户名是否存在
        int resultCoun = userMapper.checkUsername(username);
        if (resultCoun == 0){//用户名不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String question = userMapper.forgetGetQuestion(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
    }

    public ServerResponse<String> forgetCheckAnswer(String username,String question ,String answer){
        int resultCount = userMapper.forgetCheckAnswer(username,question,answer);
        if (resultCount > 0){
            //密保问题正确
            String forgetToken = UUID.randomUUID().toString();

            //把token放进本地缓存中,因为我们需要为token设置一定的有效期
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("密保问题错误");


    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }

        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        //校验token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if (StringUtils.equals(token,forgetToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.forgetResetPassword(username,md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else {

            return ServerResponse.createByErrorMessage("token错误,请重新重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");


    }

    @Override
    public ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew) {
        if (StringUtils.isBlank(passwordOld)){
            return ServerResponse.createByErrorMessage("原密码为空,请输入原密码");
        }
        if (StringUtils.isBlank(passwordNew)){
            return ServerResponse.createByErrorMessage("新密码为空,请输入新密码");
        }
        String md5PasswordOld = MD5Util.MD5EncodeUtf8(passwordOld);
        if (userMapper.checkPassword(md5PasswordOld,user.getId())==0) {
            //原密码输入错误
            return ServerResponse.createByErrorMessage("原密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

/*
    @Override
    public ServerResponse<String> updateInformation(User currentUser, User user) {
        //防止横向越权
        if (userMapper.checkPassword(currentUser.getPassword(),currentUser.getId()) == 0){
            return ServerResponse.createByErrorMessage("用户账号异常,请重新登录");
        }

        user.setId(currentUser.getId());
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount>0){
            return ServerResponse.createBySuccessMessage("信息更新成功");
        }
        return ServerResponse.createByErrorMessage("信息更新失败");

    }
*/
    public ServerResponse<User> updateInformation(User user){
        //username不能更新
        //email要进行校验,校验新的email是不是已经存在,并且存在的Email如果相同的话,不能是我们这个用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount>0){
            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
        //TODO - 这里新设一个User,注意,这个updateUser里面的username字段为null,这样往数据库更新时,usermane就不会被改动
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0){
            //由于updateUser没有username字段,到时返回为session的Attribute不合适,所以只能返回user
            return ServerResponse.createBySuccess("更新个人信息成功",user);
        }

        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse<String> checkAdmin(User user){
        if (user!=null && user.getRole().intValue() == Const.Role.ROLE_MANAGER){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
