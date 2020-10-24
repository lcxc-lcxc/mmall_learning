package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;

import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IUserService iUserService;

    /**
     * 增加节点
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session,String categoryName ,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return errorResponse;
        }

        /*        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAdmin(user).isSuccess()){//是管理员
            return iCategoryService.addCategory(categoryName,parentId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");*/

    }

    /**
     * 修改品类名字
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session,String categoryName , Integer categoryId){

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iCategoryService.setCategoryName(categoryName,categoryId);
        }else {
            return errorResponse;
        }




        /*        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserService.checkAdmin(user).isSuccess()){//是管理员

            return iCategoryService.setCategoryName(categoryName,categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");*/
    }


    /**
     * 获取品类子节点(平级)
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getChildParallelCategory(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId, HttpSession session){

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iCategoryService.getChildParallelCategory(categoryId);
        }else {
            return errorResponse;
        }


/*        User user = ((User)session.getAttribute(Const.CURRENT_USER));
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录");
        }
        //校验是否是管理员
        if(iUserService.checkAdmin(user).isSuccess()){//是管理员
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");*/
    }

/*
    */
/**
     * 获取当前分类id及递归子节点categoryId
     * @param session
     * @param categoryId
     * @return
     *//*

    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    public ServerResponse<List<Integer>> getDeepCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){


        User user = ((User)session.getAttribute(Const.CURRENT_USER));
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录");
        }

        //校验是否是管理员
        if(iUserService.checkAdmin(user).isSuccess()){//是管理员
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getDeepCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");

    }
*/

    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId, HttpSession session){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        }else {
            return errorResponse;
        }

/*        User user = ((User)session.getAttribute(Const.CURRENT_USER));
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录");
        }
        //校验是否是管理员
        if(iUserService.checkAdmin(user).isSuccess()){//是管理员
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");*/
    }

}
