package com.fung.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;

import java.util.GregorianCalendar;
import java.util.StringTokenizer;


 

/**
 * 类说明：
 * @author ljf@800pharm.com 
 * since 2012-3-27
 */
public class DateUtil {
	
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String getNowTimeUseDefFormat(){
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		return sdf.format(new Date());
	}
	
	public static Long getNowLongMillisecond(){
		Date date = new Date();
		return date.getTime();
	}
	
	public static String longToStringDateFormat(Long millisecond,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(millisecond));
	}
	
	public static String longToStringDateFormat(Long millisecond){
		return longToStringDateFormat(millisecond, DEFAULT_FORMAT);
	}
	
	/**
	 * 
	 * @param field
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date add(int field, Date date, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int fieldNewValue = (c.get(field) + value);
        c.set(field, fieldNewValue);
        return c.getTime();
    }
	
	
    /**
     * 计算剩余时间,返回形式 xx年XX月xx日XX小时xx分xx秒
     * @param beginTimes
     * @param endTimes
     * @return
     */
    public static String remainTimes(long beginTimes,long endTimes){
    	long between=(endTimes-beginTimes)/1000;//除以1000是为了转换成秒
    	
    	 StringBuffer sb=new StringBuffer();
    	 
    	if(endTimes>beginTimes){
	        long day=between/(24*3600);
	        long hour=between%(24*3600)/3600;
	        long minute=between%3600/60;
	        long second=between%60/60;
	    //    System.out.println(""+day+"天"+hour+"小时"+minute+"分"+second+"秒");
	        sb.append("离活动结束剩余：");
	        if(day>0){
	        	sb.append(day+"天");
	        }
	        if(hour>0){
	        	sb.append(hour+"小时");
	        }
	        if(minute>0){
	        	sb.append(minute+"分");
	        }
	       // if(second>0){
	        	sb.append(second+"秒");
	      //  }
    	}else{
    		sb.append("");
    	}
    	return sb.toString();
    }
    
    public static String fmtDateCNStr(String date){
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	DateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
    	Date d=null;
    	try {    
    		  d = format.parse(date);  // Wed sep 26 00:00:00 CST 2007    
    	} catch (Exception e) {    
    		 e.printStackTrace();    
    	}  
    	return format2.format(d);  
    }
    
    public static final String nowStr() {
		return dateTime2Str(new Date());
	}
	public static final String dateTime2Str(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public static final long getTimeInMillis(Date dateTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		return calendar.getTimeInMillis();
	}
	public static final long getTimeInMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.getTimeInMillis();
	}
	public static final long getEndDateTimeInMillis(String date) {
		return getTimeInMillis(dateStr2DateTime(date, "23:59:59"));
	}
	
	public static final Date dateStr2DateTime(String dateStr, String ext) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		Date date = dateFormat.parse(dateStr + " " + ext, new ParsePosition(0));
		return date;
	}
	
	public static final Date DateStr2DateTimeFormat(String date,String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final String date2Str2(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
	

	/**
	 * 当前时间往前推6天(7天内) <br/>
	 * modify by zhousp at 2014/12/08 9:06
	 * @return
	 */
	public static final String getFirstDay4ThisWeek(){
		SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd 00:00:00",Locale.CHINA);
		//7天内
		Date seventDays = DateUtil.add(Calendar.DAY_OF_MONTH, new Date(), -6);
		return smf.format(seventDays.getTime());
	}
	
	/*public static final String getFirstDay4ThisWeek(){
		SimpleDateFormat smf=new SimpleDateFormat("yyyy-MM-dd 00:00:00",Locale.CHINA);
		Calendar dt=Calendar.getInstance(Locale.CHINA);	//当前时间，貌似多余，其实是为了所有可能的系统一致  
		//dt.setTimeInMillis(System.currentTimeMillis()); 
		//System.out.println("当前时间:"+ smf.format(dt.getTime()));
		dt.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		//System.out.println("周一时间:"+smf.format(dt.getTime()));
		return smf.format(dt.getTime());
		
	}*/

//	public static final Date str2Date(String dateStr) throws Exception {
//		return str2Date(dateStr, "-");
//	}

//	public static final Date str2Date(String dateStr, String delim) throws Exception {
//		if (StringUtils.isEmpty(dateStr) == true) {
//			return new Date();
//		}
//		StringTokenizer stringTokenizer = new StringTokenizer(dateStr, delim);
//		if (stringTokenizer.countTokens() != 3) {
//			throw new Exception("date format error!");
//		}
//		int year = Integer.parseInt(stringTokenizer.nextToken());
//		int month = Integer.parseInt(stringTokenizer.nextToken());
//		int day = Integer.parseInt(stringTokenizer.nextToken());
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.YEAR, year);
//		calendar.set(Calendar.MONTH, month - 1);
//		calendar.set(Calendar.DAY_OF_MONTH, day);
//
//		return calendar.getTime();
//	}
	
	/**
	 * 日期相加-今日起时间往后加
	 * 
	 * @param addDay
	 * @return yyyy-mm-dd
	 */
	public static String todayAdd(int addDay) {
		GregorianCalendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day + addDay);
		Date yearDay = calendar.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;

	}

	/**
	 * 日期相加-今日起时间往后加后的千分秒时间
	 * 
	 * @param addDay
	 * @return calendar.getTimeInMillis()
	 */
	public static long getTodayAddLong(int addDay){ 
		GregorianCalendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day + addDay);
		// Date yearDay = calendar.getTime();
		// DateFormat df = DateFormat.getDateInstance();
		// String preYearDay = df.format(yearDay);
		return calendar.getTimeInMillis();
	}
	
	public static String formatDate(Date date,String format){
		java.util.Date current=new java.util.Date();
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat(format); 
        String c=sdf.format(current);
        return c;
	}

	public static void main(String[] args) throws Exception {
		String s = getFirstDay4ThisWeek();
		System.out.println(s);
		//System.out.println(str2Date("2013-04-09 34:45:67","/"));
	}
	
	
}
