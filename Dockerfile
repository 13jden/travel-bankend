# 多阶段构建 - 构建阶段
FROM maven:3.9.6-eclipse-temurin-17 AS builder

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
FROM eclipse-temurin:17-jre-jammy

# 设置工作目录
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai

# 安装必要的工具和字体
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl fonts-noto-cjk && \
    rm -rf /var/lib/apt/lists/*

# 设置环境变量
ENV SPRING_PROFILES_ACTIVE=prod

# 复制构建好的jar文件
COPY --from=builder /app/travel-admin/target/travel-admin-*.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
CMD ["java", "-jar", "app.jar"] 