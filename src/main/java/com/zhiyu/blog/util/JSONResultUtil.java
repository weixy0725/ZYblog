package com.zhiyu.blog.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 返回结果封装工具类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
public class JSONResultUtil {

	/**
	 * 
	 * 成果结果
	 * 
	 * @param code
	 * @param object
	 * @param array
	 * @return
	 */
	public static JSONObject successResult(int code, JSONObject object, JSONArray array) {
		JSONObject jsonObject = new JSONObject();
		JSONObject result = new JSONObject();

		result.put("code", code);
		result.put("info", "");
		result.put("developInfo", "");

		jsonObject.put("result", result);
		jsonObject.put("object", object);
		jsonObject.put("array", array);

		return jsonObject;

	}

	/**
	 * 失败结果
	 * 
	 * @param code
	 * @param info
	 * @param developInfo
	 * @return
	 */
	public static JSONObject failResult(int code, String info, String developInfo) {

		JSONObject jsonObject = new JSONObject();
		JSONObject result = new JSONObject();

		result.put("code", code);
		result.put("info", info);
		result.put("developInfo", developInfo);

		jsonObject.put("result", result);
		jsonObject.put("object", new JSONObject());
		jsonObject.put("array", new JSONArray());

		return jsonObject;
	}

}
