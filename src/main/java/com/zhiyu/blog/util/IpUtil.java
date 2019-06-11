package com.zhiyu.blog.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * IP工具类
 * 
 * @author xinyuan.wei
 * @date 2019年6月3日
 */
@Slf4j
public class IpUtil {
    /**
     * 获取IP
     * 
     * @param request
     * @return
     */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		log.info("x-forwarded-for ip: " + ip);
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			log.info("Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			log.info("WL-Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			log.info("HTTP_CLIENT_IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			log.info("HTTP_X_FORWARDED_FOR ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			log.info("X-Real-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("getRemoteAddr ip: " + ip);
		}
		log.info("获取客户端ip: " + ip);
		return ip;
	}
    
	/**
	 * 隐藏IP信息
	 * 
	 * @param ip
	 * @return
	 */
	public static String hiddenIP(String ip) {
		String hiddenIP = "";
		if (!StringUtils.isEmpty(ip)) {
			if(ip.equals("0:0:0:0:0:0:0:1")) {
				ip="127.0.0.1";
			}
			String[] ipGroup = ip.split("\\.");
			hiddenIP = ipGroup[0] + "." + ipGroup[1] + ".*." + ipGroup[3];
		}
		return hiddenIP;
	}

}
