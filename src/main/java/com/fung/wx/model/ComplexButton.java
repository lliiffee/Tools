package com.fung.wx.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 复杂按钮（父按钮）
*    
* 项目名称：WeiChatService   
* 类名称：ComplexButton   
* 类描述：   
* 创建人：zhouling
* 创建时间：Nov 15, 2013 4:00:39 PM    
* 修改备注：   
* @version    
*
 */
public class ComplexButton extends Button {
	
	
	
	private List <Button> sub_button;
	
	public ComplexButton(){
		this.sub_button=new ArrayList<Button>();
	}

	 
	
	public List<Button> getSub_button() {
		return sub_button;
	}



 


	public void addSub_button(Button button)
	{
		 
			this.getSub_button().add(button);
		 
	}

}
