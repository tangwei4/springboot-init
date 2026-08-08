package com.springbootinit.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 监控配置
 */
@Configuration
public class DruidMonitorConfig {

    /**
     * 注册 Druid 监控 Servlet
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // 登录用户名密码
        bean.addInitParameter("loginUsername", "admin");
        bean.addInitParameter("loginPassword", "admin");
        // IP 白名单（空表示允许所有）
        bean.addInitParameter("allow", "");
        // IP 黑名单
        bean.addInitParameter("deny", "");
        // 禁用重置按钮
        bean.addInitParameter("resetEnable", "false");

        return bean;
    }

    /**
     * 注册 Druid Web 监控过滤器
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        // 过滤所有请求
        bean.addUrlPatterns("/*");
        // 排除静态资源
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        // 开启 Session 统计
        bean.addInitParameter("sessionStatEnable", "true");
        // Session 最大数量
        bean.addInitParameter("sessionStatMaxCount", "1000");

        return bean;
    }
}
