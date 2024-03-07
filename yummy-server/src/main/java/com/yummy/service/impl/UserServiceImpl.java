package com.yummy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yummy.constant.MessageConstant;
import com.yummy.constant.StatusConstant;
import com.yummy.dto.UserLoginDTO;
import com.yummy.entity.User;
import com.yummy.exception.LoginFailedException;
import com.yummy.exception.UserNotLoginException;
import com.yummy.mapper.UserMapper;
import com.yummy.properties.WeChatProperties;
import com.yummy.service.UserService;
import com.yummy.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    private static final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //1. request openid from wechat server
        String openid = getOpenid(userLoginDTO.getCode());
        //if openid is null, throw exception, use self-defined exception
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //2. check if user exist
        User user = userMapper.getByOpenid(openid);
        //3. if not exist, insert into user table
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //4. return user
        return user;
    }

    private String getOpenid(String code){
        //use http client to request openid from wechat server
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        // return a json string
        String json = HttpClientUtil.doGet(WX_URL, paramMap);

        //use fast json to parse into object and get openid
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");

        return openid;
    }
}
