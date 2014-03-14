package com.fung.es.curd;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

 

public class QuerySearchTest {
	public static void main(String[] args) {
		Client client = ESUtils.getClient();
		QueryBuilder query = QueryBuilders.fieldQuery("desc", "丸");
		SearchResponse response = client.prepareSearch("twitter")
				.setTypes("tweet")
				//设置查询条件,
		        .setQuery(query)
		        .setFrom(0).setSize(60)
		        .execute()
		        .actionGet();
		/**
		 * SearchHits是SearchHit的复数形式，表示这个是一个列表
		 */
		SearchHits shs = response.getHits();
		for(SearchHit hit : shs){
			System.out.println("分数(score):"+hit.getScore()+", 业务描述(desc):"+
					hit.getSource().get("desc"));
		}
		client.close();
	}

}

/*
可以看到，搜索引擎已经将我们所有带丸的记录都筛选出来了。并且，字数最少的自动排在了最前面。是不是很智能。在完全没有配置ES任何东西之前，就能使用搜索功能了。
*/