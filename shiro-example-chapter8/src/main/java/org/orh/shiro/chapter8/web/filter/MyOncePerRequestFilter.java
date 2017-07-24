package org.orh.shiro.chapter8.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

/**
 * OncePerRequestFilter 保证一次请求只调用一次doFilter
 *
 */
public class MyOncePerRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("================== once per request filter");
        chain.doFilter(request, response);
    }

}
