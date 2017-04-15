package com.test.security;

import org.apache.log4j.Logger;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;

/**
 *
 * 对受保护对象的访问进行拦截的抽象类
 * 权限管理 过滤器
 */
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    private static final Logger logger = Logger.getLogger(CustomFilterSecurityInterceptor.class);

    /**
     * 获取与受保护对象关联的ConfigAttribute集合
     */
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    /**
     * Filter 接口 作用around advice，通过它我们可以控制是否要执行方法、是否要修改方法的返回值，以及是否要抛出异常。
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 正在请求的受保护的对象，基本上来说是MethodInvocation（使用AOP）、JoinPoint（使用Aspectj）
        // 和FilterInvocation（Web请求）三种类型
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        logger.debug("===="+fi.getRequestUrl());
        invoke(fi);
    }

    /**
     * 用于调用受保护的方法
     * @param fi
     * @throws IOException
     * @throws ServletException
     */
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        // 正在请求调用的受保护对象传递给beforeInvocation()方法进行权限鉴定
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public Class<? extends Object> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(
            FilterInvocationSecurityMetadataSource smSource) {
        this.securityMetadataSource = smSource;
    }

    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
