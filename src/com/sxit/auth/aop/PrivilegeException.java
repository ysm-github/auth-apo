package com.sxit.auth.aop;

/**
 * 系统权限控制异常类
 * @author ysm
 * @date 2014-5-20 下午02:13:15
 */
public class PrivilegeException extends RuntimeException{

	private static final long serialVersionUID = -983510418352680739L;

	public PrivilegeException() {
		super();
	}

	public PrivilegeException(String msg) {
		super(msg);
	}

	public PrivilegeException(Throwable e) {
		super(e);
	}

	public PrivilegeException(String msg, Throwable e) {
		super(msg, e);
	}
}
