package com.fung.es.curd;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

 

public class BulkIndexTest {
	
	public static void main(String[] args) {
		String[] desc = new String[]{
				"玉屏风口服液",
				"清咽丸",
				"四消丸",
				"感冒清胶囊",
				"人参归脾丸",
				
				"人参健脾丸",
				"明目地黄丸",
				"小儿咳喘灵颗粒",
				"小儿化痰止咳冲剂",
				"双黄连",
				"六味地黄丸"
		};
		Client client = ESUtils.getClient();
		int j= 0;
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for(int i=1000;i<1010;i++){
			LogModel l = new LogModel();
			l.setDesc(desc[j]);
			j++;
			String json = ESUtils.toJson(l);
			IndexRequestBuilder indexRequest = client.prepareIndex("twitter", "tweet")
			//指定不重复的ID		
	        .setSource(json).setId(String.valueOf(i));
			//添加到builder中
			bulkRequest.add(indexRequest);
		}
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
		    // process failures by iterating through each bulk response item
			System.out.println(bulkResponse.buildFailureMessage());
		}
	}
}
