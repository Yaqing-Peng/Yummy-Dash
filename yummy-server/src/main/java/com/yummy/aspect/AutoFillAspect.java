package com.yummy.aspect;

import com.yummy.annotation.AutoFill;
import com.yummy.enumeration.OperationType;
import com.yummy.context.BaseContext;
import com.yummy.constant.AutoFillConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * cut point
     */
    @Pointcut("execution(* com.yummy.mapper.*.*(..)) && @annotation(com.yummy.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * fill common fields before cut points
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("Start filling common fields...");

        //1.Get methods op type
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//get method signature
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//get annotation
        OperationType operationType = autoFill.value();//get sql op type

        //2.Get methods para: entity
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];//set default to be the first one

        //3.Prepare auto fill data
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //4.Fill the fields based on op type
        if(operationType == OperationType.INSERT){
            //get methods object
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //use reflection to do auto fill
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
