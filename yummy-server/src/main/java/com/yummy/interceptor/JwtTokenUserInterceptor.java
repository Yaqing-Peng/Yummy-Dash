package com.yummy.interceptor;

import com.yummy.constant.JwtClaimsConstant;
import com.yummy.context.BaseContext;
import com.yummy.properties.JwtProperties;
import com.yummy.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt token interceptor
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * verify jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //check if we intercept a Controller method
        if (!(handler instanceof HandlerMethod)) {
            //if not, release
            return true;
        }

        //1、get token from request header
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、verify token
        try {
            log.info("jwt verification:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            //set cur emp id in threadLocal
            BaseContext.setCurrentId(userId);
            log.info("cur user id：", userId);
            //3. success, release
            return true;
        } catch (Exception ex) {
            //4. fail, return 401
            response.setStatus(401);
            return false;
        }
    }
}
