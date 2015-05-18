package com.hmkcode.spring.mybatis;  

import java.io.IOException;
import java.sql.Driver;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hmkcode.spring.mybatis.service.Service;
  
@PropertySource(name="jdbc.properties", value="classpath:/jdbc.properties")
@Configuration 
@EnableTransactionManagement
@MapperScan("com.hmkcode.spring.mybatis.mapper") // Mapper 파일 위치
public class AppConfig {  
	
	@Value("classpath:db-schema.sql")
	private Resource schemaScript;

	@Value("classpath:db-test-data.sql")
	private Resource dataScript;
	
	@Autowired
    private Environment env;

	@SuppressWarnings("unchecked")
	@Bean
	public SimpleDriverDataSource getDataSource() {
		
		SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
//		simpleDriverDataSource.setDriverClass(JDBCDriver.class);
//		simpleDriverDataSource.setUrl("jdbc:hsqldb:mem:testdb");
//		simpleDriverDataSource.setUsername("");
//		simpleDriverDataSource.setPassword("");
		try {
			System.out.println(env.getProperty("jdbc.driverClassName"));
			simpleDriverDataSource.setDriverClass((Class<Driver>)Class.forName(env.getProperty("jdbc.driverClassName")));
			simpleDriverDataSource.setUsername(env.getProperty("jdbc.username"));
			simpleDriverDataSource.setPassword(env.getProperty("jdbc.password"));
			simpleDriverDataSource.setUrl(env.getProperty("jdbc.url"));
		} catch ( ClassNotFoundException e ) {
			
		}
		
		return simpleDriverDataSource;
	}
	
	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
	    final DataSourceInitializer initializer = new DataSourceInitializer();
	    initializer.setDataSource(dataSource);
	    initializer.setDatabasePopulator(databasePopulator());
	    return initializer;
	}

	private DatabasePopulator databasePopulator() {
	    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	    populator.addScript(schemaScript);
	    populator.addScript(dataScript);
	    return populator;
	}
	
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate(DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean getSqlSessionFactory(DataSource ds) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(ds);
		
//		bean.setConfigLocation(new ClassPathResource("/mybatis-config.xml"));
		bean.setTypeAliasesPackage("com.hmkcode.spring.mybatis.vo");
		
		String searchingPackages = "classpath:com/hmkcode/spring/mybatis/testmapper/**/*.xml";
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resourceResolver.getResources(searchingPackages);
			bean.setMapperLocations(resources);
			return bean;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
//	@Bean(name = "mapper")
//	public MapperFactoryBean<Mapper> getMapper(DataSource ds, SqlSessionFactoryBean factory) {
//		MapperFactoryBean<Mapper> mapper = new MapperFactoryBean<Mapper>();
//		try {
//			mapper.setMapperInterface(Mapper.class);
//			mapper.setSqlSessionFactory(factory.getObject());
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//		return mapper;
//	}
	
	@Bean(name = "service")
	public Service getService() {
		return new Service();
	}
	
//    @Bean(name = "dataSource")
//    public DataSource getDataSource(){
//        DataSource dataSource = createDataSource();
//        DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource);
//        return dataSource;
//    }
//
//    private DatabasePopulator createDatabasePopulator() {
//        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//        databasePopulator.setContinueOnError(true);
//        databasePopulator.addScript(new ClassPathResource("schema.sql"));
//        return databasePopulator;
//    }
//
//    private SimpleDriverDataSource createDataSource() {
//        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
//        simpleDriverDataSource.setDriverClass(org.h2.Driver.class);
//        simpleDriverDataSource.setUrl("jdbc:h2:target/database/example;AUTO_RECONNECT=TRUE");
//        simpleDriverDataSource.setUsername("");
//        simpleDriverDataSource.setPassword("");
//        return simpleDriverDataSource;      
//    }
}  
 