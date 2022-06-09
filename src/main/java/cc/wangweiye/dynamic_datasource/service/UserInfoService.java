package cc.wangweiye.dynamic_datasource.service;

import cc.wangweiye.dynamic_datasource.dao.UserInfoMapper;
import cc.wangweiye.dynamic_datasource.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    public List<UserInfo> list() {
        return userInfoMapper.selectList();
    }
}
