package com.yummy.mapper;

import com.yummy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);


    void insert(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    @Select("select count(*) from user where create_time < #{endTime} ")
    Integer getTotalUserCount(LocalDateTime endTime);

    @Select("select count(*) from user where create_time > #{beginTime} and create_time < #{endTime} ")
    Integer getNewUserCount(LocalDateTime beginTime, LocalDateTime endTime);
}
