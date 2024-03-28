package spring.conf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:spring/db.properties")
@EnableTransactionManagement
//@MapperScan("user.dao")
public class SpringConfiguration {
	private @Value("${jdbc.driver}") String driver;
	private @Value("${jdbc.url}") String url;
	private @Value("${jdbc.username}") String username;
	private @Value("${jdbc.password}") String password;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public BasicDataSource dataSource(){
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(driver);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);
		
		return basicDataSource;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		//sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("spring/mybatis-config.xml"));
		
		//sqlSessionFactoryBean.setTypeAliasesPackage("user.bean");
		sqlSessionFactoryBean.setTypeAliasesPackage("*.bean");
				
		//sqlSessionFactoryBean.setMapperLocations(new ClassPathResource("mapper/userMapper.xml"));
		
		/*
		sqlSessionFactoryBean.setMapperLocations(
				new ClassPathResource("mapper/userMapper.xml"),
				new ClassPathResource("mapper/userUploadMapper.xml"));
		*/
		
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*Mapper.xml"));
		
		return sqlSessionFactoryBean.getObject(); //SqlSessionFactory 변환
	}
	
	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
		return sqlSessionTemplate;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager(){
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
		return dataSourceTransactionManager;
	}
}

/* 파라메터에 원하는 값을 입력하면 스프링이 던져준다.
@Bean
public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext, 
										   DataSource dataSource) throws Exception {
	SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	sqlSessionFactoryBean.setDataSource(dataSource);
	sqlSessionFactoryBean.setTypeAliasesPackage("user.bean");
	//sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("spring/mybatis-config.xml"));
	//sqlSessionFactoryBean.setMapperLocations(new ClassPathResource("mapper/userMapper.xml"));
	sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*Mapper.xml"));
	
	return sqlSessionFactoryBean.getObject();
}

@Bean
public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) throws Exception {
	SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
	return sqlSessionTemplate;
}

@Bean
public DataSourceTransactionManager transactionManager(DataSource dataSource){
	DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
	return dataSourceTransactionManager;
}
*/














