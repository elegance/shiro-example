package org.orh.shiro.chapter5.hash;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.junit.Test;

public class PasswordTest implements BaseTest {

    @Test
    public void testPasswordServiceWithRealm() {
        login("classpath:shiro-passwordservice.ini", "wu", "123");
        System.out.println(subject().isAuthenticated());
    }

    @Test
    public void testPasswordServiceWithJdbcRealm() {
        login("classpath:shiro-jdbc-passwordservice.ini", "wu", "123");
        System.out.println(subject().isAuthenticated());
    }

    @Test
    public void testGeneratePassword() {
        String algorithmName = "md5";
        String username = "liu";
        String password = "123";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;

        SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        String encodedPassword = hash.toHex();
        System.out.println(salt2);
        System.out.println(encodedPassword);
    }

    @Test
    public void testHashedCredentialsMatcherWithMyRealm2() {
        login("classpath:shiro-hashedCredentialsMatcher.ini", "wu", "123");
        System.out.println(subject().isAuthenticated());
    }

    @Test
    public void testHashedCredentialsMatcherWithJdbcRealm() {
        // 如果是 shiro 1.4+ 的话，通过 BeanUtilsBean 设置将会无效哟，因为 ReflectionBuilder 的构造函数中 ：beanUtilsBean = new BeanUtilsBean();  存储了这个 beanUtilsBan
        // 因为这个问题，将 shiro 改回了 1.3.2
        BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);
        Object rs = BeanUtilsBean.getInstance().getConvertUtils().convert("COLUMN", JdbcRealm.SaltStyle.class);
        System.out.println(rs.getClass());

        login("classpath:shiro-jdbc-hashedCredentialsMatcher.ini", "liu", "123");
        System.out.println(subject().isAuthenticated());
    }

    private class EnumConverter extends AbstractConverter {
        @Override
        protected String convertToString(final Object value) throws Throwable {
            return ((Enum) value).name();
        }

        @Override
        protected Object convertToType(final Class type, final Object value) throws Throwable {
            return Enum.valueOf(type, value.toString());
        }

        @Override
        protected Class getDefaultType() {
            return null;
        }
    }
    
    @Test(expected = ExcessiveAttemptsException.class)
    public void testRetryLimitHasedCredentialsMatcherWithMyRealm() {
        // 先用错误的密码试错5次
        for (int i = 1; i <= 5; i++) {
            try {
                login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "234");
            } catch (Exception e) { // ignore
            }
        }
        login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "liu", "123");
    }
}
