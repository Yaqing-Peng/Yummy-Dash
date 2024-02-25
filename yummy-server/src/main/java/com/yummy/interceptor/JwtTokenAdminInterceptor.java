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
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

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
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、verify token
        try {
            log.info("jwt verification:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            //set cur emp id in threadLocal
            BaseContext.setCurrentId(empId);
            log.info("cur emp id：", empId);
            //3. success, release
            return true;
        } catch (Exception ex) {
            //4. fail, return 401
            response.setStatus(401);
            return false;
        }
    }
}
