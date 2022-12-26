package com.wwp.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wwp.common.constant.CommonConstant;

//import com.neo.common.constant.CommonConstant;

import java.io.Serializable;



public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */

	private boolean success = true;

	/**
	 * 返回处理消息
	 */

	private String message = "操作成功！";

	/**
	 * 返回代码
	 */

	private Integer code = 0;

	/**
	 * 返回数据对象 data
	 */

	private T result;

	/**
	 * 时间戳
	 */



	public Result() {

	}

	public void setCode(Integer code) {
		this.code = code;
	}
	public void setSuccess(boolean b){
		this.success=b;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setResult(T result) {this.result = result;	}
	public Integer getCode() {	return code;}
	public String getMessage()	{	return message;	}
	public T getResult(){	return result;	}
	public boolean getSuccess(){return success;}




	public static<T> Result<T> OK() {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage("成功");
		return r;
	}

	public static<T> Result<T> OK(T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> OK(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setCode(CommonConstant.SC_OK_200);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}



	public static Result<Object> error(String msg) {
		return error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
	}

	public static <T>Result<T> error(int code, String msg) {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public static<T> Result<T> error(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(false);
		r.setCode(CommonConstant.SC_INTERNAL_SERVER_ERROR_500);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}
	/**
	 * 无权限访问返回结果
	 */
	public static Result<Object> noauth(String msg) {
		return error(CommonConstant.SC_JEECG_NO_AUTHZ, msg);
	}

	public Result<T> error500(String message) {
		this.message = message;
		this.code = CommonConstant.SC_INTERNAL_SERVER_ERROR_500;
		this.success = false;
		return this;
	}

	public Result<T> success200(String message) {
		this.message = message;
		this.code = CommonConstant.SC_OK_200;
		this.success = true;
		return this;
	}

	@JsonIgnore
	private String onlTable;

}