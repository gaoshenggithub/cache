package com.gaosheng.cache.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }


    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet()
                , "/druid/*");
        Map<String,String> initParatmeters = new HashMap<String,String>();
        initParatmeters.put("loginUsername","admin");
        initParatmeters.put("loginPassword","123456");
        initParatmeters.put("allow","");
        initParatmeters.put("deny","192.168.0.106");
        bean.setInitParameters(initParatmeters);
        return bean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        bean.setUrlPatterns(Arrays.asList("/"));
        Map<String,String> initParatmeters = new HashMap<String,String>();
        initParatmeters.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(initParatmeters);
        return bean;

    }
}
