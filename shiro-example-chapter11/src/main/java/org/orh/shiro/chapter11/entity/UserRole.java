package org.orh.shiro.chapter11.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;
}
