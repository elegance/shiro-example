package org.orh.shiro.chapter12.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 项目采用spring-jdbc 的 jdbcTemplate 进行数据库操作
 * spring-jdbc文档请参考：https://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html
 */
@Configuration
public class JdbcTemplateConfiguration {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:file:~/.h2/shiro-chapter12");
        ds.setValidationQuery("select 1");

        return new JdbcTemplate(ds);
    }
}
