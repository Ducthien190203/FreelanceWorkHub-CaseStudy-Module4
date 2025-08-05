package vn.codegym.freelanceworkhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "vn.codegym.freelanceworkhub") // Quét toàn bộ beans
@PropertySource("classpath:db.properties") // Tách cấu hình DB ra file riêng
public class AppConfig {

    // 1. DataSource - cấu hình kết nối DB
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/freelanceworkhub?useSSL=false&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("haianh04"); // đổi theo máy bạn
        return dataSource;
    }

    // 2. EntityManagerFactory - cấu hình JPA/Hibernate
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("vn.codegym.freelanceworkhub.model");

        emf.setJpaProperties(hibernateProperties());
        emf.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        return emf;
    }

    // 3. TransactionManager
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

    // 4. Thuộc tính Hibernate
    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.setProperty("hibernate.show_sql", "true");
        return props;
    }
}
