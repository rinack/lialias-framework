package cn.lialias.framework.cache.aspect;

import java.lang.reflect.Method;

public class CacheAdviceSupport {

    /**
     * Compute the key for the given caching operation.
     * 
     * @return the generated key, or {@code null} if none can be generated
     */
    protected Object generateKey(String cachekey, CacheOperationContext context) {
        return context.generateKey(cachekey, ExpressionEvaluator.NO_RESULT);
    }

    protected CacheOperationContext getOperationContext(Method method, Object[] args, Object target, Class<?> targetClass) {
        return new CacheOperationContext(method, args, target, targetClass);
    }

}