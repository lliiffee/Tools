package com.fungspring.beans;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 工厂类：从外部读取配置信息创建并注入所需对象
 */
class ProxyFactory {

	// 代理对象的提供者
	private ProxyHandler handler;
	// 被代理对象
	private Object target;
	// 所有组件类的集合
	private Map<String, Object> beans;

	/**
	 * 读取外部配置文件初始化所有组件间的关系
	 */
	public ProxyFactory(String configFile) {
		beans = new HashMap<String, Object>();

		try {
			Properties props = new Properties();
			props.load(new FileInputStream(configFile));

			// bean 声明及依赖注入的 key-value 对
			Map<String, String> beanKV = new HashMap<String, String>();
			Map<String, String> diKV = new HashMap<String, String>();

			for (Map.Entry entry : props.entrySet()) {
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();

				if (key.startsWith("Bean")) {
					beanKV.put(key, value);
				} else if (key.startsWith("DI")) {
					diKV.put(key, value);
				}
			}

			// 一定要先处理 bean 声明再进行依赖注入
			this.processKeyValue(beanKV);
			this.processKeyValue(diKV);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理 key-value
	 */
	private void processKeyValue(Map<String, String> map) throws Exception {
		for (Map.Entry entry : map.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			this.handleEntry(key, value);
		}
	}

	/**
	 * 利用工具类 BeanUtil 处理key-value对，即创建或注入bean
	 */
	private void handleEntry(String key, String value) throws Exception {
		String[] keyParts = key.split("\\.");

		String tag = keyParts[0];
		if ("Bean".equals(tag)) {
			// 组件定义：利用反射实例化该组件
			Object bean = Class.forName(value).newInstance();
			System.out.println("组件定义：" + bean.getClass().getName());
			beans.put(keyParts[1], bean);

		} else if ("DI".equals(tag)) {
			// 依赖注入：获取需要bean的主体，以及被注入的实例
			Object bean = beans.get(keyParts[1]);
			Object fieldRef = beans.get(value);
			System.out.println("依赖注入：" + bean.getClass().getName() + "."
					+ fieldRef.getClass().getName());
			BeanUtil.setProperty(bean, keyParts[2], fieldRef);
		}
	}

	/**
	 * 针对Factory已创建的Target获取代理对象
	 */
	public Object getProxy(String proxyName, String targetNanme) {
		Object target = this.beans.get(targetNanme);
		if (target != null) {
			this.handler = (ProxyHandler) this.beans.get(proxyName);
			return this.handler.getProxy(target);
		}
		return null;
	}

}