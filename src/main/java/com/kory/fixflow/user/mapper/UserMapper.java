package com.kory.fixflow.user.mapper;

import com.kory.fixflow.user.dto.UpdateUserStatusDTO;
import com.kory.fixflow.user.dto.UserPageQueryDTO;
import com.kory.fixflow.user.entity.User;
import com.kory.fixflow.user.vo.UserDetailVO;
import com.kory.fixflow.user.vo.UserPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {
    
    /*
    查询用户分页列表数据
     */
    List<UserPageVO> selectUserPageList(UserPageQueryDTO queryDTO);
    
    /*
    查询符合条件的用户总数
     */
    Long selectUserPageCount(UserPageQueryDTO queryDTO);
    
    
    /*
    根据用户名查询用户
     */
    User selectByUsername(String username);
    
    /*
    新增用户
     */
    int insertUser(User user);
    
    /*
    根据 ID 查询用户
     */
    User selectById(Long id);
    
    /*
    修改用户基础信息
     */
    int updateUser(User user);
    
    /*
    逻辑删除用户
     */
    int deleteUserById(Long id);
    
    /**
     * 修改用户状态
     * @param id 用户ID
     * @param status 用户状态
     * @return 返回修改行数
     */
    int updateUserStatus(@Param("id") Long id,
                         @Param("status") Integer status
                         );
    
    /**
     * 查询用户详情
     *
     * @param id 用户ID
     * @return 返回用户详情VO
     */
    UserDetailVO selectUserDetailById(Long id);
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @param password 用户密码
     * @return 受影响行数
     */
    int updatePassword(@Param("id") Long id,
                       @Param("password") String password
                       );
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户实体
     */
    User selectByPhone(String phone);
    
    /**
     * 根据用户名查询可登录用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    User selectLoginUserByUsername(String username);
    
    
}
