package org.orh.shiro.chapter8.web.filter;

import java.util.Arrays;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;

public class MyPathMatchingFilter extends PathMatchingFilter {
    
    // preHandle: 会进行url模式与请求url进行匹配，如果匹配就会调用 onPreHandler；如果没有配置或没有匹配，则会返回false
    
    // onPreHandle: 如果url与请求url匹配，就会执行 onPreHandle，并把该拦截器配置的参数传入。

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("url matches, config is " + Arrays.toString((String[]) mappedValue));
        return true;
    }
}
