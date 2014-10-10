package  com.fung.wx.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fung.wx.model.AccessToken;
import com.fung.wx.model.Button;
import com.fung.wx.model.CommonButton;
import com.fung.wx.model.ComplexButton;
import com.fung.wx.model.Menu;
import com.fung.wx.model.MenuMapper;
import com.fung.wx.model.WxMenuSetting;
import com.fung.wx.util.LogUtil;
import com.fung.wx.util.MessageUtil;
import com.fung.wx.util.WeiXinUtil;

 

/**
 * 菜单管理器
*    
* 项目名称：WeiChatService   
* 类名称：MenuManager   
* 类描述：   
* 创建人：zhouling
* 创建时间：Nov 15, 2013 4:55:47 PM    
* 修改备注：   
* @version    
*
 */
public class MenuManager {
	 private DataSource dataSource;
     /**
      * spring提供的jdbc操作辅助类
 */
     private JdbcTemplate jdbcTemplate;
 
     // 设置数据源
     public void setDataSource(DataSource dataSource) {
         this.jdbcTemplate = new JdbcTemplate(dataSource);
     }
	private static LogUtil log=new LogUtil(MenuManager.class);
	
 
	 
	  /** 
	     * 组装菜单数据 
	     *  
	     * @return 
	     */  
	    private   Menu getMenu() {
	    	
	    	List<WxMenuSetting> list = this.jdbcTemplate.query(
	    			"select * from tb_wx_menuset where menu_type!='hidden' order by parent,sort , id "
	    			,new Object[]{}
	    			,new int[]{}
	    			, new MenuMapper());
	    	
	    	Map<Integer,Button> rootMenus=new TreeMap<Integer,Button>();
	    	for(WxMenuSetting wx :list)
	    	{
	    		 
	    		if(wx.getParent()==0 )
	    		{
	    		  if(wx.getMenuType().equals("root"))
	    		  {
	    			  ComplexButton mainBtn = new ComplexButton(); 
		    			mainBtn.setName(wx.getMenuName());
		    			rootMenus.put(wx.getId(), mainBtn);
	    		  }else if(wx.getMenuType().equals("view"))
	    		  {
	    			  CommonButton btn = new CommonButton(); 
	    			  btn.setName(wx.getMenuName());
		    			btn.setType(wx.getMenuType());
		    			if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(wx.getMenuType()))
		    			{
		    			 btn.setKey(wx.getId()+"");
		    			}else
		    			{
		    				btn.setUrl(wx.getUrl());
		    			}
		    			rootMenus.put(wx.getId(), btn);
	    		  }
	    			
	    			
	    		}else
	    		{
	    			CommonButton btn = new CommonButton(); 
	    			btn.setName(wx.getMenuName());
	    			btn.setType(wx.getMenuType());
	    			if("click".equals(wx.getMenuType()))
	    			{
	    			 btn.setKey(wx.getId()+"");
	    			}else
	    			{
	    				btn.setUrl(wx.getUrl());
	    			}
	    			
	    			ComplexButton mainBtn=(ComplexButton)rootMenus.get(wx.getParent());
	    			if(mainBtn!=null)
	    			mainBtn.addSub_button(btn);
	    		}
	    	}
	   
	        Menu menu = new Menu();  
	        for(Button mbt:rootMenus.values())
	        {
	        	 menu.getButtons().add(mbt);
	        }
	       
	        return menu;  
	    }  
	  

	    
	    public static void main(String[] args)
	    {
	    	
	    	
	    	try {
				//sendOrderHandleStatusToWx();
				String[] springConfig  = 
					{	
						"wxopenApi_applicatin.xml" 
					};
				ApplicationContext ctx = 
						new ClassPathXmlApplicationContext(springConfig);
			  
						MenuManager mm =(MenuManager)ctx.getBean("menuManager"); 
						System.out.println(JSONObject.toJSONString(mm.getMenu().getButtons().toArray()));
						
				    	  AccessToken at = WeiXinUtil.getAccessToken("wx7179cc98fb47eff5", "823ae7209cb695d4a4a0c71071d6006e ");  
				    	//  String tokey="TRYP5YwmKWU_2J1rKyGoYgPNLfTKGufXk9JXySbHXj9xP8ZNEAREpZaB_yxhgrEv9b2Neqfbx3KStdlbkxXOlA";
				    	  String  tokey=at.getToken();
					        if (null != at) {  
					            // 调用接口创建菜单   
				    	  
					            int result = WeiXinUtil.createMenu(mm.getMenu(), tokey);  
					         
					            // 判断菜单创建结果   
					            if (0 == result)  
					                log.info("菜单创建成功！");  
					            else  
					                log.info("菜单创建失败，错误码：" + result);  
					        }  
					        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    	 //MenuManager mm=new MenuManager();
	    	

	    }


}
