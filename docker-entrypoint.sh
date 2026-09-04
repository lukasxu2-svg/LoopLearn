#!/bin/sh

set -e

echo "Starting Spring Boot backend..."
java -jar /app/app.jar &
BACKEND_PID=$!

echo "Starting Nginx..."
nginx -g "daemon off;" &
NGINX_PID=$!

cleanup() {
    echo "Stopping services..."
    kill "$BACKEND_PID" 2>/dev/null || true
    kill "$NGINX_PID" 2>/dev/null || true
}

trap cleanup INT TERM EXIT

while true; do
    if ! kill -0 "$BACKEND_PID" 2>/dev/null; then
        echo "Spring Boot stopped."
        exit 1
    fi

    if ! kill -0 "$NGINX_PID" 2>/dev/null; then
        echo "Nginx stopped."
        exit 1
    fi

    sleep 1
done