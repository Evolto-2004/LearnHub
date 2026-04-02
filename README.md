# LearnHub

LearnHub 是一个基于 `Java + MySQL + React` 的学习平台，提供后台管理、PC 学生端、H5 学生端和统一 API 服务。

## 快速启动

```bash
cp .env.example .env
docker compose up -d --build
```

启动后可访问：

- 管理后台：`http://localhost:9900`
- PC 学生端：`http://localhost:9800`
- H5 学生端：`http://localhost:9801`
- API：`http://localhost:9700`

默认管理员账号：

- 邮箱：`admin@learnhub.local`
- 密码：`learnhub`

## 项目结构

- `learnhub-admin`：管理后台
- `learnhub-pc`：PC 学生端
- `learnhub-h5`：H5 学生端
- `learnhub-api`：后端服务
