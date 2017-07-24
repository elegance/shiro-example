package org.orh.shiro.chapter8.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.AdviceFilter;

public class MyAdviceFilter extends AdviceFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("====预处理/前置处理");
        return true; // 返回 false将中断后续拦截器的执行
    }

    /**
     * 执行完拦截器链之后正常返回执行
     */
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("====后处理/后置返回处理");
    }

    /**
     * 不管是否有异常，afterCompletion 都会执行，如清理资源的功能
     */
    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        System.out.println("完成处理/后置最终处理");
    }
}
