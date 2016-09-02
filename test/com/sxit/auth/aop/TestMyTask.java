package com.sxit.auth.aop;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sxit.crm.common.bean.system.CrmRole;
import com.sxit.web.authManagement.service.AuthManagementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/applicationContext-*.xml")
public class TestMyTask extends AbstractJUnit4SpringContextTests {
	
	private static Logger log = Logger.getLogger(TestMyTask.class);
	
	@Inject
	private AuthManagementService myTask;
	
	@Test
	public void startTask(){
		log.info("=============startTask");
		List<CrmRole> list = myTask.getAuthoritiesByUsername("010-65331662");
		assertThat(list.size()>0,is(true));
	}
	
	@Test
	public void endTask(){
		log.info("=============endTask");
		List<CrmRole> list = myTask.getAllAuthoritys();
		assertThat(list.size()>0,is(true));
	}
	
}
