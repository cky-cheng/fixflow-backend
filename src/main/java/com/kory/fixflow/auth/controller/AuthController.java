package com.kory.fixflow.auth.controller;

import com.kory.fixflow.auth.dto.LoginDTO;
import com.kory.fixflow.auth.service.AuthService;
import com.kory.fixflow.auth.vo.CurrentUserVO;
import com.kory.fixflow.auth.vo.LoginResultVO;
import com.kory.fixflow.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;  // 认证业务层
    
    /**
     * 用户登录
     * @param loginDTO 登录请求 DTO
     * @return 统一结果
     */
    @PostMapping("/login")
    public Result<LoginResultVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResultVO loginResultVO = authService.login(loginDTO);
        return Result.success(loginResultVO);
    }
    
    /**
     * 获取当前登录用户信息
     * @return 当前用户信息
     */
    @GetMapping("/info")
    public Result<CurrentUserVO> getCurrentUserInfo() {
        CurrentUserVO currentUserVO = authService.getCurrentUserInfo();
        return Result.success(currentUserVO);
    }
}
