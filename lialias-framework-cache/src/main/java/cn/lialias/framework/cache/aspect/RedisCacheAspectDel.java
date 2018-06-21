package cn.lialias.framework.cache.aspect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.lialias.framework.cache.annotation.CacheableRedisDel;
import cn.lialias.framework.redis.AliasRedisCache;

@Aspect
@Component
public class RedisCacheAspectDel {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AliasRedisCache redisCache;

	@Pointcut("@annotation(cn.lialias.framework.cache.annotation.CacheableRedisDel)")
	public void setJoinPoint() {

	}

	// 环绕通知:可以获取返回值
	@Around(value = "setJoinPoint()")
	public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		Object result = null;
		try {

			// 前置通知,先执行一遍注解注入的方法
			// result = joinPoint.proceed();

			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Method targetMethod = AopUtils.getMostSpecificMethod(methodSignature.getMethod(),
					joinPoint.getTarget().getClass());

			// 获取注解
			Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
			CacheableRedisDel cacheableRedisDel = method.getAnnotation(CacheableRedisDel.class);
			if (cacheableRedisDel == null) {
				return joinPoint.proceed();
			}

			// 获取传入参数
			// Object[] args = joinPoint.getArgs();
			// 生成key
			// String key = generateKey(cacheableRedisGet, args);

			// 获取实现类
			// Class<?> impclazz =
			// AopProxyUtils.ultimateTargetClass(proceedingJoinPoint.getThis());

			CacheAdviceSupport cacheAdviceSupport = new CacheAdviceSupport();
			CacheOperationContext context = cacheAdviceSupport.getOperationContext(targetMethod, joinPoint.getArgs(),
					joinPoint.getTarget(), joinPoint.getTarget().getClass());
			Object expkey = cacheAdviceSupport.generateKey(cacheableRedisDel.cachekey(), context);

			if (expkey == null) {
				return joinPoint.proceed();
			}

			// 后置通知
			result = joinPoint.proceed();
			delCacheObject(cacheableRedisDel.module().concat(expkey.toString()));

			// 返回通知
			return result;

		} catch (Exception e) {
			// 异常通知
			logger.error(e.getMessage(), e);
		}

		return result;

	}

	/**
	 * 
	 * @param
	 * @param key
	 * @throws UnsupportedEncodingException
	 */
	private void delCacheObject(String key) throws UnsupportedEncodingException {
		if (StringUtils.hasText(key)) {
			if (key.indexOf(":*") != -1) {
				redisCache.deleteCacheWithPattern(key);
			} else {
				redisCache.deleteCache(key);
			}
		}
	}

}
