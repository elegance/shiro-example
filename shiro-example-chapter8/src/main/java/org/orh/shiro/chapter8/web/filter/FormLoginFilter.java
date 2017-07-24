package org.orh.shiro.chapter8.web.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

public class FormLoginFilter extends PathMatchingFilter {
    private String loginUrl = "/login.jsp";
    private String successUrl = "/";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return true; // 已经登录认证过
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (isLoginRequest(req)) {
            if ("post".equalsIgnoreCase(req.getMethod())) { // form 表单提交
                boolean loginSuccess = login(req); // 登录
                if (loginSuccess) {
                    return redirectToSuccessUrl(req, resp);
                }
            }
            return true; // 继续过滤器链
        } else { // 保存当前地址并重定向到登录界面
            saveRequestAndRedirectToLogin(req, resp);
            return false;
        }
    }

    private boolean redirectToSuccessUrl(HttpServletRequest req, HttpServletResponse res) throws IOException {
        WebUtils.redirectToSavedRequest(req, res, successUrl);
        return false;
    }

    public void saveRequestAndRedirectToLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        WebUtils.saveRequest(req);
        WebUtils.issueRedirect(req, res, loginUrl);
    }

    private boolean login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (Exception e) {
            request.setAttribute("shiroLoginFailure", e.getClass());
            return false;
        }
        return true;
    }

    private boolean isLoginRequest(HttpServletRequest req) {
        return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(req));
    }

}
