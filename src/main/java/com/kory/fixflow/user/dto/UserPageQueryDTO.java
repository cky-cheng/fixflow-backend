package com.kory.fixflow.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户分页查询参数 DTO
 */
@Data
public class UserPageQueryDTO {
    
    /*
    当前页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于 1")
    private Integer pageNum = 1;

    /*
    每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数不能小于 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize = 5;
    
    private String username;    // 用户名  模糊查询
    
    private String phone;       // 手机号  模糊查询
    
    private Integer status;     // 用户状态
    
    /*
    分页偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
