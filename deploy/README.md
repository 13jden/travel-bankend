# 🚀 乡村旅游后端服务部署指南

## Self-Hosted Runner 自动部署

使用 self-hosted runner 在您的服务器上直接运行 workflow，实现**构建、推镜像、部署一步到位**！

### 🎯 优势
- ✅ **本地构建**：无需等待 GitHub 云端构建
- ✅ **直接部署**：构建完成立即部署，无需 SSH
- ✅ **更快速度**：本地网络，镜像推拉更快
- ✅ **资源可控**：使用自己的服务器资源

### 📋 触发条件
- **测试环境**: 推送到 `develop` 分支
- **生产环境**: 推送到 `main/master` 分支

### 🔄 自动化流程
1. **代码测试** → 在服务器上运行单元测试
2. **镜像构建** → 在服务器上构建 Docker 镜像并推送
3. **立即部署** → 直接在服务器上部署最新版本

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

## Self-Hosted Runner 配置

### 1. 一键安装 Runner

在您的服务器上运行：

```bash
# 下载安装脚本
wget https://raw.githubusercontent.com/your-username/travel-spring/main/deploy/setup-runner.sh
chmod +x setup-runner.sh

# 一键安装环境
sudo ./setup-runner.sh install
```

### 2. 配置 GitHub Runner

安装完成后，按照提示配置：

```bash
# 1. 获取 Runner Token
# 进入 GitHub 仓库 → Settings → Actions → Runners → New self-hosted runner

# 2. 配置 Runner
sudo -u github-runner bash
cd /home/github-runner/actions-runner
./config.sh --url https://github.com/YOUR_USERNAME/YOUR_REPO --token YOUR_TOKEN

# 3. 启动服务
exit  # 退出 github-runner 用户
sudo systemctl enable github-runner
sudo systemctl start github-runner
```

### 3. 修改仓库地址

编辑配置文件，将 `your-username` 替换为您的 GitHub 用户名：

```bash
# 测试环境
sudo nano /opt/travel-spring-staging/docker-compose.yml

# 生产环境  
sudo nano /opt/travel-spring-production/docker-compose.yml
```

### 4. Runner 管理命令

```bash
# 查看 Runner 状态
./setup-runner.sh status

# 查看 Runner 日志
./setup-runner.sh logs

# 启动/停止 Runner
./setup-runner.sh start
./setup-runner.sh stop
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

### 方案一：Self-Hosted Runner（推荐）

1. **安装 Runner**：`sudo ./setup-runner.sh install`
2. **配置 GitHub Token**（获取方式见上文）
3. **修改仓库地址**（在 docker-compose.yml 中）
4. **推送代码触发部署**：
   ```bash
   git push origin develop  # 服务器自动构建并部署到测试环境
   git push origin main     # 服务器自动构建并部署到生产环境
   ```

### 方案二：远程 SSH 部署

如果不想使用 self-hosted runner，可以使用原来的 SSH 方式：

1. **配置 GitHub Secrets**（服务器 SSH 信息）
2. **修改 workflow** 使用 `ubuntu-latest` 而不是 `self-hosted`
3. **推送代码触发部署**

## 🔄 Self-Hosted Runner 工作流程

```mermaid
graph LR
    A[推送代码] --> B[触发 Workflow]
    B --> C[服务器运行测试]
    C --> D[服务器构建镜像]
    D --> E[推送到 Registry]
    E --> F[服务器直接部署]
    F --> G[部署完成]
```

**一切都在您的服务器上完成，无需外部依赖！** 🎉 