package com.fung.es.curd;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;



public class ESUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static String toJson(Object o){
		try {
			return objectMapper.writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static Client getClient(){
		Settings settings = ImmutableSettings.settingsBuilder()
				//指定集群名称
                .put("cluster.name", "elasticsearch")
                //探测集群中机器状态
                .put("client.transport.sniff", true).build();
		/*
		 * 创建客户端，所有的操作都由客户端开始，这个就好像是JDBC的Connection对象
		 * 用完记得要关闭
		 */
		Client client = new TransportClient(settings)
		.addTransportAddress(new InetSocketTransportAddress("192.168.0.202", 9300));
		
		return client;
	}
}
