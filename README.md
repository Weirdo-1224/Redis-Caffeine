# Redis_Caffeine 项目概述

## 1. 项目架构与技术栈
### 核心框架
- **Spring Boot**: 3.5.3
- **持久层**: MyBatis + MySQL 8.0
- **缓存系统**: Redis + Caffeine 二级缓存
- **API风格**: RESTful
- **构建工具**: Maven
- **JDK版本**: 17 (推荐)

### 关键依赖
| 功能 | 依赖 |
|------|------|
| Web支持 | `spring-boot-starter-web` |
| 缓存抽象 | `spring-boot-starter-cache` |
| Redis客户端 | `spring-boot-starter-data-redis` (Lettuce) |
| 本地缓存 | Caffeine |
| ORM框架 | MyBatis |
| 数据库连接 | MySQL Connector |
| 简化代码 | Lombok |
| AOP支持 | `spring-boot-starter-aop` |

## 2. 项目结构
```
src/
├── main/
│   ├── java/com/example/redis_caffeine/
│   │   ├── config/          # 配置类
│   │   │   └── RedisConfig.java  # Redis序列化与连接配置
│   │   ├── controller/       # API控制器
│   │   │   └── OrderController.java  # 订单管理接口
│   │   ├── entity/           # 实体类
│   │   │   └── Order.java    # 订单实体
│   │   ├── mapper/           # MyBatis映射器
│   │   │   └── OrderMapper.java  # 订单数据访问
│   │   ├── service/          # 业务逻辑
│   │   │   ├── OrderService.java     # 订单服务接口
│   │   │   └── impl/                 # 服务实现
│   │   │       └── OrderServiceImpl.java  # 订单服务实现
│   │   └── RedisCaffeineApplication.java  # 应用入口
│   └── resources/
│       ├── application.yml   # 主配置文件
│       └── mapper/           # MyBatis XML映射
│           └── OrderMapper.xml  # 订单SQL定义
└── test/                     # 测试代码
    └── java/com/example/redis_caffeine/  # 测试包结构
```

## 3. 核心功能模块
### 3.1 订单管理
- **数据模型** (`Order.java`)
  ```java
  private Long id;             // 订单ID
  private Long userId;         // 用户ID
  private Long voucherId;      // 优惠券ID
  private Integer payType;     // 支付方式
  private Integer status;      // 订单状态
  private LocalDateTime createTime; // 创建时间
  private LocalDateTime updateTime; // 更新时间
  // 其他时间字段: payTime, useTime, refundTime
  ```

- **API接口** (`OrderController.java`)
 
| 方法   | 路径          | 功能     |
|--------|---------------|----------|
| GET    | `/order/{id}` | 查询订单 |
| PUT    | `/order/{id}` | 更新订单 |
| DELETE | `/order/{id}` | 删除订单 |

- **数据访问** (`OrderMapper.xml`)
  - 查询: `select * from tb_order where id = #{id}`
  - 更新: 全字段更新(含`update_time = NOW()`)
  - 删除: 根据ID删除

### 3.2 缓存系统
#### 多级缓存策略
1. **本地缓存**: Caffeine
   - 配置: 默认缓存120秒
   - 应用: 订单查询结果本地缓存

2. **分布式缓存**: Redis
   - 配置: <mcfile name="application.yml" path="d:/JavaCode/Redis_Caffeine/src/main/resources/application.yml"></mcfile>
   ```yaml
   spring:
     redis:
       host: 127.0.0.1
       port: 6379
       database: 8
       timeout: 5000ms
       lettuce:
         pool:
           max-active: 8
           max-idle: 8
           min-idle: 2
   ```

#### 缓存实现
- **序列化配置**: <mcsymbol name="redisTemplate" filename="RedisConfig.java" path="d:/JavaCode/Redis_Caffeine/src/main/java/com/example/redis_caffeine/config/RedisConfig.java" startline="18" type="function"></mcsymbol>
  - Key: `StringRedisSerializer` (纯字符串)
  - Value: `GenericJackson2JsonRedisSerializer` (支持对象+类型信息)

- **缓存操作** (`OrderServiceImpl.java`)
  ```java
  // 双缓存实现 (已注释的V1版本)
  orderCache.get(key, k -> {
      // 1. 查Redis
      // 2. 查DB
      // 3. 回填缓存
  });
  ```

## 4. 关键配置解析
### 4.1 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/hmdp?useSSL=false&serverTimezone=UTC
    username: root
    password: 123456
```

### 4.2 MyBatis配置
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml  # XML映射位置
  type-aliases-package: com.example.redis_caffeine.entity  # 实体别名
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # SQL日志
```

## 5. 业务流程分析
### 订单查询流程
1. 接收请求: `GET /order/{id}`
2. 权限校验 (未实现)
3. 缓存查询:
   - 先查Caffeine本地缓存
   - 再查Redis分布式缓存
   - 最后查MySQL数据库
4. 结果返回: Order对象JSON

### 缓存更新策略
- **更新操作**: 全量更新Redis + Caffeine
- **删除操作**: 同时删除两级缓存
- **过期策略**: Redis TTL 120秒, Caffeine默认策略

## 6. 待优化项
1. **异常处理**: 缺少全局异常处理器
2. **参数校验**: 接口未实现输入验证
3. **事务管理**: 仅类级别`@Transactional`
4. **缓存设计**: 未实现缓存穿透/击穿防护
5. **代码规范**: 存在大量注释代码块
6. **测试覆盖**: 测试目录为空

## 7. 部署与运行
### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 5.0+

### 启动命令
```bash
# Maven
mvn spring-boot:run

# 或打包后运行
mvn package
java -jar target/Redis_Caffeine-0.0.1-SNAPSHOT.jar
```
        

