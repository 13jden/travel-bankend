@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM 乡村旅游管理系统 Docker 部署脚本 (Windows版本)
REM 作者: AI Assistant
REM 版本: 1.0.0

echo [INFO] 开始部署乡村旅游管理系统...

REM 检查Docker是否安装
echo [INFO] 检查Docker环境...
docker --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker未安装，请先安装Docker Desktop
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker Compose未安装，请先安装Docker Compose
    pause
    exit /b 1
)

echo [SUCCESS] Docker环境检查通过

REM 创建必要的目录
echo [INFO] 创建必要的目录...
if not exist "logs\nginx" mkdir "logs\nginx"
if not exist "logs\app" mkdir "logs\app"
if not exist "uploads" mkdir "uploads"
if not exist "nginx\ssl" mkdir "nginx\ssl"

echo [SUCCESS] 目录创建完成

REM 生成自签名SSL证书
echo [INFO] 生成自签名SSL证书...
if not exist "nginx\ssl\cert.pem" (
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout "nginx\ssl\key.pem" -out "nginx\ssl\cert.pem" -subj "/C=CN/ST=Beijing/L=Beijing/O=Travel/OU=IT/CN=localhost"
    echo [SUCCESS] SSL证书生成完成
) else (
    echo [INFO] SSL证书已存在，跳过生成
)

REM 停止现有服务
echo [INFO] 停止现有服务...
docker-compose down --remove-orphans

REM 清理旧镜像
echo [INFO] 清理旧镜像...
docker system prune -f

REM 构建新镜像
echo [INFO] 构建应用镜像...
docker-compose build --no-cache

REM 启动服务
echo [INFO] 启动服务...
docker-compose up -d

echo [SUCCESS] 服务启动完成

REM 等待服务就绪
echo [INFO] 等待服务就绪...

REM 等待MySQL就绪
echo [INFO] 等待MySQL服务就绪...
set timeout=60
:wait_mysql
docker-compose exec -T mysql mysqladmin ping -h localhost --silent >nul 2>&1
if errorlevel 1 (
    set /a timeout-=2
    if !timeout! leq 0 (
        echo [ERROR] MySQL服务启动超时
        pause
        exit /b 1
    )
    timeout /t 2 /nobreak >nul
    goto wait_mysql
)
echo [SUCCESS] MySQL服务就绪

REM 等待Redis就绪
echo [INFO] 等待Redis服务就绪...
set timeout=30
:wait_redis
docker-compose exec -T redis redis-cli ping | findstr "PONG" >nul 2>&1
if errorlevel 1 (
    set /a timeout-=1
    if !timeout! leq 0 (
        echo [ERROR] Redis服务启动超时
        pause
        exit /b 1
    )
    timeout /t 1 /nobreak >nul
    goto wait_redis
)
echo [SUCCESS] Redis服务就绪

REM 等待应用就绪
echo [INFO] 等待应用服务就绪...
set timeout=120
:wait_app
curl -f http://localhost:8080/actuator/health >nul 2>&1
if errorlevel 1 (
    set /a timeout-=5
    if !timeout! leq 0 (
        echo [ERROR] 应用服务启动超时
        pause
        exit /b 1
    )
    timeout /t 5 /nobreak >nul
    goto wait_app
)
echo [SUCCESS] 应用服务就绪

REM 显示服务状态
echo [INFO] 显示服务状态...
docker-compose ps

echo [INFO] 显示服务日志...
echo === 应用日志 ===
docker-compose logs --tail=20 travel-app

echo.
echo === Nginx日志 ===
docker-compose logs --tail=20 nginx

echo.
echo === MySQL日志 ===
docker-compose logs --tail=10 mysql

echo.
echo === Redis日志 ===
docker-compose logs --tail=10 redis

REM 显示访问信息
echo.
echo [SUCCESS] === 部署完成 ===
echo 应用地址: http://localhost:8080
echo Nginx地址: http://localhost (自动重定向到HTTPS)
echo HTTPS地址: https://localhost
echo MySQL地址: localhost:3306
echo Redis地址: localhost:6379
echo.
echo 默认数据库用户: travel_user
echo 默认数据库密码: travel123456
echo 默认管理员用户: admin
echo 默认管理员密码: 123456

echo.
echo [SUCCESS] 部署完成！
pause 