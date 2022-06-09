package cc.wangweiye.dynamic_datasource.dao;


import cc.wangweiye.dynamic_datasource.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    @Select("select * from sys_user")
    List<UserInfo> selectList();
}
