package cc.wangweiye.dynamic_datasource.his;

import cc.wangweiye.dynamic_datasource.annotation.SwitchSource;
import cc.wangweiye.dynamic_datasource.dao.HisDeptInfoMapper;
import cc.wangweiye.dynamic_datasource.domain.DeptInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HisService {
    @Autowired
    private HisDeptInfoMapper hisDeptInfoMapper;

    //不开启事务
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //切换到HIS的数据源
    @SwitchSource
    public List<DeptInfo> list() {
        return hisDeptInfoMapper.listDept();
    }
}
