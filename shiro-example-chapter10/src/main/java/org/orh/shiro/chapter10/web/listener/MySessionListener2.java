package org.orh.shiro.chapter10.web.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

public class MySessionListener2 extends SessionListenerAdapter {

    @Override
    public void onStart(Session session) {// 会话创建时触发
        System.out.println("[listener2]会话创建：" + session.getId());
    }
}
