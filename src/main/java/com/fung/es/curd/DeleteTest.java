package com.fung.es.curd;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
 

 

public class DeleteTest {
	public static void main(String[] argv){
		Client client = ESUtils.getClient();
		//在这里创建我们要索引的对象
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
				.execute().actionGet();
		System.out.println(response.getId());
		System.out.println(ESUtils.toJson(response.getHeaders()));
	}
}