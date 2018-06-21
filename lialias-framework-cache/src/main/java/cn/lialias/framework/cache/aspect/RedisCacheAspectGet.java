package cn.lialias.framework.cache.aspect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.List;

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

import cn.lialias.framework.cache.ReturnType;
import cn.lialias.framework.cache.annotation.CacheableRedisGet;
import cn.lialias.framework.redis.AliasRedisCache;

@Aspect
@Component
public class RedisCacheAspectGet {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AliasRedisCache redisCache;

	@Pointcut("@annotation(cn.lialias.framework.cache.annotation.CacheableRedisGet)")
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
			CacheableRedisGet cacheableRedisGet = method.getAnnotation(CacheableRedisGet.class);
			if (cacheableRedisGet == null) {
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
			Object expkey = cacheAdviceSupport.generateKey(cacheableRedisGet.cachekey(), context);

			if (expkey == null) {
				return joinPoint.proceed();
			}

			// 获取缓存
			Object cacheObject = getCacheObject(cacheableRedisGet,
					cacheableRedisGet.module().concat(expkey.toString()));
			if (null != cacheObject) {
				return cacheObject;
			}

			// 后置通知
			result = joinPoint.proceed();
			if (null != result) {
				setCacheObject(cacheableRedisGet, cacheableRedisGet.module().concat(expkey.toString()), result);
			}

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
	 * @param annotation
	 * @param key
	 * @return
	 */
	private Object getCacheObject(CacheableRedisGet cacheableRedisGet, String key) {

		Object cacheObject = null;
		ReturnType returnType = cacheableRedisGet.returnType();
		if (returnType == ReturnType.OBJECT) {
			cacheObject = redisCache.getCache(key, cacheableRedisGet.clazzOut());
		} else if (returnType == ReturnType.COLLECTION) {
			List<?> list = redisCache.getListCache(key, cacheableRedisGet.clazzOut());
			if (!list.isEmpty()) {
				cacheObject = list;
			}
		}

		return cacheObject;

	}

	/**
	 * 
	 * @param annotation
	 * @param key
	 * @param value
	 * @throws UnsupportedEncodingException
	 */
	private void setCacheObject(CacheableRedisGet cacheableRedisGet, String key, Object value)
			throws UnsupportedEncodingException {
		ReturnType returnType = cacheableRedisGet.returnType();
		if (returnType == ReturnType.OBJECT) {
			redisCache.putCacheWithExpireTime(key, value, cacheableRedisGet.timeout());
		} else if (returnType == ReturnType.COLLECTION) {
			redisCache.putListCacheWithExpireTime(key, (List<?>) value, cacheableRedisGet.timeout());
		}
	}

}
