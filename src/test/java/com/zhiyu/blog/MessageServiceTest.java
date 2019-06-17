package com.zhiyu.blog;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zhiyu.blog.service.MessageService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZYblogApplication.class)
public class MessageServiceTest {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private  HttpServletRequest request;
	
	@Test
	public void Test() throws Exception {
		
		messageService.saveMessage(1L, "测试", 1,1L,request);
		
	}

}
