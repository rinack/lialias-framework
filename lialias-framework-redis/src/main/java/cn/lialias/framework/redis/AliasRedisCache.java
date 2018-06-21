package cn.lialias.framework.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;

public class AliasRedisCache {

	@Autowired
	private RedisCache redisCache;

	public Object getCache(String key, Class<?> clazzOut) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<?> getListCache(String key, Class<?> clazzOut) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putCacheWithExpireTime(String key, Object value, int timeout) {
		// TODO Auto-generated method stub
		
	}

	public void putListCacheWithExpireTime(String key, List<?> value, int timeout) {
		// TODO Auto-generated method stub
		
	}

	public void deleteCacheWithPattern(String key) {
		// TODO Auto-generated method stub
		
	}

	public void deleteCache(String key) {
		// TODO Auto-generated method stub
		
	}
	
}
