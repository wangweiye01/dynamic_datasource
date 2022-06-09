package cc.wangweiye.dynamic_datasource.config;

import cc.wangweiye.dynamic_datasource.annotation.SwitchSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan({"cc.wangweiye.dynamic_datasource.dao"})
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * @Bean：向IOC容器中注入一个Bean
     * @ConfigurationProperties：使得配置文件中以spring.datasource为前缀的属性映射到Bean的属性中
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("dataSource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    /**
     * 向IOC容器中注入另外一个数据源
     * 全局配置文件中前缀是spring.datasource.his
     */
    @Bean(name = SwitchSource.DEFAULT_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.his")
    public DataSource hisDataSource() {
        return new DruidDataSource();
        // return DataSourceBuilder.create().build();
    }

    /**
     * 构建一个动态数据源，用以数据源的切换
     *
     * @param dataSource
     * @param hisDataSource
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("dataSource") DataSource dataSource, @Qualifier(SwitchSource.DEFAULT_NAME) DataSource hisDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        //放置目标数据源
        targetDataSources.put(SwitchSource.DEFAULT_NAME, hisDataSource);
        //构建动态数据源
        return new DynamicDataSource(dataSource, targetDataSources);
    }

    /**
     * 创建动态数据源的SqlSessionFactory，传入的是动态数据源
     *
     * @Primary这个注解很重要，如果项目中存在多个SqlSessionFactory，这个注解一定要加上
     */
    @Primary
    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 自动将数据库中的下划线转换为驼峰格式
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultFetchSize(100);
        configuration.setDefaultStatementTimeout(30);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 重写事务管理器，管理动态数据源
     */
    @Primary
    @Bean(value = "transactionManager2")
    public PlatformTransactionManager annotationDrivenTransactionManager(DynamicDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
