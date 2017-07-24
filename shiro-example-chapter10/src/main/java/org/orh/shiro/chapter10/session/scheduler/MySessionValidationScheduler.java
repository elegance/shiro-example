package org.orh.shiro.chapter10.session.scheduler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.orh.shiro.chapter10.JdbcTemplateUtils;
import org.orh.shiro.chapter10.SerializableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;

public class MySessionValidationScheduler implements SessionValidationScheduler, Runnable {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    private static final Logger log = LoggerFactory.getLogger(MySessionValidationScheduler.class);

    ValidatingSessionManager sessionManager;

    ScheduledExecutorService scheduledExecutorService;

    private long interval = DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;

    private boolean enable = false;

    public ValidatingSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        log.info("Executing session validation...");

        long startTime = System.currentTimeMillis();

        String sql = "select session from sessions limit ?,?";
        int start = 0; // 起始记录
        int size = 20; // 每页大小
        List<String> sessionList = jdbcTemplate.queryForList(sql, String.class, start, size);

        while (sessionList.size() > 0) {
            for (String sessionStr : sessionList) {
                try {
                    Session session = SerializableUtils.deserialize(sessionStr);
                    Method validateMethod =
                            ReflectionUtils.findMethod(AbstractValidatingSessionManager.class, "validate", Session.class, SessionKey.class);
                    validateMethod.setAccessible(true);
                    ReflectionUtils.invokeMethod(validateMethod, sessionManager, session, new DefaultSessionKey(session.getId()));
                } catch (Exception e) {
                    // ignore
                }
            }
            start = start + size;
            sessionList = jdbcTemplate.queryForList(sql, String.class, start, size);
        }
        long stopTime = System.currentTimeMillis();
        log.info("Session validation completed successfully in " + (stopTime - startTime) + " milliseconds.");
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @Override
    public void enableSessionValidation() {
        if (this.interval > 0l) {
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });
            this.scheduledExecutorService.scheduleAtFixedRate(this, interval, interval, TimeUnit.MILLISECONDS);
            this.enable = true;
        }
    }

    @Override
    public void disableSessionValidation() {
        this.scheduledExecutorService.shutdown();
        this.enable = true;
    }

}
