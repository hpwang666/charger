package com.wwp.common.exception;

import com.wwp.vo.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 * 
 * @Author scott
 * @Date 2019
 */
@RestControllerAdvice
public class CustomExceptionHandler {

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(CustomException.class)
	public Result<?> handleRRException(CustomException e){
		return Result.error(e.getMessage());
	}

	@ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
	public Result<?> handleAuthorizationException(AuthorizationException e){
		return   Result.error("认证错误"+e.getMessage());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public Result<?> handlerNoFoundException(Exception e) {
		return Result.error(404, "路径不存在"+e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public Result<?> handleException(Exception e){

		if (e instanceof java.lang.NullPointerException) {
			//GET method 404 Not Found
			return Result.error("操作失败鸟404，"+e.getMessage());
		} else {
			//500
			return Result.error("操作失败鸟500，"+e.getMessage());
		}
		//return Result.error("操作失败鸟，"+e.getMessage());
	}


	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		StringBuffer sb = new StringBuffer();
		sb.append("不支持");
		sb.append(e.getMethod());
		sb.append("请求方法，");
		sb.append("支持以下方法");
		String [] methods = e.getSupportedMethods();
		if(methods!=null){
			for(String str:methods){
				sb.append(str);
				sb.append("、");
			}
		}

		//return Result.error("没有权限，请联系管理员授权");
		return Result.error(405,sb.toString());
	}

}
