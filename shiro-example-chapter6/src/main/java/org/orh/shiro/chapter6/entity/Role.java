package org.orh.shiro.chapter6.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String role;
    private String description;
    @Builder.Default
    private Boolean available = Boolean.TRUE;
}
