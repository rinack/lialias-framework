package cn.lialias.framework.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，结合AOP实现Redis自动缓存
 * 
 * 调用示例
 * 方法前加入,注解
 * @CacheableRedisGet(module = "sys_role_module_roleid", cachekey = "':' + #paramRoleId" , returnType = ReturnType.COLLECTION, clazzOut = SysRoleModule.class)
 * public List<SysRoleModule> getRoleBindModule(String paramRoleId,String orderNumber){}
 * 注：这里面注解里的参数是根据每次调用的时候动态传入的,语法详见：Spring Expression Language (SpEL)
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface CacheableRedisDel {

	/**
	 * 模块全名
	 * 
	 * @return
	 */
	String module();
	
	/**
	 * 缓存主键
	 * @return
	 */
	String cachekey();

}
