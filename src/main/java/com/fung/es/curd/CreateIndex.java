package com.fung.es.curd;

import java.io.IOException;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;

public class CreateIndex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//创建连接elasticsearch服务的client
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("client.transport.sniff", true)
				.put("cluster.name", "elasticsearch_54")
				.build();  
		Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("192.168.0.3", 9300));  
		
		//创建索引
		//elasticsearch的java客户端，支持多种方式构建索引数据，这里有两种方式的代码示例：使用jsonbuilder构建数据
		IndexResponse response;
		
		try {                               //库                                           //表                                          //主键
			response = client.prepareIndex("comment_index", "comment_ugc", "comment_123674")  
				    .setSource( XContentFactory.jsonBuilder()
				    		.startObject()
				    		.field("author", "569874")  
				      .field("author_name", "riching")  
				      .field("mark", 232)  
				      .field("body", "北京不错，但是人太多了")  
				      .field("createDate", "20130801175520")  
				      .field("valid", true).endObject())  
				    .setTTL(8000)  
				    .execute().actionGet();
			System.out.println(response.getId());  
			
		} catch (ElasticSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
			  
		//另外一种，是把数据构造成json串，直接传给client 
//		Student student = new Student(103161066, 20, "riching", "beijing");  
//		String jsonValue = mapper.writeValueAsString(student);  
//		response = client.prepareIndex("student_index", "student_info", "stu_103161066").setSource(jsonValue).execute().actionGet();  
//		System.out.println(response.getId());  
			

	}
	
	class Student{
		private int id;
		private int age;
		private String name;
		private String address;
		
		public Student(int id,int age,String name,String address){
			this.id=id;
			this.age=age;
			this.name=name;
			this.address=address;
		}
	}

}


/*
 * 4、根据id获取数据 
Java代码  收藏代码
GetResponse responseGet = client.prepareGet("comment_index", "comment_ugc",         "comment_123674").execute().actionGet();  
System.out.println(responseGet.getSourceAsString());  


5、查询索引 
Java代码  收藏代码
SearchRequestBuilder builder = client.prepareSearch("comment_index").setTypes("comment_ugc").setSearchType(SearchType.DEFAULT).setFrom(0).setSize(100);  
BoolQueryBuilder qb = QueryBuilders.boolQuery().must(new   QueryStringQueryBuilder("北京").field("body"))  
    .should(new QueryStringQueryBuilder("太多").field("body"));  
builder.setQuery(qb);  
SearchResponse response = builder.execute().actionGet();  
System.out.println("  " + response);  
System.out.println(response.getHits().getTotalHits());  

执行结果 
Java代码  收藏代码
{  
  "took" : 8,  
  "timed_out" : false,  
  "_shards" : {  
    "total" : 5,  
    "successful" : 5,  
    "failed" : 0  
  },  
  "hits" : {  
    "total" : 1,  
    "max_score" : 0.19178301,  
    "hits" : [ {  
      "_index" : "comment_index",  
      "_type" : "comment_ugc",  
      "_id" : "comment_123674",  
      "_score" : 0.19178301, "_source" : {"author":"569874","author_name":"riching","mark":232,"body":"北京不错，但是人太多了","createDate":"20130801175520","valid":true}  
    } ]  
  }  
}  
1  


6、删除索引，可以根据索引id删除索引，也可以构造query进行删除，这跟lucene的api是类似的，只不过api不一样而已 
Java代码  收藏代码
DeleteResponse response = client.prepareDelete("comment_index", "comment_ugc", "comment_123674") .setOperationThreaded(false).execute().actionGet();  
System.out.println(response.getId());  

这个删除有个小问题，如果删除完立即进行查询还是可以查到
*/
