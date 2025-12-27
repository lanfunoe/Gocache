FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# 复制 pom.xml 并下载依赖（利用Docker缓存）
COPY pom.xml .
RUN mvn dependency:go-offline

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests

# 运行时镜像
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 复制打包好的jar文件
COPY --from=builder /app/target/*.jar app.jar

# 创建必要的目录
RUN mkdir -p /app/media /app/logs

# 暴露端口
EXPOSE 6522

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:6522/actuator/health || exit 1

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
