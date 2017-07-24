package org.orh.shiro.chapter8.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

public class AnyRolesFilter extends AccessControlFilter {
    private String unauthorizedUrl = "/unauthorized.jsp";

    private String loginUrl = "login.jsp";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String[] roles = (String[]) mappedValue;
        if (roles == null) {
            return true;
        }
        for (String role : roles) {
            if (getSubject(request, response).hasRole(role)) {
                return true;
            }
        }
        return false; // 跳到onAccessDenied处理
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) { // 表示没有登录，重定向登录页面
            saveRequest(request);
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
    }

}
