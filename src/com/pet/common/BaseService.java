package com.pet.common;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.pet.common.BaseDao;
import com.pet.common.InjectBaseDependencyHelper;
import com.pet.common.Page;

/**
 * 通用Service实现
 * 
 * @author zhangQ
 * create date:2013-6-19
 */
@Transactional
public abstract class BaseService<T> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BaseDao<T> baseDao;
	
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}


	public BaseService(){
	}

	@PostConstruct
	public void init(){
		InjectBaseDependencyHelper.findAndInjectBaseDaoDependency(this);
	}

	/**
	 * 新增对象.
	 */
	@Transactional
	public void save(final T entity) {
		/*
		Object uuidFile = ReflectionUtils.getDeclaredField(entity, "uuid");
		if(Validator.isNotNull(uuidFile)){
			Object fieldValue = ReflectionUtils.getFieldValue(entity, "uuid");
			if(Validator.isNull(fieldValue)){
				ReflectionUtils.invokeSetterMethod(entity, "uuid", Identities.uuid2());		//自动产生uuid
			}
		}
		*/
		baseDao.save(entity);
	}

	/**
	 * 保存新增或修改的对象.
	 * @param entity
	 */
	@Transactional
	public void saveOrUpdate(final T entity){
		baseDao.saveOrUpdate(entity);
	}

	/**
	 * 更新对象
	 */
	@Transactional
	public void update(final T entity){
		/*
		Object uuidFile = ReflectionUtils.getDeclaredField(entity, "uuid");
		if(Validator.isNotNull(uuidFile)){
			Object fieldValue = ReflectionUtils.getFieldValue(entity, "uuid");
			if(Validator.isNull(fieldValue)){
				ReflectionUtils.invokeSetterMethod(entity, "uuid", Identities.uuid2());		//自动产生uuid
			}
		}
		*/
		baseDao.update(entity);
	}

	/**
	 * 更新对象
	 * @Transactional
	 */
	public T merge(final T entity){
		return baseDao.merge(entity);
	}
	
	/**
	 * 删除对象.
	 * 
	 * @param entity 对象必须是session中的对象或含id属性的transient对象.
	 */
	@Transactional
	public void delete(final T entity) {
		baseDao.delete(entity);
	}

	/**
	 * 按id删除对象.
	 */
	@Transactional
	public void delete(final Serializable id) {
		baseDao.delete(id);
	}

	/**
	 * 按id获取对象.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public T get(final Serializable id) {
		return baseDao.get(id);
	}

	/**
	 * 获取全部对象.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<T> findAll() {
		return baseDao.findAll();
	}
	
	/**
	 * 统计所有
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public int countAll(){
		return baseDao.countAll();
	}
	
	/**
	 * 删除所有
	 */
	@Transactional
	public void removeAll(){
		baseDao.removeAll();
	}
	
	/**
	 * 按照hql批量更新
	 * @param hql
	 * @param objects
	 */
	@Transactional
	public void batchHandleByHQL(String hql,Object... objects){
		baseDao.batchHandleByHQL(hql, objects);
	}
	
	/**
	 * 分页查询所有，并且按照orderBy字段排序
	 * @param orderBy
	 * @param isAsc
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public Page<T> getScrollData(String orderBy, boolean isAsc,int pageIndex,int pageSize) {
		Page<T> page = new Page<T>(pageIndex,pageSize);
		page.setRecords(baseDao.getScrollData(orderBy, isAsc, page.getStart(), pageSize));
		page.setTotalRow(baseDao.countAll());
		return page;
	}
	
	/**
	 * 分页查询所有，并且按照orderBy封装的Map排序
	 * @param orderby
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public Page<T> getScrollData(LinkedHashMap<String, String> orderby,int pageIndex, int pageSize) {
		Page<T> page = new Page<T>(pageIndex,pageSize);
		page.setRecords(baseDao.getScrollData(orderby, page.getStart(), pageSize));
		page.setTotalRow(baseDao.countAll());
		return page;
	}

	/**
	 * 条件分页查询
	 * @param whereHql 不含"where"关键字的查询条件，如"name=? and age=?"
	 * @param queryParams 参数值对象
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public Page<T> getScrollData(String whereHql, Object[] queryParams,int pageIndex, int pageSize) {
		Page<T> page = new Page<T>(pageIndex,pageSize);
		page.setRecords(baseDao.getScrollData(whereHql, queryParams, page.getStart(), pageSize));
		page.setTotalRow(baseDao.countBy(whereHql, queryParams));
		return page;
	}
	
	/**
	 * 条件分页查询
	 * @param whereHql 不含"where"关键字的查询条件，如"name=? and age=?"
	 * @param pageIndex
	 * @param pageSize
	 * @param queryParams 参数值对象
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public Page<T> getScrollData(String whereHql,int pageIndex, int pageSize, Object... queryParams) {
		Page<T> page = new Page<T>(pageIndex,pageSize);
		page.setRecords(baseDao.getScrollData(whereHql, queryParams, page.getStart(), pageSize));
		page.setTotalRow(baseDao.countBy(whereHql, queryParams));
		return page;
	}

	/**
	 * 分页查询所有
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public Page<T> getScrollData(int pageIndex, int pageSize) {
		Page<T> page = new Page<T>(pageIndex,pageSize);
		page.setRecords(baseDao.getScrollData(page.getStart(), pageSize));
		page.setTotalRow(baseDao.countAll());
		return page;
	}

	/**
	 * 条件查询，并且分页返回结果
	 * @param whereHql 不含"where"关键字的查询条件，如"name=? and age=?"
	 * @param queryParams 参数值对象
	 * @param orderBy 排序map，如 new LinkedHashMap<String,String>().put("name","desc")
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public Page<T> getScrollData(String whereHql,Object[] queryParams,LinkedHashMap<String,String> orderBy ,int pageIndex,int pageSize){
		Page<T> page = new Page<T>(pageIndex,pageSize);
		page.setRecords(baseDao.getScrollData(whereHql, queryParams, orderBy, page.getStart(), pageSize));
		page.setTotalRow(baseDao.countBy(whereHql, queryParams));
		return page;
	}
	
	/**
	 * 按照条件统计
	 * @param whereHql
	 * @param queryParams
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public int countBy(String whereHql,Object[] queryParams){
		return baseDao.countBy(whereHql, queryParams);
	}
	
	/**
	 * 更加完整hql条件查询
	 * @param fullHQL
	 * @param queryParams
	 * @return
	 */
	public int countByFullHQL(String fullHQL,Object... queryParams){
		return baseDao.countByFullHQL(fullHQL, queryParams);
	}
	
	/**
	 * 按属性查找对象列表,匹配方式为相等.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<T> findBy(final String propertyName, final Object value) {
		return baseDao.findBy(propertyName, value);
	}

	/**
	 * 按属性查找唯一对象,匹配方式为相等.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public T findUniqueBy(final String propertyName, final Object value) {
		return baseDao.findUniqueBy(propertyName, value);
	}

	/**
	 * 按id列表获取对象.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<T> findByIds(List<? extends Serializable> ids) {
		return baseDao.findByIds(ids);
	}
	
	/**
	 * 按id列表获取对象.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<T> findByIds(Serializable[] ids) {
		List<Serializable> idList = Lists.newArrayList();
		if(ids!=null && ids.length>0){
			for (Serializable id : ids) {
				idList.add(id);
			}
		}
		return baseDao.findByIds(idList);
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public <X> List<X> find(final String hql, final Object... values) {
		return baseDao.find(hql, values);
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return baseDao.find(hql, values);
	}
	
	/**
	 * 按照FullHQL分页查询对象列表
	 * @param firstIndex 起始行的下标，第一个从0开始。注意：区别pageIndex 
	 * @param pageSize
	 * @param hql 完整的hql语句
	 * @param values
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public <X> List<X> find(int firstIndex,int pageSize,final String hql, final Object... values) {
		return baseDao.find(firstIndex, pageSize,hql,values);
	}

	/**
	 * 分页查询所有列表
	 * @param firstIndex 起始行的下标，第一个从0开始。注意：区别pageIndex 
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<T> findScrollData(int firstIndex,int pageSize) {
		return baseDao.getScrollData(firstIndex, pageSize);
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public <X> X findUnique(final String hql, final Object... values) {
		return baseDao.findUnique(hql, values);
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return baseDao.findUnique(hql, values);
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 */
	@Transactional
	public int batchExecute(final String hql, final Object... values) {
		return baseDao.batchExecute(hql, values);
	}
	
	/**
	 * 执行HQL进行批量修改/删除操作.
	 */
	@Transactional
	public int batchExecute(final Object[] values,final String hql) {
		return baseDao.batchExecute(hql, values);
	}
	
	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @return 更新记录数.
	 */
	@Transactional
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return baseDao.batchExecute(hql, values);
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * <p/>
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		return baseDao.createQuery(queryString, values);
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public List<T> find(final Criterion... criterions) {
		return baseDao.find(criterions);
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	@Transactional(readOnly=true,propagation=Propagation.REQUIRED)
	public T findUnique(final Criterion... criterions) {
		return baseDao.findUnique(criterions);
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 
	 * 只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,可实现新的函数,执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合. Hibernate.initialize
	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initEntity(T entity) {
		baseDao.initEntity(entity);
	}

	/**
	 * @see #initEntity(Object)
	 */
	public void initEntity(List<T> entityList) {
		baseDao.initEntity(entityList);
	}
}
