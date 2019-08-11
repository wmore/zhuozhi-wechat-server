package net.joywise.wechat.server.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @Description: 返回类
 * @Copyright: 福建卓智网络科技有限公司 (c)2016
 * @Created Date : 2016年6月13日
 * @author longweier
 * @vesion 1.0
 */
public class ServiceResult<T> implements Serializable{
	
	private static final long serialVersionUID = 4730576638927219990L;
	
	private boolean success;
    
	@JsonIgnore
	private Integer statusCode = HttpServletResponse.SC_OK;
	
	private String message;
	
	private Map<String,Object> property;

	private T data;
	
	public ServiceResult(){
		this.success = true;
	}
	
	public ServiceResult(boolean success){
		this.success = success;
	}
	
	public ServiceResult(boolean success, String message){
		this.success = success;
		this.message = message;
	}
	
	public ServiceResult(boolean success, String message, Map<String,Object> property){
		this.success = success;
		this.message = message;
		this.property = property;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getProperty() {
		return property;
	}

	public void setProperty(Map<String, Object> property) {
		this.property = property;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
