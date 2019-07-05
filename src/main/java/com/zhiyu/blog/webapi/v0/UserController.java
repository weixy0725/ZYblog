package com.zhiyu.blog.webapi.v0;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyu.blog.util.JSONResultUtil;
import com.zhiyu.blog.util.ResultCodeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登录接口
 * 
 * @author xinyuan.wei
 * @date 2019年5月8日
 */
@Api(tags = "文章管理")
@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@ApiOperation(value = "登录")
	@PostMapping(value = "/login")
	public JSONObject login() {
		JSONObject object = new JSONObject();
		object.put("token", "1121323");
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, new JSONArray());
	}

}
