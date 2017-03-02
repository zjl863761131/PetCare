package com.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.common.BaseDao;
import com.pet.common.BaseService;
import com.pet.dao.StoreuserDao;
import com.pet.entity.StoreUser;


@Service
public class StoreUserService extends BaseService<StoreUser>{
	
	/*@Override
	@Autowired
	@Qualifier("StoreuserService")
	public void setBaseDao(BaseDao<StoreUser> dao) {
		super.setBaseDao(dao);
	}
	*/
}
