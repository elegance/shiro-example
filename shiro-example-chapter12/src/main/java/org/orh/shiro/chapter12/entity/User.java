package org.orh.shiro.chapter12.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户类-
 * 
 * 如果对以下的注解不理解，可以花上5分钟参考这里：https://blog.chengjf.com/t/16316/
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"password", "salt"})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String salt;

    @Builder.Default
    private Boolean locked = Boolean.FALSE;

    public String getCredentialsSalt() {
        return username + salt;
    }
}
