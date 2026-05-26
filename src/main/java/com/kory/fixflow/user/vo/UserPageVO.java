package com.kory.fixflow.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户展示分页 VO
 */
@Data
public class UserPageVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    private LocalDateTime createTime;
}
