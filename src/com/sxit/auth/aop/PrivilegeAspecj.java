package com.sxit.auth.aop;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sxit.crm.common.bean.system.CrmUser;

/**
 * 定义系统权限切面通知
 * 
 * @author ysm
 * @date 2014-5-20 下午01:55:04
 */
@Aspect
@Component
public class PrivilegeAspecj {
	
	protected static Logger logger = Logger.getLogger(PrivilegeAspecj.class);
	/**
	 * 定义service切面
	 */
	@Pointcut("execution(public * com.sxit.*.*.service.impl.*.*(..))")
	public void serviceName() {
	}

	/**
	 * 定义环绕通知
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("serviceName()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		logger.info("starting proxy................");
		if(!validate(pjp)){
			  throw new PrivilegeException("Without permission 999, operation is prohibited!");
		}
		return pjp.proceed();
	}
	/**
	 * 验证用户权限
	 * @param pjp
	 * true  权限验证成功
	 * false 权限验证失败
	 * @return
	 */
	private boolean validate(ProceedingJoinPoint pjp){
		String PrivilegeCode=this.getPrivilege(pjp);
		 if(PrivilegeCode==null){
			   return true;
			  }
		 
		 if(PrivilegeCode !=null && PrivilegeCode.equals("")){
			   return true;
		 }
		 
		 CrmUser memInfo=this.getCrmUser();
		 for(String code:memInfo.getPrivilegeGroups()){
			 if(PrivilegeCode.equals(code)){
				 return true;
			 }
		 }
		 return false;
		 
	}
	/**
	 * 获取当前session用户信息
	 * @return
	 */
	private CrmUser getCrmUser(){
		CrmUser memInfo = null;//RequestUtil.getSessionAttr("user");
		return memInfo;
	}
	/**
	 * 获取service执行方法的注解配置选项
	 * @param pjp
	 * @return
	 */
	private String getPrivilege(ProceedingJoinPoint pjp){
		MethodSignature joinPointObject=(MethodSignature) pjp.getSignature();
		  Method method = joinPointObject.getMethod();
		  Permission permi=method.getAnnotation(Permission.class);
		  if(permi==null){
		   return null;
		  }
		  return permi.value();
	}
}
