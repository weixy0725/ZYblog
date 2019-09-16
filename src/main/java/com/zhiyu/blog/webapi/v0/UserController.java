package com.zhiyu.blog.webapi.v0;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyu.blog.bean.UserBean;
import com.zhiyu.blog.dao.UserDao;
import com.zhiyu.blog.service.UserService;
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
	
	@Value("${MD5.key}")
	private String key;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "登录")
	@PostMapping(value = "/login")
	public JSONObject login(@RequestBody UserBean user) {
		JSONObject object = new JSONObject();
		try {
		UserBean userBean = userService.findByName(user.getUsername());
		if(null!=userBean&&DigestUtils.md5Hex(user.getPassword()+key).equals(userBean.getPassword())) {
			String token = UUID.randomUUID().toString().replaceAll("-","");
		    object.put("token", token);
		    userBean.setToken(token);
		    userService.save(userBean);//存储token
		}else {
			return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "用户名或密码错误！", "");
		}
		}catch(Exception e) {
			return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "", e.getMessage());
		}
		
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, new JSONArray());
	}
	
	@ApiOperation(value = "获取用户信息")
	@GetMapping(value = "/info")
	public JSONObject getInfo(@RequestParam String token) {
		JSONObject object = new JSONObject();
		
		try {
			UserBean userBean = userService.findByToken(token);
			if(null!=userBean) {
				object.put("name", userBean.getUsername());
		        object.put("avatar", "avatar");
			}else {
				return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "请重新登录系统！", "");
			}
			}catch(Exception e) {
				return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "", e.getMessage());
			}
			
			return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, new JSONArray());
	}
	
	@ApiOperation(value = "获取用户信息")
	@PostMapping(value = "/logout")
	public JSONObject logOut(HttpServletRequest httServletRequest) {
		JSONObject object = new JSONObject();
		
		try {
			String token = httServletRequest.getHeader("X-Token")==null?"":httServletRequest.getHeader("X-Token");
			UserBean userBean = userService.findByToken(token);
			if(null!=userBean) {
				userBean.setToken("");
				userService.save(userBean);
			}else {
				return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "退出系统失败！", "");
			}
			}catch(Exception e) {
				return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "", e.getMessage());
			}
			
			return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, new JSONArray());
	}

}
