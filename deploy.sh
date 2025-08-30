#!/bin/bash

# 乡村旅游管理系统 Docker 部署脚本
# 作者: AI Assistant
# 版本: 1.0.0

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查Docker是否安装
check_docker() {
    log_info "检查Docker环境..."
    if ! command -v docker &> /dev/null; then
        log_error "Docker未安装，请先安装Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose未安装，请先安装Docker Compose"
        exit 1
    fi
    
    log_success "Docker环境检查通过"
}

# 创建必要的目录
create_directories() {
    log_info "创建必要的目录..."
    
    mkdir -p logs/nginx
    mkdir -p logs/app
    mkdir -p uploads
    mkdir -p nginx/ssl
    
    log_success "目录创建完成"
}

# 生成自签名SSL证书
generate_ssl_cert() {
    log_info "生成自签名SSL证书..."
    
    if [ ! -f "nginx/ssl/cert.pem" ] || [ ! -f "nginx/ssl/key.pem" ]; then
        mkdir -p nginx/ssl
        openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
            -keyout nginx/ssl/key.pem \
            -out nginx/ssl/cert.pem \
            -subj "/C=CN/ST=Beijing/L=Beijing/O=Travel/OU=IT/CN=localhost"
        log_success "SSL证书生成完成"
    else
        log_info "SSL证书已存在，跳过生成"
    fi
}

# 构建和启动服务
deploy_services() {
    log_info "开始部署服务..."
    
    # 停止现有服务
    log_info "停止现有服务..."
    docker-compose down --remove-orphans
    
    # 清理旧镜像
    log_info "清理旧镜像..."
    docker system prune -f
    
    # 构建新镜像
    log_info "构建应用镜像..."
    docker-compose build --no-cache
    
    # 启动服务
    log_info "启动服务..."
    docker-compose up -d
    
    log_success "服务启动完成"
}

# 等待服务就绪
wait_for_services() {
    log_info "等待服务就绪..."
    
    # 等待MySQL就绪
    log_info "等待MySQL服务就绪..."
    timeout=60
    while [ $timeout -gt 0 ]; do
        if docker-compose exec -T mysql mysqladmin ping -h localhost --silent; then
            log_success "MySQL服务就绪"
            break
        fi
        sleep 2
        timeout=$((timeout - 2))
    done
    
    if [ $timeout -le 0 ]; then
        log_error "MySQL服务启动超时"
        exit 1
    fi
    
    # 等待Redis就绪
    log_info "等待Redis服务就绪..."
    timeout=30
    while [ $timeout -gt 0 ]; do
        if docker-compose exec -T redis redis-cli ping | grep -q "PONG"; then
            log_success "Redis服务就绪"
            break
        fi
        sleep 1
        timeout=$((timeout - 1))
    done
    
    if [ $timeout -le 0 ]; then
        log_error "Redis服务启动超时"
        exit 1
    fi
    
    # 等待应用就绪
    log_info "等待应用服务就绪..."
    timeout=120
    while [ $timeout -gt 0 ]; do
        if curl -f http://localhost:8080/actuator/health &> /dev/null; then
            log_success "应用服务就绪"
            break
        fi
        sleep 5
        timeout=$((timeout - 5))
    done
    
    if [ $timeout -le 0 ]; then
        log_error "应用服务启动超时"
        exit 1
    fi
}

# 显示服务状态
show_status() {
    log_info "显示服务状态..."
    docker-compose ps
    
    log_info "显示服务日志..."
    echo "=== 应用日志 ==="
    docker-compose logs --tail=20 travel-app
    
    echo -e "\n=== Nginx日志 ==="
    docker-compose logs --tail=20 nginx
    
    echo -e "\n=== MySQL日志 ==="
    docker-compose logs --tail=10 mysql
    
    echo -e "\n=== Redis日志 ==="
    docker-compose logs --tail=10 redis
}

# 显示访问信息
show_access_info() {
    echo -e "\n${GREEN}=== 部署完成 ===${NC}"
    echo -e "应用地址: ${BLUE}http://localhost:8080${NC}"
    echo -e "Nginx地址: ${BLUE}http://localhost${NC} (自动重定向到HTTPS)"
    echo -e "HTTPS地址: ${BLUE}https://localhost${NC}"
    echo -e "MySQL地址: ${BLUE}localhost:3306${NC}"
    echo -e "Redis地址: ${BLUE}localhost:6379${NC}"
    echo -e "\n默认数据库用户: ${YELLOW}travel_user${NC}"
    echo -e "默认数据库密码: ${YELLOW}travel123456${NC}"
    echo -e "默认管理员用户: ${YELLOW}admin${NC}"
    echo -e "默认管理员密码: ${YELLOW}123456${NC}"
}

# 主函数
main() {
    log_info "开始部署乡村旅游管理系统..."
    
    check_docker
    create_directories
    generate_ssl_cert
    deploy_services
    wait_for_services
    show_status
    show_access_info
    
    log_success "部署完成！"
}

# 脚本入口
if [ "$1" = "stop" ]; then
    log_info "停止所有服务..."
    docker-compose down
    log_success "服务已停止"
elif [ "$1" = "restart" ]; then
    log_info "重启所有服务..."
    docker-compose restart
    log_success "服务已重启"
elif [ "$1" = "logs" ]; then
    log_info "显示服务日志..."
    docker-compose logs -f
elif [ "$1" = "status" ]; then
    log_info "显示服务状态..."
    docker-compose ps
    show_status
elif [ "$1" = "clean" ]; then
    log_warning "清理所有数据（包括数据库数据）..."
    docker-compose down -v
    docker system prune -af
    log_success "清理完成"
else
    main
fi 