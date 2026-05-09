# LearnHub

LearnHub 是一个基于 `Java + MySQL + React` 的学习平台，提供后台管理、PC 学生端和统一 API 服务。

## 快速启动

```bash
cp .env.example .env
docker compose up -d --build
```

首次使用全新数据库启动时，系统会自动写入本地 MinIO 的 S3 开发配置，默认可直接上传视频和其他资源，无需再到后台手动配置 S3。

默认本地 S3 / MinIO 配置：

- AccessKey：`learnhub`
- SecretKey：`learnhub123456`
- Bucket：`learnhub`
- Region：`us-east-1`
- Endpoint：`http://minio.localhost:9000`

如果之前已经启动过项目，数据库里可能已经存在旧的空 S3 配置项，初始化逻辑不会覆盖已有配置。此时可以在后台系统配置中手动填入上面的值，或执行 `docker compose down -v` 清空旧数据卷后重新启动。

## 如果不想显示示例数据

```bash
LEARNHUB_DEMO_DATA_ENABLED=false docker compose up -d --build
```

启动后可访问：

- 管理后台：`http://localhost:9900`
- PC 学生端：`http://localhost:9800`
- API：`http://localhost:9700`

默认管理员账号：

- 邮箱：`admin@learnhub.local`
- 密码：`learnhub`

## 项目结构

- `learnhub-admin`：管理后台
- `learnhub-pc`：PC 学生端
- `learnhub-api`：后端服务
