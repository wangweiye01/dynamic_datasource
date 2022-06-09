package cc.wangweiye.dynamic_datasource.dao;

import cc.wangweiye.dynamic_datasource.domain.DeptInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HisDeptInfoMapper {
    @Select("select * from sys_user")
    List<DeptInfo> listDept();
}
