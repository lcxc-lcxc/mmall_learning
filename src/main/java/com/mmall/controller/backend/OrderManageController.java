package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize" ,defaultValue = "10") int pageSize){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iOrderService.manageList(pageNum, pageSize);
        }else {
            return errorResponse;
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> detail(HttpSession session, Long orderNo){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iOrderService.manageDetail(orderNo);
        }else {
            return errorResponse;
        }
    }


    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> search(HttpSession session, Long orderNo,
                                          @RequestParam(value = "pageNum" ,defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize" ,defaultValue = "10") int pageSize){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iOrderService.manageSearch(orderNo, pageNum, pageSize);
        }else {
            return errorResponse;
        }
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo){
        ServerResponse errorResponse;
        if ((errorResponse = BackendUtils.backendCheck(session,iUserService)).isSuccess()){
            return iOrderService.manageSendGoods(orderNo);
        }else {
            return errorResponse;
        }
    }
}
