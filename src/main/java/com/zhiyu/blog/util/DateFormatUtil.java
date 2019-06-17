package com.zhiyu.blog.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 * 
 * @author xinyuan.wei
 * @date 2019年5月5日
 */
public class DateFormatUtil {

	public static String DateFormat(LocalDateTime localDateTime) {

		String datetime = "";

		if (null != localDateTime) {

			datetime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}

		return datetime;
	}
	
	public static String DateFormat(String localDateTimeString) {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString,dateTimeFormatter);
		
		return DateFormat(localDateTime);	
	}

}
