# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 技术栈

Java 21 / Spring Boot 3.5.14 / Spring Security + JWT (JJWT 0.13.0) / MyBatis (XML Mapper) / MySQL / Lombok / Maven

## 构建命令

```bash
./mvnw clean package -DskipTests   # 构建
./mvnw spring-boot:run              # 启动（8080端口，需本地MySQL fixflow库）
./mvnw test                         # 运行全部测试
./mvnw test -Dtest=ClassName        # 运行单个测试类
./mvnw test -Dtest=ClassName#method # 运行单个测试方法
```

## 架构概述

资产管理和报修工单系统。根包 `com.kory.fixflow`，按业务模块分层组织。

**业务模块**：auth(/auth)、user(/users)、role(/roles)、department(/departments)、asset(/assets)、assetcategory(/asset-categories)、order(/orders)、log(/operation-logs)

**通用层** (`common/`)：`Result<T>` 响应包装、`PageResult<T>` 分页、`BusinessException` 业务异常、`GlobalExceptionHandler` 全局异常处理、`JwtUtil` JWT 工具

**安全层** (`security/` + `config/SecurityConfig.java`)：JWT 无状态认证，仅 `/auth/login` 允许匿名访问，其他接口必须认证。通过 `SecurityUtil.getCurrentUserId()` 获取当前登录用户 ID

**操作日志** (`log/`)：基于 AOP 的操作日志记录，在 Controller 方法上添加 `@OperationLogAnnotation(module="xxx", operation="xxx")` 注解即可自动记录请求信息、操作人、耗时

**角色体系**：`RoleCodeEnum` 定义三个角色 — `ADMIN`（管理员）、`REPAIRER`（维修人员）、`USER`（普通用户），通过 `user_role` 关联表管理

## 模块分层模式

每个业务模块统一遵循以下分层结构：

```
module/
  controller/   # @RestController + @RequiredArgsConstructor，返回 Result<T>
  dto/          # 请求参数对象，使用 Jakarta Validation 校验
  entity/       # 数据库实体，@Data 注解
  mapper/       # 数据访问接口，@Mapper 注解
  service/      # 接口定义
  service/impl/ # 接口实现，@Service + @RequiredArgsConstructor
  vo/           # 视图对象，返回给前端
```

Service 层负责校验外键存在性和业务状态，校验失败抛 `BusinessException`。

## 数据库约定

- **表名**：用户表为 `sys_user`，其他表与模块名一致（asset, department, asset_category, repair_order, role, user_role, operation_log）
- **字段命名**：数据库下划线，Java 驼峰，MyBatis 自动转换（`map-underscore-to-camel-case: true`）
- **主键**：自增 Long，INSERT 使用 `useGeneratedKeys="true" keyProperty="id"` 回填
- **逻辑删除**：所有表使用 `is_deleted` 字段（0=正常, 1=删除），查询必须加 `is_deleted = 0`
- **时间字段**：`create_time`（创建时间）、`update_time`（更新时间）

## MyBatis 规范

- Mapper 接口使用 `@Mapper` 注解
- XML 文件位于 `src/main/resources/mapper/` 目录
- 分页查询拆成两条 SQL：`selectXxxPageList`（返回列表）+ `selectXxxPageCount`（返回总数）
- 查询条件使用 `<where>` + `<if test="...">` 动态 SQL
- 关联查询使用 LEFT JOIN，关联表需加 `AND is_deleted = 0` 条件
- 模糊查询使用 `LIKE CONCAT('%', #{keyword}, '%')`

## API 设计约定

| 操作 | HTTP 方法 | URL 模式 | 返回值 |
|------|-----------|----------|--------|
| 分页查询 | GET | `/xxx/page` | `Result<PageResult<XxxPageVO>>` |
| 详情查询 | GET | `/xxx/{id}` | `Result<XxxDetailVO>` |
| 新增 | POST | `/xxx` | `Result<Long>`（返回新增记录 ID） |
| 修改 | PUT | `/xxx/{id}` | `Result<Void>` |
| 删除 | DELETE | `/xxx/{id}` | `Result<Void>` |

分页参数通过 DTO 接收（包含 `pageNum`、`pageSize` 和业务查询条件），DTO 中需计算 `offset` 属性供 SQL LIMIT 使用。

## MyBatis Mapper 参数传递约定

- 单个 DTO 参数：SQL 中直接用字段名 `#{pageNum}`、`#{offset}`
- DTO 作为方法的第二个参数：SQL 中用 `#{queryDTO.xxx}` 前缀访问（如 `#{queryDTO.offset}`）
- 当前登录用户 ID 作为方法第一个参数时用 `#{currentUserId}`

## 工单模块复杂操作模式（order 模块）

order 模块超出标准 CRUD，包含带子资源的操作路径：

```
PUT  /{id}/reject        # 驳回
PUT  /{id}/assign        # 派发
PUT  /{id}/accept        # 接单
PUT  /{id}/confirm       # 确认完成
POST /{id}/evaluation    # 评价（新增子资源）
GET  /{id}/evaluation    # 查询评价
```

按角色区分分页查询路径：
- `/my/page` — 当前用户提交的工单
- `/my/wait-confirm/page` — 当前用户待确认工单
- `/admin/pending/page` — 管理员待受理工单
- `/repairer/wait-receive/page` — 维修人员待接单工单

Service 层状态操作必须校验：
1. 工单存在性（`selectById` + null 判断）
2. 当前状态是否允许该操作（枚举比对）
3. 操作人身份权限（`currentUserId` 比对 `assigneeId` 或 `applicantId`）

## 响应码规范

| 码值 | 含义 | 使用场景 |
|------|------|----------|
| 200 | 成功 | 操作成功 |
| 400 | 参数错误 | 请求参数校验失败 |
| 401 | 未登录 | 未认证或 token 过期 |
| 403 | 无权限 | 权限不足 |
| 404 | 资源不存在 | 请求的资源未找到 |
| 5001 | 业务异常 | 业务规则校验失败 |
| 500 | 系统异常 | 服务器内部错误 |

## 工单状态流转（order 模块）

```
待受理(0) ──受理──→ 待接单(2)
   │                  │
   └──驳回──→ 已驳回(1)  └──接单──→ 处理中(3)
                                      │
                                      └──完成──→ 待确认(4)
                                                   │
                                                   └──确认──→ 已完成(5)
                                                                │
                                                                └──评价──→ 已评价(6)
```
