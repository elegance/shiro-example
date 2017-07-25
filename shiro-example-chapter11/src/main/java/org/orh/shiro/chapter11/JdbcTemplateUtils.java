package org.orh.shiro.chapter11;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 项目采用spring-jdbc 的 jdbcTemplate 进行数据库操作
 * spring-jdbc文档请参考：https://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html
 */
public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate jdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }
        return jdbcTemplate;
    }

    private static JdbcTemplate createJdbcTemplate() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:file:~/.h2/shiro-chapter6");
        ds.setValidationQuery("select 1");

        return new JdbcTemplate(ds);
    }
}
