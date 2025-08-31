# 🚀 乡村旅游后端服务部署指南

## 自动部署流程

### 1. GitHub Actions 自动部署

项目配置了完整的 CI/CD 流水线，推送代码即可自动触发部署：

#### 📋 触发条件
- **测试环境**: 推送到 `develop` 分支
- **生产环境**: 推送到 `main/master` 分支

#### 🔄 自动化流程
1. **代码测试** → 运行单元测试
2. **镜像构建** → 构建 Docker 镜像并推送到 GitHub Container Registry
3. **自动部署** → SSH 连接服务器并部署最新版本

### 2. 本地推送触发部署

```bash
# 开发完成后推送到测试环境
git add .
git commit -m "feat: 新功能开发"
git push origin develop  # 🚀 自动部署到测试环境

# 测试通过后合并到生产环境
git checkout main
git merge develop
git push origin main     # 🚀 自动部署到生产环境
```

## 服务器配置

### 1. GitHub Secrets 配置

在 GitHub 仓库设置中添加以下 Secrets：

#### 测试环境
- `STAGING_HOST`: 测试服务器 IP 地址
- `STAGING_USER`: 服务器用户名
- `STAGING_SSH_KEY`: SSH 私钥

#### 生产环境  
- `PROD_HOST`: 生产服务器 IP 地址
- `PROD_USER`: 服务器用户名
- `PROD_SSH_KEY`: SSH 私钥

### 2. 服务器初始化

在服务器上运行以下命令：

```bash
# 下载部署脚本
wget https://raw.githubusercontent.com/your-username/travel-spring/main/deploy/server-deploy.sh
chmod +x server-deploy.sh

# 一键初始化环境
./server-deploy.sh init
```

### 3. 手动操作命令

```bash
# 查看服务状态
./server-deploy.sh status

# 查看日志
./server-deploy.sh logs

# 重启服务
./server-deploy.sh restart

# 手动更新
./server-deploy.sh update
```

## 🔧 环境配置

### 环境变量 (.env)
```bash
GITHUB_REPOSITORY=your-username/travel-spring
MYSQL_ROOT_PASSWORD=your-strong-password
MYSQL_PASSWORD=your-user-password
```

### 服务端口
- **应用端口**: 8080
- **MySQL**: 3306  
- **Redis**: 6379

## 📊 监控和日志

### 健康检查
```bash
curl http://localhost:8080/actuator/health
```

### 查看日志
```bash
cd /opt/travel-spring
docker-compose logs -f travel-app
```

### 资源监控
```bash
docker stats
```

## 🛠️ 故障排除

### 常见问题

1. **镜像拉取失败**
   ```bash
   # 检查网络连接
   docker pull ghcr.io/your-username/travel-spring:latest
   ```

2. **服务启动失败**
   ```bash
   # 查看详细日志
   docker-compose logs travel-app
   ```

3. **端口被占用**
   ```bash
   # 检查端口占用
   netstat -tlnp | grep :8080
   ```

### 回滚操作
```bash
# 查看可用镜像版本
docker images | grep travel-spring

# 回滚到指定版本
docker-compose down
docker tag ghcr.io/your-username/travel-spring:sha-xxxxx ghcr.io/your-username/travel-spring:latest
docker-compose up -d
```

## 🎯 快速开始

1. **配置 GitHub Secrets**（服务器 SSH 信息）
2. **修改仓库地址**（在 `server-deploy.sh` 中）
3. **推送代码触发部署**：
   ```bash
   git push origin develop  # 部署到测试环境
   git push origin main     # 部署到生产环境
   ```

就是这么简单！🎉 