## shiro-example

----

#### [第二章-身份认证](shiro-example-chapter2)

* [`.ini` 配置realm、JDBCRealm、配置多个Realm](shiro-example-chapter2/src/test/java/org/orh/shiro/chapter2/LoginLogoutTest.java)
* [自定义`authenticator.authenticationStrategy` 认证器的认证策略](shiro-example-chapter2/src/test/java/org/orh/shiro/chapter2/AuthenticatorTest.java)


#### [第三章-授权](shiro-example-chapter3)

* [基于角色的访问控制（隐式角色）](shiro-example-chapter3/src/test/java/org/orh/shiro/chapter3/RoleTest.java)
* [基于资源(权限)的访问控制（显示角色）](shiro-example-chapter3/src/test/java/org/orh/shiro/chapter3/PermissionTest.java)
* [Authorizer、PermissionResolver、RolePermissionResolver-自定义扩展](shiro-example-chapter3/src/test/java/org/orh/shiro/chapter3/AuthorizerTest.java)

#### [第四章-INI配置](shiro-example-chapter4)

* [纯java代码-无配置式](shiro-example-chapter4/src/test/java/org/orh/shiro/chapter4/NonConfigurationCreateTest.java)
* [ini-配置式](shiro-example-chapter4/src/test/java/org/orh/shiro/chapter4/ConfigurationCreateTest.java)
* [ini-几种类型值的注入方式](shiro-example-chapter4/src/test/java/org/orh/shiro/chapter4/IniMainTest.java)

#### [第五章-编码/加密](shiro-example-chapter5)
* [base64编码、Hex编码、字符编码，MD5、SHA-X HASH摘要算法，对称加密与解密](shiro-example-chapter5/src/test/java/org/orh/shiro/chapter5/hash/CodecAndCryptoTest.java)
* [passwordService、passwordMatcher密码加密与验证，saltStyle的saltStyle枚举转换问题，credentialsMatcher-实现密码验证服务，密码重试次数限定](shiro-example-chapter5/src/test/java/org/orh/shiro/chapter5/hash/PasswordTest.java)
* [ehcache3-的编程式配置、三层存储、TTI与TTL测试](shiro-example-chapter5/src/test/java/org/orh/shiro/chapter5/hash/Ehcache3Test.java)

#### [第六章-Realm及相关对象](shiro-example-chapter6)
* [用户、角色、权限，用户-n角色、角色-n权限，lombok实体注解](shiro-example-chapter6/src/main/java/org/orh/shiro/chapter6/entity)
* [spring-jdbc jdbcTemplate dao中操作](shiro-example-chapter6/src/main/java/org/orh/shiro/chapter6/dao)
* [通过-UserRealm连接Shiro与UserService](shiro-example-chapter6/src/main/java/org/orh/shiro/chapter6/realm/UserRealm.java)
* [测试jdbc登录成功、用户名错误、密码错误、密码超出重试次数、有/没有角色、有/没有权限的测试](shiro-example-chapter6/src/test/java/org/orh/shiro/chapter6/realm/UserRealmTest.java)
* [多个realm时subject中principal信息](shiro-example-chapter6/src/test/java/org/orh/shiro/chapter6/realm/PrincipalCollectionTest.java)
* [用户、角色、权限的调整测试](shiro-example-chapter6/src/test/java/org/orh/shiro/chapter6/ServiceTest.java)

#### [第七章-与Web集成](shiro-example-chapter7)
* Run Command: `mvn jetty:run`，切换`web.xml`中的`shiroConfigLocations`注释，测试不同功能
* [shiro1.2+ web.xml方式的配置](/shiro-example-chapter7/src/main/webapp/WEB-INF/web.xml)
* [页面取值方式：shiro标签与request设置attribute subject](/shiro-example-chapter7/src/main/webapp/WEB-INF/jsp/loginSuccess.jsp)
* [普通登录-使用shiro.ini](/shiro-example-chapter7/src/main/java/org/orh/shiro/chapter7/web/servlet/LoginServlet.java)
* [Basic的拦截器身份-浏览器弹出登录-使用shiro-basicFilterLogin.ini](/shiro-example-chapter7/src/main/resources/shiro-basicFilterLogin.ini)
* [类似普通登录-简化-使用shiro-formFilterLogin.ini](/shiro-example-chapter7/src/main/resources/shiro-formFilterLogin.ini)

#### [第八章-拦截器机制](shiro-example-chapter8)
* [扩展OncePerRequestFilter-保证一次请求只执行一次doFilter](/shiro-example-chapter8/src/main/java/org/orh/shiro/chapter8/web/filter/MyOncePerRequestFilter.java)
* [扩展AdviceFilter](/shiro-example-chapter8/src/main/java/org/orh/shiro/chapter8/web/filter/MyAdviceFilter.java)
* [PathMatchingFilter 继承了 AdviceFilter，提供了 url 模式过滤的功能](/shiro-example-chapter8/src/main/java/org/orh/shiro/chapter8/web/filter/MyPathMatchingFilter.java)
* [AccessControlFilter 继承 PathMatchingFilter-提供isAccessAllowed与onAccessDenied](/shiro-example-chapter8/src/main/java/org/orh/shiro/chapter8/web/filter/MyAccessControlFilter.java)
* [基于表单登录拦截器](/shiro-example-chapter8/src/main/java/org/orh/shiro/chapter8/web/filter/FormLoginFilter.java)
* [任意角色拦截器](/shiro-example-chapter8/src/main/java/org/orh/shiro/chapter8/web/filter/AnyRolesFilter.java)