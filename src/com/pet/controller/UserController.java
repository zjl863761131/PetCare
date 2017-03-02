package com.pet.controller;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pet.annotation.PartialView;
import com.pet.dao.UserDao;
import com.pet.entity.StorePic;
import com.pet.entity.StoreUser;
import com.pet.entity.SysUser;
import com.pet.service.StoreUserService;
import com.pet.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService service;
	
	StoreUserService storeUserService;
	@RequestMapping("/toLogin")
	public String tologin()
	{
		return "front/login";
	}
	
	@RequestMapping("/to")
	public String to()
	{
		return "front/login";
	}
	
	@RequestMapping("/toUserPic/{UserId}")
	@PartialView
	public String toUserPic(Model model,@PathVariable int id)
	{
		
		StoreUser storeUser = storeUserService.get(id);
		model.addAttribute("CurrentStoreUser", storeUser);
        Set<StorePic> storePic = storeUser.getStorePic();
        ArrayList<StorePic> storePictList = new ArrayList<>(storePic);
        model.addAttribute("BookComments", storePictList);
		return "front/login";
	}
	
	
	
	
	@RequestMapping("/login")
	public String login(Model m,SysUser u)
	{
		service.save(u);
		m.addAttribute("u", u);
		return "front/success";
	}
}
