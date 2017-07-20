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