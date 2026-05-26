package com.kory.fixflow.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * 新增用户请求 DTO
 */
@Data
public class CreateUserDTO {
    /*
    账户
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4到20个字符之间")
    private String username;
    
    /*
    登录密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20个字符之间")
    private String password;
    
    /*
    真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 20, message = "真实姓名长度必须在2到20个字符之间")
    private String realName;
    
    /*
    手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /*
    邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /*
    所属部门ID
     */
    private Long departmentId;
    
    /*
    角色ID列表
     */
    @NotEmpty(message = "用户角色不能为空")
    private List<Long> roleIds;
}
