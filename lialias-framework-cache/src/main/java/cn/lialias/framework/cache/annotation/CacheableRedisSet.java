package cn.lialias.framework.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.lialias.framework.cache.ReturnType;

/**
 * 自定义注解，结合AOP实现Redis自动缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface CacheableRedisSet {

	/**
	 * 模块全名
	 * 
	 * @return
	 */
	String module();

	/**
	 * 如果入参类型为对象型,则需指定关键字段
	 * 
	 * @return
	 */
	String objectFields();

	/**
	 * 如果返回类型为对象
	 * 
	 * @return
	 */
	Class<?> clazzPut();

	/**
	 * 返回参数类型
	 * 
	 * @return
	 */
	ReturnType returnType() default ReturnType.VOID;

	/**
	 * 如果返回类型为对象
	 * 
	 * @return
	 */
	Class<?> clazzOut() default Object.class;

	/**
	 * 缓存有效时间
	 * 
	 * @return
	 */
	int timeout() default 600;

	/**
	 * 是否序列化
	 * 
	 * @return
	 */
	boolean serialize() default false;

}
