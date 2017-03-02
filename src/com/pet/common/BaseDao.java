package com.pet.common;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.alibaba.druid.filter.AutoLoad;
import com.pet.utils.GetterUtil;
import com.pet.utils.ReflectionUtils;
import com.pet.utils.Validator;


/**
 * 通用数据持久层
 * 
 * @author zhangQ 
 * Date: 2013-6-3 20:29
 * 
 */

public abstract class BaseDao<T> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected SessionFactory sessionFactory;
	protected Class<T> clazz;

	/**
	 * 用于Dao层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class. 
	 * eg. public class UserDao extends BaseDao<User, Long>
	 */
	public BaseDao() {
		this.clazz = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 用于用于省略Dao层, 在Service层直接使用通用BaseDao的构造函数. 在构造函数中定义对象类型Class. 
	 * eg. HibernateDao<User, Long> userDao = new BaseDao<User,
	 * Long>(sessionFactory, User.class);
	 */
	public BaseDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.clazz = entityClass;
	}

	/**
	 * 取得sessionFactory.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 采用@Autowired按类型注入SessionFactory, 当有多个SesionFactory的时候Override本函数.
	 */
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 取得当前Session.
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 新增对象.
	 */
	public void save(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().save(entity);
		logger.debug("save entity: {}", entity);
	}
	
	/**
	 * 保存新增或修改的对象.
	 * @param entity
	 */
	public void saveOrUpdate(final T entity){
		Assert.notNull(entity,"entity不能为空");
		getSession().saveOrUpdate(entity);
		logger.debug("saveOrUpdate entity: {}", entity);
	}
	
	/**
	 * 更新对象
	 */
	public void update(final T entity){
		Assert.notNull(entity,"entity不能为空");
		getSession().update(entity);
		logger.debug("update entity: {}", entity);
	}
	
	/**
	 * 更新对象
	 */
	@SuppressWarnings("unchecked")
	public T merge(final T entity){
		Assert.notNull(entity,"entity不能为空");
		logger.debug("merge entity: {}", entity);
		return (T) getSession().merge(entity);
	}
	
	/**
	 * 删除对象.
	 * 
	 * @param entity 对象必须是session中的对象或含id属性的transient对象.
	 */
	public void delete(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	public void delete(final Serializable id) {
		Assert.notNull(id, "id不能为空");
		delete(get(id));
		logger.debug("delete entity {},id is {}", clazz.getSimpleName(), id);
	}

	/**
	 * 按id获取对象.
	 */
	@SuppressWarnings("unchecked")
	public T get(final Serializable id) {
		Assert.notNull(id, "id不能为空");
		return (T) getSession().get(clazz, id);
	}

	/**
	 * 获取全部对象.
	 */
	public List<T> findAll() {
		return find();
	}
	
	/**
	 * 统计所有
	 * @return
	 */
	public int countAll(){
		return GetterUtil.getInteger(getSession().createQuery("select count("+getIdName()+") from "+clazz.getSimpleName()).setCacheable(true).uniqueResult());
	}
	
	
	/**
	 * 删除所有
	 */
	public void removeAll(){
		int num= getSession().createQuery("delete from "+clazz.getSimpleName()).executeUpdate();
		logger.debug("delete entity {},total {}", clazz.getSimpleName(), num);
	}
	
	/**
	 * 按照hql批量更新
	 * @param hql
	 * @param objects
	 */
	public void batchHandleByHQL(String hql,Object... objects){
		Query q = getSession().createQuery(hql);
		if(Validator.isNotNull(objects)){
			for(int i=0; i<objects.length; i++){
				q.setParameter(i, objects[i]);
			}
		}
		q.executeUpdate();
	}
	
	/**
	 * 分页查询所有，并且按照orderBy字段排序
	 * @param orderBy
	 * @param isAsc
	 * @param firstIndex
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getScrollData(String orderBy, boolean isAsc,int firstIndex,int maxResult) {
		Criteria c = createCriteria();
		if (isAsc) {
			c.addOrder(Order.asc(orderBy));
		} else {
			c.addOrder(Order.desc(orderBy));
		}
		
		if(firstIndex!=-1 && maxResult!=-1){
			c.setFirstResult(firstIndex).setMaxResults(maxResult);
		}
		return c.list();
	}
	
	/**
	 * 分页排序查询
	 * @param orderby
	 * @param firstIndex
	 * @param maxResult
	 * @return
	 */
	public List<T> getScrollData(LinkedHashMap<String, String> orderby,int firstIndex, int maxResult) {
		return getScrollData(null, null, orderby,firstIndex, maxResult);
	}

	/**
	 * 条件分页查询
	 * @param whereHql 不含"where"关键字的查询条件，如"name=? and age=?"
	 * @param queryParams 参数值对象
	 * @param firstIndex
	 * @param maxResult
	 * @return
	 */
	public List<T> getScrollData(String whereHql, Object[] queryParams,int firstIndex, int maxResult) {
		return getScrollData(whereHql, queryParams, null,firstIndex, maxResult);
	}

	/**
	 * 分页查询所有
	 * @param firstIndex
	 * @param maxResult
	 * @return
	 */
	public List<T> getScrollData(int firstIndex, int maxResult) {
		return getScrollData(null, null, null,firstIndex, maxResult);
	}
	
	/**
	 * 根据完整的HQL语句分页查询
	 * @param fullHQL
	 * @param queryParams
	 * @param firstIndex
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getScrollDataByFulHQL(String fullHQL,Object[] queryParams,int firstIndex,int maxResult){
		Query query = this.createQuery(fullHQL, queryParams);
		if(firstIndex!=-1 && maxResult!=-1){
			query.setFirstResult(firstIndex).setMaxResults(maxResult);
		}
		return query.list();
	}

	/**
	 * 条件查询，并且分页返回结果
	 * @param whereHql 不含"where"关键字的查询条件，如"name=? and age=?"
	 * @param queryParams 参数值对象
	 * @param orderBy 排序map，如 new LinkedHashMap<String,String>().put("name","desc")
	 * @param firstIndex
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getScrollData(String whereHql,Object[] queryParams,LinkedHashMap<String,String> orderBy ,int firstIndex,int maxResult){
		StringBuilder where = new StringBuilder();
		where.append("from "+clazz.getSimpleName()+" o ");
		where.append(whereHql==null || "".equals(whereHql.trim()) ? " ":"where " + whereHql);
		where.append(" "+ buildOrderBy(orderBy));
		
		Query query = this.createQuery(where.toString(), queryParams);
		if(firstIndex!=-1 && maxResult!=-1){
			query.setFirstResult(firstIndex).setMaxResults(maxResult);
		}
		return query.list();
	}
	
	/**
	 * 构造order by语句
	 * @param orderBy
	 * @return
	 */
	protected String buildOrderBy(LinkedHashMap<String,String> orderBy){
		StringBuilder orderbysql = new StringBuilder();
		if(orderBy!=null && !orderBy.isEmpty()){
			orderbysql.append(" order by ");
			for(String key : orderBy.keySet()){
				orderbysql.append("o.").append(key).append(" ").append(orderBy.get(key)).append(",");
			}
			orderbysql.deleteCharAt(orderbysql.length()-1);
		}
		return orderbysql.toString();
	}
	
	/**
	 * 按照条件统计
	 * @param whereHql
	 * @param queryParams
	 * @return
	 */
	public int countBy(String whereHql,Object[] queryParams){
		StringBuilder where = new StringBuilder();
		where.append("select count("+getIdName()+") from "+clazz.getSimpleName()+" o ");
		where.append(whereHql==null || "".equals(whereHql.trim()) ? " ":"where " + whereHql);
		
		Query query = this.createQuery(where.toString(), queryParams);
		return GetterUtil.getInteger(query.uniqueResult());
	}
	
	/**
	 * 更加完整hql条件查询
	 * @param fullHQL
	 * @param queryParams
	 * @return
	 */
	public int countByFullHQL(String fullHQL,Object... queryParams){
		Query query = this.createQuery(fullHQL, queryParams);
		return GetterUtil.getInteger(query.uniqueResult());
	}
	
	/**
	 * 按属性查找对象列表,匹配方式为相等
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<T> findBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(criterion);
	}

	/**
	 * 按属性查找唯一对象,匹配方式为相等.
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).setCacheable(true).setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	/**
	 * 按id列表获取对象.
	 * @param ids
	 * @return
	 */
	public List<T> findByIds(List<? extends Serializable> ids) {
		if(ids==null || ids.isEmpty()){
			return null;
		}
		return find(Restrictions.in(getIdName(), ids));
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}
	
	/**
	 * 按照FullHQL分页查询对象列表
	 * @param firstIndex 起始行的下标，第一个从0开始。注意：区别pageIndex 
	 * @param pageSize
	 * @param hql 完整的hql语句
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> find(int firstIndex,int pageSize,final String hql,final Object... values) {
		return createQuery(hql, values).setFirstResult(firstIndex).setMaxResults(pageSize).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * @param hql
	 * @param values
	 * @return
	 */
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}
	
	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param hql
	 * @param values
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * <p/>
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString).setCacheable(true);
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString).setCacheable(true);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(final Criterion... criterions) {
		return createCriteria(criterions).list();
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	@SuppressWarnings("unchecked")
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria.
	 * <p/>
	 * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(clazz).setCacheable(true);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
	
	/**
	 * 执行sql查询
	 * @param sql
	 * @return
	 */
	public SQLQuery queryBySQL(String sql) {
		SQLQuery query=getSession().createSQLQuery(sql);
		return query;
	}
	
	/**
	 * 按照sql分页查询对象列表
	 * @param firstIndex 起始行的下标，第一个从0开始。注意：区别pageIndex 
	 * @param pageSize
	 * @param sql 完整的sql语句
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> queryBySQL(int firstIndex,int pageSize,final String sql,final Object... values) {
		SQLQuery query = this.queryBySQL(sql);
		
		if(firstIndex!=-1 && pageSize!=-1){
			query.setFirstResult(firstIndex).setMaxResults(pageSize);
		}
		
		if (values != null && values.length > 0) {
			for (int i=0;i<values.length;i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query.list();
	}
	
	/**
	 * 根据sql统计个数
	 * 
	 * @param sql
	 * @param values
	 * @return
	 */
	public Integer countBySQL(final String sql,final Object... values) {
		SQLQuery query = this.queryBySQL(sql);
		if (values != null && values.length > 0) {
			for (int i=0;i<values.length;i++) {
				query.setParameter(i, values[i]);
			}
		}
		return GetterUtil.getInteger(query.uniqueResult());
	}

	/**
	 * 执行sql更新语句
	 * @param sql
	 * @return
	 */
	public int executeUpdateBySQL(String sql) {
		return getSession().createSQLQuery(sql).executeUpdate();
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 
	 * 只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,可实现新的函数,执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合. Hibernate.initialize
	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initEntity(T entity) {
		Hibernate.initialize(entity);
	}

	/**
	 * @see #initEntity(Object)
	 */
	public void initEntity(List<T> entityList) {
		for (T entity : entityList) {
			Hibernate.initialize(entity);
		}
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		return meta.getIdentifierPropertyName();
	}
}
