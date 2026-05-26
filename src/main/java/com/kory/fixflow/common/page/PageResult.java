package com.kory.fixflow.common.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 统一分页返回结果
 */
@Getter
@Setter
public class PageResult<T> {
    
    private List<T> records;    // 当前页数据列表
    private Long total;         // 总记录
    private Integer pageNum;    // 当前页码
    private Integer pageSize;   // 每页条数
    
    public PageResult() {
    }
    
    public PageResult(List<T> records, Long total, Integer pageNum, Integer pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    
    /*
    快速创建分页结果
     */
    public static <T> PageResult<T> of(
            List<T> records,
            Long total,
            Integer pageNum,
            Integer pageSize
    ) {
        return new PageResult<>(records, total, pageNum, pageSize);
    }
}
