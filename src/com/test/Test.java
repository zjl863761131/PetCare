package com.test;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import com.pet.entity.StoreUser;
import com.pet.service.StoreUserService;



public class Test extends BaseTest {
	@Autowired
	private StoreUserService userService;
	
	@org.junit.Test
	public void save()
	{
		StoreUser user=new StoreUser();
		user.setStUsername("666");
		user.setStPwd("666");
		user.setStPhone("666");
		user.setStLicense("img");
		user.setStEmail("666@666");
		user.setStDescribe("666");
		user.setStAddress("666");
	
		userService.save(user);
	}
	
	
	
}
