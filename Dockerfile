# ============================================================
# 1. Build frontend
# ============================================================
FROM node:26-alpine AS frontend-build

WORKDIR /frontend

COPY front_end/package*.json ./
RUN npm ci

COPY front_end/ ./

COPY front_end/.env.production .env.production

RUN npm run build


# ============================================================
# 2. Build backend
# ============================================================
FROM maven:3.9.9-eclipse-temurin-21 AS backend-build

WORKDIR /backend

COPY back_end/.mvn/ .mvn/
COPY back_end/mvnw back_end/pom.xml ./

RUN chmod +x mvnw

COPY back_end/src ./src

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -DskipTests package


# ============================================================
# 3. Final production image
# ============================================================
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends nginx \
    && rm -rf /var/lib/apt/lists/* \
    && rm -f /etc/nginx/sites-enabled/default

COPY --from=frontend-build /frontend/dist /usr/share/nginx/html
COPY --from=backend-build /backend/target/*.jar /app/app.jar

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

EXPOSE 80

ENTRYPOINT ["/docker-entrypoint.sh"]