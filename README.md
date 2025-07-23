# Redis_Caffeine - 多级缓存解决方案

[![Java](https://img.shields.io/badge/Java-17-orange)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-5.0+-red)](https://redis.io)
[![Caffeine](https://img.shields.io/badge/Caffeine-3.0+-blue)](https://github.com/ben-manes/caffeine)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)](https://www.mysql.com)

基于Spring Boot的多级缓存实现项目，整合Redis分布式缓存与Caffeine本地缓存，提供高性能的订单数据访问解决方案。

📖 **技术博客**：[深入解析多级缓存架构设计](https://yourblog.com/redis-caffeine)

## 项目特点

- 🚀 **双级缓存架构**：本地缓存(Caffeine) + 分布式缓存(Redis)的二级缓存体系
- ⚡ **高性能访问**：高频数据本地缓存，减少网络IO开销
- 🔄 **缓存一致性**：采用更新双删策略保证数据一致性
- 📊 **可视化监控**：集成Spring Boot Actuator缓存指标监控

## 技术栈

| 技术领域       | 具体实现                                                                 |
|----------------|--------------------------------------------------------------------------|
| 核心框架       | Spring Boot 3.5.3                                                       |
| 持久层         | MyBatis + MySQL 8.0                                                     |
| 缓存系统       | Redis 5.0+ + Caffeine 3.0+                                              |
| API风格        | RESTful                                                                 |
| 构建工具       | Maven                                                                   |
| JDK版本        | 17                                                                      |

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 5.0+

### 配置步骤

1. **克隆项目**

```markdown
git clone https://github.com/yourusername/Redis_Caffeine.git
cd Redis_Caffeine
```


2. **数据库配置**

修改`application.yml`中的数据库连接信息：

```markdown
spring:
    datasource:
    url: jdbc:mysql://localhost:3306/your_db?useSSL=false&serverTimezone=UTC
    username: your_username
    password: your_password
```


3. **Redis配置**

```markdown
spring:
    redis:
    host: 127.0.0.1
    port: 6379
    database: 8
```


4. **启动应用**

```markdown
mvn spring-boot:run
```


## 项目结构


```markdown
src/
├── main/
│   ├── java/com/example/redis_caffeine/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # API控制器
│   │   ├── entity/          # 实体类
│   │   ├── mapper/          # MyBatis映射器
│   │   ├── service/         # 业务逻辑
│   │   └── RedisCaffeineApplication.java  # 应用入口
│   └── resources/
│       ├── application.yml  # 主配置文件
│       └── mapper/          # MyBatis XML映射
└── test/                    # 测试代码
```


## API接口

### 订单管理

| 方法   | 路径          | 描述     |
|--------|---------------|----------|
| GET    | `/order/{id}` | 查询订单 |
| PUT    | `/order/{id}` | 更新订单 |
| DELETE | `/order/{id}` | 删除订单 |

**示例请求**:

```markdown
curl -X GET "http://localhost:8080/order/1"
```


## 缓存策略

### 查询流程

1. 优先查询Caffeine本地缓存
2. 未命中则查询Redis分布式缓存
3. 仍未命中则查询MySQL数据库
4. 结果回填到Redis和Caffeine

### 更新策略

- **写操作**：在更新DB的同时，强制更新Redis和Caffeine缓存
- **过期时间**：
    - Redis：默认120秒TTL
    - Caffeine：基于大小和访问时间的混合策略

## 性能优化

- 使用`StringRedisSerializer`优化Key序列化
- 采用`GenericJackson2JsonRedisSerializer`处理复杂Value对象
- 配置Lettuce连接池减少连接创建开销

## 未来计划

- [ ] 添加缓存穿透/击穿防护
- [ ] 实现分布式锁保证强一致性
- [ ] 集成Prometheus监控缓存命中率
- [ ] 添加Swagger API文档

## 贡献指南

欢迎提交Issue和PR！请确保：

1. 代码符合Google Java Style规范
2. 新功能附带单元测试
3. 更新相关文档

## 许可证

[MIT License](LICENSE)
