FROM node:20-alpine AS node-builder

RUN corepack enable

COPY learnhub-admin /app/admin
COPY learnhub-pc /app/pc

WORKDIR /app/admin
RUN pnpm i && VITE_APP_URL=/api/ pnpm build

WORKDIR /app/pc
RUN pnpm i && VITE_APP_URL=/api/ pnpm build

FROM eclipse-temurin:17-jdk-jammy AS java-builder

COPY learnhub-api /app

WORKDIR /app

RUN /app/mvnw -Dmaven.test.skip=true clean package

FROM eclipse-temurin:17-jre-jammy AS base

RUN apt-get update \
  && apt-get install -y --no-install-recommends nginx \
  && rm -rf /var/lib/apt/lists/*

COPY --from=java-builder /app/learnhub-api/target/learnhub-api.jar /app/api/app.jar

COPY --from=node-builder /app/admin/dist /app/admin
COPY --from=node-builder /app/pc/dist /app/pc

COPY docker/nginx/conf/nginx.conf /etc/nginx/sites-enabled/default

EXPOSE 9898
EXPOSE 9800
EXPOSE 9900

CMD nginx; echo "Waiting for MySQL to start..."; sleep 15; java -jar /app/api/app.jar --spring.profiles.active=prod --spring.datasource.url="jdbc:mysql://${DB_HOST}:${DB_PORT:-3306}/${DB_NAME}?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true" --spring.datasource.username=${DB_USER} --spring.datasource.password=${DB_PASS} --sa-token.is-concurrent=${SA_TOKEN_IS_CONCURRENT:-false} --sa-token.jwt-secret-key=${SA_TOKEN_JWT_SECRET_KEY}
