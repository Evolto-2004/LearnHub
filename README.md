# LearnHub

LearnHub is a learning platform based on `Java + MySQL + React`, providing an admin dashboard, a PC student frontend, and a unified API service.

## Quick Start

```bash
cp .env.example .env
docker compose up -d --build
```

When starting with a fresh database for the first time, the system automatically writes the local MinIO S3 development configuration. By default, videos and other resources can be uploaded directly without manually configuring S3 in the admin dashboard.

Default local S3 / MinIO configuration:

- AccessKey: `learnhub`
- SecretKey: `learnhub123456`
- Bucket: `learnhub`
- Region: `us-east-1`
- Endpoint: `http://minio.localhost:9000`

If the project has been started before, the database may already contain older empty S3 configuration entries, and the initialization logic will not overwrite existing configuration. In that case, manually enter the values above in the admin system configuration, or run `docker compose down -v` to clear the old data volumes and start again.

## Disable Demo Data

```bash
LEARNHUB_DEMO_DATA_ENABLED=false docker compose up -d --build
```

After startup, visit:

- Admin dashboard: `http://localhost:9900`
- PC student frontend: `http://localhost:9800`
- API: `http://localhost:9700`

Default admin account:

- Email: `admin@learnhub.local`
- Password: `learnhub`

## Project Structure

- `learnhub-admin`: Admin dashboard
- `learnhub-pc`: PC student frontend
- `learnhub-api`: Backend service
