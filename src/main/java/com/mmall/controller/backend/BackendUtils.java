package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

import javax.servlet.http.HttpSession;

public class BackendUtils {

    /**
     * 用于后台的校验（校验是否登录，是否是管理员）
     * @param session
     * @param iUserService
     * @return
     */
    protected static ServerResponse backendCheck(HttpSession session, IUserService iUserService){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            //Service
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }
}
