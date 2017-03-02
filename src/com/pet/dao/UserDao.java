package com.pet.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pet.common.BaseDao;
import com.pet.entity.SysUser;

@Repository
public class UserDao extends BaseDao<SysUser>{
  /* @Autowired
   SessionFactory sf;
   
   public void save(SysUser u)
   {
	   System.out.println(u.getUsername()+"========"+u.getPwd());
	   sf.getCurrentSession().save(u);
   }*/
	
	
	
}
