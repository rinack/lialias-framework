package cn.lialias.framework.cache.aspect;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class BeanFactoryHelper implements BeanFactoryAware {

	private static BeanFactory beanFactory; // BEAN工厂

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BeanFactoryHelper.beanFactory = beanFactory;
	}

	public static BeanFactory getBeanfactory() {
		return BeanFactoryHelper.beanFactory;
	}
}
