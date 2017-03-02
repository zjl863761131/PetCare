package com.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.common.BaseDao;
import com.pet.common.BaseService;
import com.pet.dao.UserDao;
import com.pet.entity.StoreUser;
import com.pet.entity.SysUser;

@Service
public class UserService extends BaseService<SysUser>{
 
  
  /* @Autowired 
  @Transactional
  public void save(SysUser u)
  {
	  dao.save(u);
	  System.out.println("============");
  }*/
	
	
}
