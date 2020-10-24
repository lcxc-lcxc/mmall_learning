package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iProductService.productSave(product);
        }else {
            return errorResponse;
        }
/*        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            //Service
            return iProductService.productSave(product);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }*/
    }
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else {
            return errorResponse;
        }

        /*        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            //Service
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }*/
    }

    /**
     * 获取产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Integer productId){

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iProductService.detail(productId);
        }else {
            return errorResponse;
        }


        /*        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录请登录");
        }
        if (iUserService.checkAdmin(user).isSuccess()){
            //Service
            return null;
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }*/
    }

    /**
     * 获得所有产品的List展示，并含分页操作
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session, iUserService)).isSuccess()) {
            return iProductService.getProductList(pageNum,pageSize);
        } else {
            return errorResponse;
        }
    }
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse search(HttpSession session, String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session, iUserService)).isSuccess()) {
            return iProductService.search(productName,productId,pageNum,pageSize);
        } else {
            return errorResponse;
        }
    }

    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file" ,required = false) MultipartFile file, HttpServletRequest request){

        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session, iUserService)).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");//该文件将会被创建在tomcat/webapps/ROOT文件夹下
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

            Map<String, String> fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return errorResponse;
        }
    }
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file" ,required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){

        //富文本simditor中对于返回值有自己的要求
        /*
        * {
        *   "success" : true/false,
        *   "msg" : "error message,#optional
        *   "file_path":"[real file path]"
        * }
        * */
        Map resultMap= Maps.newHashMap();
        if (BackendUtils.backendCheck(session, iUserService).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");//该文件将会被创建在tomcat/webapps/ROOT文件夹下
            String targetFileName = iFileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);

            //一个和前端的约定
            response.addHeader("Access-Controller-Allow-Headers","X-File-Name");

            return resultMap;
        } else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }


}
