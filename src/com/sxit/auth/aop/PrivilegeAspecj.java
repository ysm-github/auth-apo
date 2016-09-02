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
 * ����ϵͳȨ������֪ͨ
 * 
 * @author ysm
 * @date 2014-5-20 ����01:55:04
 */
@Aspect
@Component
public class PrivilegeAspecj {
	
	protected static Logger logger = Logger.getLogger(PrivilegeAspecj.class);
	/**
	 * ����service����
	 */
	@Pointcut("execution(public * com.sxit.*.*.service.impl.*.*(..))")
	public void serviceName() {
	}

	/**
	 * ���廷��֪ͨ
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
	 * ��֤�û�Ȩ��
	 * @param pjp
	 * true  Ȩ����֤�ɹ�
	 * false Ȩ����֤ʧ��
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
	 * ��ȡ��ǰsession�û���Ϣ
	 * @return
	 */
	private CrmUser getCrmUser(){
		CrmUser memInfo = null;//RequestUtil.getSessionAttr("user");
		return memInfo;
	}
	/**
	 * ��ȡserviceִ�з�����ע������ѡ��
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
