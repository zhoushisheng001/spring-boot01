package com.zhuguang.zhou.common.autoconfigure;

import com.zhuguang.zhou.common.mybatis.CommonXMLMapperBuilder;
import com.zhuguang.zhou.common.mybatis.KeyGenMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

/**
 * 基本服务配置
 * @author liheng
 * @since 1.0
 *
 */
@Configuration
@ConditionalOnMissingClass("ShardingDatabaseAutoConfiguration")
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DatabaseAutoConfiguration {
	@Autowired
	private MybatisProperties properties;
	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	@Value("${mybatis.keyGenMode:IDENTITY}")
	private String keyGenMode;

	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		CommonXMLMapperBuilder builder = new CommonXMLMapperBuilder();
		builder.setBaseResultMap("BaseResultMap");
		builder.setBaseTableName("BaseTable");
		builder.setGenerationType("GenerationType");
		builder.setBaseColumns("BaseColumns");

		builder.setKeyGenMode(KeyGenMode.parse(StringUtils.defaultString(keyGenMode, KeyGenMode.IDENTITY.getCode())));
       // String schema = dataSource.getConnection().getCatalog();
        // 加载分片字段
        //RuleMapperMethodGenerator.initRule(schema);

		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		if(this.properties.getConfigLocation()!=null){
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		factory.setMapperLocations(builder.builderCommonMapper(this.properties.resolveMapperLocations()));
//		factory.setMapperLocations(this.properties.resolveMapperLocations());

		return factory.getObject();
	}
	
}
