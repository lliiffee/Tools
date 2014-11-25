package com.fung.wx.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

import com.fung.wx.model.AccessToken;

public class TokenUtil {
//	String key="wxbf261f64e52a3ceb";
//	String sec="6ee4bd7cb3c20c5c53a1015f3fc37edf";
	public  String getToken(String appid,String secrect) {
		this.setToken(appid,secrect);
		return at.getToken();
	}
	private AccessToken at;
	private  void setToken(String appid,String secrect)
	{
		String key=appid;
		String sec=secrect;

		String line=this.readOneLine(this.getClass().getResource("/").getPath()+"token.tmp", "utf-8");
		if(line!=null&& !"".equals(line))
		{
			String[] tokenStrings= line.split(",");
			at =new AccessToken();
			at.setToken(tokenStrings[0]);
			at.setTimeL(Long.parseLong(tokenStrings[1]));
			System.out.println("##"+at.getToken());
		}else  
		{     at=null;
			  at = WeiXinUtil.getAccessToken(key, sec);  
	    	  at.setTimeL(System.currentTimeMillis());
	    	  this.write(at.getToken()+","+at.getTimeL(), this.getClass().getResource("/").getPath()+"token.tmp", "utf-8");
		}

		//7200 过期
		System.out.println(System.currentTimeMillis()-at.getTimeL());
			if( (System.currentTimeMillis()-at.getTimeL())/1000 >7200)
			{
				
				 at=null;
				 at = WeiXinUtil.getAccessToken(key, sec); 
				 at.setTimeL(System.currentTimeMillis());
		    	  this.write(at.getToken()+","+at.getTimeL(), this.getClass().getResource("").getPath()+"token.tmp", "utf-8");
		    	  
			}
		 
		  
	}
	
	 public     String readOneLine(String fileName, String encoding) { 
	        StringBuffer fileContent  =   new  StringBuffer(); 
	        FileInputStream fis  =  null; 
            InputStreamReader isr  =  null; 
            BufferedReader br=null;
            
	         try  { 
	              fis  =   new  FileInputStream(fileName); 
	              isr  =   new  InputStreamReader(fis, encoding); 
	              br  =   new  BufferedReader(isr); 
	            String line  =   null ; 
	             if  ((line  =  br.readLine())  !=   null ) { 
	                fileContent.append(line); 
	              
	            } 
	        }  catch  (Exception e) { 
	            //e.printStackTrace(); 
	        } finally
	        {
	        	closeReader(br);
	        	closeReader(isr );
	        	closeInputStream(fis);
	        }
	         return  fileContent.toString(); 
	    } 
	 
	   public      void  write(String fileContent, String fileName, String encoding) { 
		   FileOutputStream fos=null;
		   OutputStreamWriter osw =null;
	         try  { 
	              fos  =   new  FileOutputStream(fileName); 
	              osw  =   new  OutputStreamWriter(fos, encoding); 
	            osw.write(fileContent); 
	            osw.flush(); 
	        }  catch  (Exception e) { 
	           // e.printStackTrace(); 
	        }finally
	        {
	        	 
	        	 try {
	        		 osw.close();
	 			} catch (Exception e) {
	 				//e.printStackTrace();
	 			}finally{
	 				osw=null;
	 			}
	        	closeInputStream(fos);
	        }
	    } 
	   
	   
	   
		 private  void closeInputStream(OutputStream os)  
		 {
			 try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				os=null;
			}
			
		 }
	 
	 private  void closeInputStream(InputStream is)  
	 {
		 try {
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			is=null;
		}
		
	 }
	 
	 private static void closeReader(Reader rd)  
	 {
		 try {
			rd.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			rd=null;
		}
		
	 }
}
