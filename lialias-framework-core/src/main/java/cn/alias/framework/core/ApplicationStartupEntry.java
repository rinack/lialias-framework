package cn.alias.framework.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.StringUtils;

@ComponentScan({ "cn.alias.framework.core", "${alias.module.basePackages:}" })
@SpringBootApplication
public class ApplicationStartupEntry {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupEntry.class);

	public static void main(String[] args) {
		//加载配置信息
		Properties applicationConfigure =  deployApplicationConfigurable(args);
		//输出配置文件
		
		//启动应用
		SpringApplication springApplication = new SpringApplication(ApplicationStartupEntry.class);
		springApplication.setBannerMode(Banner.Mode.CONSOLE);
		springApplication.setAddCommandLineProperties(true);
		ConfigurableApplicationContext applicationContext = springApplication.run(args);

		logger.info("------------> " + "lialias" + " 应用启动成功	 ........");

		Map<String, ApplicationStartupListener> startupListeners = applicationContext
				.getBeansOfType(ApplicationStartupListener.class);
		if (startupListeners != null) {
			for (ApplicationStartupListener listener : startupListeners.values()) {
				listener.useStartup(applicationContext);
			}
		}
		
	}

	/**
	 * 
	 * 1.命令行参数 
	 * 2.来自java:comp/env的JNDI属性
	 * 3.Java系统属性（System.getProperties()）
	 * 4.操作系统环境变量 5.RandomValuePropertySource配置的random.*属性值
	 * 6.jar包外部的application-{profile}.properties或application.yml(带spring.profile)配置文件
	 * 7.jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件
	 * 8.jar包外部的application.properties或application.yml(不带spring.profile)配置文件
	 * 9.jar包内部的application.properties或application.yml(不带spring.profile)配置文件
	 * 10.@Configuration注解类上的@PropertySource
	 * 11.通过SpringApplication.setDefaultProperties指定的默认属性
	 * 
	 * @param args
	 * @return
	 * 
	 */
	public static Properties deployApplicationConfigurable(String[] args) {
		Properties commandLineProperty = commandApplicationProperties(args);
		return null;
	}

	/**
	 * 读取命令行
	 * 
	 * @param args
	 * @return
	 */
	public static Properties commandApplicationProperties(String[] args) {
		Properties commandLineProperty = new Properties();
		// 取得命令行配置
		SimpleCommandLinePropertySource simpleCommandLinePropertySource = new SimpleCommandLinePropertySource(args);
		String[] commandLineConfigItems = simpleCommandLinePropertySource.getPropertyNames();
		for (String commandLineConfigItem : commandLineConfigItems) {
			commandLineProperty.setProperty(commandLineConfigItem,
					simpleCommandLinePropertySource.getProperty(commandLineConfigItem));
		}
		return commandLineProperty;
	}

	/**
	 * 加载 jar包内配置文件
	 * @param properties
	 * @param fileName
	 */
	private static void initializeConfigurationClassPath(Properties properties, String fileName) {
		InputStream is = ApplicationStartupEntry.class.getResourceAsStream(fileName);
		try {
			Properties props = new Properties();
			props.load(is);
			Iterator<Object> iterator = props.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				properties.put(key, props.get(key));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 加载本地目录配置文件
	 * @param properties
	 * @param fileName
	 */
	private static void initializeConfigurationLocalFile(Properties properties, String fileName) {

		File file = new File(fileName);
		if (!file.exists()) {
			return;
		}

		Properties props = new Properties();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			props.load(br);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return;
		}

		Iterator<Object> iterator = props.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			properties.put(key, props.get(key));
		}

	}

	/**
	 * 合并配置文件
	 * @param src
	 * @param dest
	 * @return
	 */
	private static Properties mergeConfigurable(Properties src, Properties dest) {
		Properties ret = new Properties();
		if (src != null) {
			Enumeration<?> keyEnum = src.keys();
			while (keyEnum.hasMoreElements()) {
				String key = keyEnum.nextElement().toString();
				ret.put(key, src.getProperty(key));
			}
		}

		if (dest != null) {
			Enumeration<?> keyEnum = dest.keys();
			while (keyEnum.hasMoreElements()) {
				String key = keyEnum.nextElement().toString();
				ret.put(key, dest.getProperty(key));
			}
		}

		return ret;
	}

	/**
	 * 配置文件转换
	 * @param properties
	 * @return
	 */
	private static String[] convertToLaunchArgs(Properties properties) {
		String[] ret = new String[properties.size()];
		Set<Object> keys = properties.keySet();
		int idx = 0;
		for (Object key : keys) {
			String cfgValue = properties.get(key).toString();
			if (StringUtils.hasText(cfgValue)) {
				String arg = "--" + key.toString() + "=" + cfgValue;
				ret[idx++] = arg;
			} else {
				ret[idx++] = "";
			}
		}
		return ret;
	}

	/**
	 * 读取所有的配置文件信息
	 * @param properties
	 * @return
	 */
	public static Map<String, String> getAllProperties(Properties properties) {
		Map<String, String> map = new HashMap<>();
		Enumeration<?> en = properties.propertyNames();
		String key = "";
		String value = "";
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			value = properties.getProperty(key);
			map.put(key, value);
		}
		return map;
	}

}
