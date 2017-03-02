package com.pet.common;


import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import com.pet.common.BaseDao;
import com.pet.common.BaseService;

/**
 * 注入BaseDao 及 BaseService 的工具类
 * 
 * @auto zhangQ
 * create date:2013-10-11 20:46
 */
public class InjectBaseDependencyHelper {


	/**
	 * 查找并且注入BaseDao
	 * @param baseService
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void findAndInjectBaseDaoDependency(BaseService<?> baseService) {
        final Set<Object> candidates = findDependencies(baseService,Autowired.class);

        if (candidates.size() != 1) {
            throw new IllegalStateException("expect 1 @Autowired anntation BaseDao subclass bean, but found " + candidates.size() +
                            ", please check class [" + baseService.getClass() + "] @Autowired annotation.");
        }

        Object baseDao = candidates.iterator().next();

        if (baseDao.getClass().isAssignableFrom(Autowired.class)) {
            throw new IllegalStateException("[" + baseService.getClass() + "] @Autowired annotation bean must be BaseDao subclass");
        }
       baseService.setBaseDao((BaseDao) baseDao);
    }

    /**
     * 根据类在目标对象上的字段上查找依赖
     *
     * @param target
     * @param annotation
     */
    private static Set<Object> findDependencies(final Object target, final Class<? extends Annotation> annotation) {

        final Set<Object> candidates = new HashSet<Object>();

        ReflectionUtils.doWithFields(
                target.getClass(),
                new ReflectionUtils.FieldCallback() {
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        ReflectionUtils.makeAccessible(field);
                        Object obj = ReflectionUtils.getField(field, target);
                        candidates.add(obj);
                    }
                },
                new ReflectionUtils.FieldFilter() {
                    public boolean matches(Field field) {
                    	ReflectionUtils.makeAccessible(field);
                    	Object obj = ReflectionUtils.getField(field, target);
                        return (obj instanceof BaseDao) && field.isAnnotationPresent(annotation);
                    }
                }
        );

        ReflectionUtils.doWithMethods(
                target.getClass(),
                new ReflectionUtils.MethodCallback() {
                    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                        ReflectionUtils.makeAccessible(method);
                        PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
                        candidates.add(ReflectionUtils.invokeMethod(descriptor.getReadMethod(), target));
                    }
                },
                new ReflectionUtils.MethodFilter() {
                    public boolean matches(Method method) {
                        boolean hasAnnotation = false;
                        hasAnnotation = method.isAnnotationPresent(annotation);
                        if (!hasAnnotation) {
                            return false;
                        }

                        boolean hasReadMethod = false;
                        PropertyDescriptor descriptor = BeanUtils.findPropertyForMethod(method);
                        hasReadMethod = descriptor != null && descriptor.getReadMethod() != null;

                        return hasReadMethod;

                    }
                }
        );

        return candidates;
    }

}
