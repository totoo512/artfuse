package com.zyy.mapper;

import com.zyy.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    @Insert("insert into user(user_id, username, phone, openid, create_time) " +
            "values(#{userId}, #{username}, #{phone}, #{openid}, #{createTime})")
    void insert(User user);
}
