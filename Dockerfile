# 多阶段构建 - 构建阶段
FROM maven:3.9.6-openjdk-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制pom文件
COPY pom.xml .
COPY travel-common/pom.xml travel-common/
COPY travel-web/pom.xml travel-web/
COPY travel-admin/pom.xml travel-admin/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY travel-common/src travel-common/src
COPY travel-web/src travel-web/src
COPY travel-admin/src travel-admin/src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jre-slim

# 设置工作目录
WORKDIR /app

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 创建非root用户
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 复制构建好的jar文件
COPY --from=builder /app/travel-admin/target/travel-admin-*.jar app.jar

# 设置文件权限
RUN chown -R appuser:appuser /app
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"] 