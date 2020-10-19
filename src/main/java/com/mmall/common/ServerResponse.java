package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//这个表示如果key-value中的value是null的话，则这个key-value会被删除
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

//    构造器
    private ServerResponse(int status){
        this.status = status;
    }


    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }

//    这里可能存在问题，比如和上一个构造函数相比，假设调用的是ServerResponse(1."abc");它执行哪个构造函数呢？
//    实验表明，它会执行ServerResponse(int status,String msg)，而不是private ServerResponse(int status,T data)
//    但是加入我需要的T就是String，就是想调用ServerResponse(int status,T data)，怎么办呢？
//    下面我们使用后工厂模式，想怎么创建，就调用不同的工厂方法即可
    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //使之不进行序列化为jason
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode() ;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

//Success
    //工厂模式，产生所需的ServerResponse对象
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

//Error
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String ErrorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ErrorMessage);
    }

//Other Errors
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }




}
