package cc.wangweiye.dynamic_datasource;

import cc.wangweiye.dynamic_datasource.domain.DeptInfo;
import cc.wangweiye.dynamic_datasource.domain.UserInfo;
import cc.wangweiye.dynamic_datasource.his.HisService;
import cc.wangweiye.dynamic_datasource.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class DynamicDatasourceApplicationTests {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HisService hisService;

    @Test
    @Transactional
    void contextLoads() {
        List<DeptInfo> deptInfos = hisService.list();
        List<UserInfo> userInfos = userInfoService.list();
        System.out.println(deptInfos.size());
        System.out.println(userInfos.size());
    }
}
