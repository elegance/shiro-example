package org.orh.shiro.chapter12.web.controller;

import org.apache.shiro.SecurityUtils;
import org.orh.shiro.chapter12.web.bean.RetMsg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/user/test")
    public RetMsg userTest() {
        return RetMsg.success(SecurityUtils.getSubject().getPrincipal());
    }
}
