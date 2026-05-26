#!/usr/bin/env bash
set -e

echo "==> Derrubando containers e apagando volumes..."
docker compose down -v

echo "==> Subindo serviços..."
docker compose up -d --build

echo "==> Aguardando usuario-service..."
until docker inspect --format='{{.State.Health.Status}}' usuario-service 2>/dev/null | grep -q "healthy"; do
  sleep 5
  printf "."
done
echo " OK"

echo "==> Aguardando restaurante-service..."
until docker inspect --format='{{.State.Health.Status}}' restaurante-service 2>/dev/null | grep -q "healthy"; do
  sleep 5
  printf "."
done
echo " OK"

echo "==> Aguardando gateway-service..."
until docker inspect --format='{{.State.Health.Status}}' gateway-service 2>/dev/null | grep -q "healthy"; do
  sleep 5
  printf "."
done
echo " OK"

echo "==> Aguardando procpag..."
until docker inspect --format='{{.State.Health.Status}}' procpag 2>/dev/null | grep -q "healthy"; do
  sleep 5
  printf "."
done
echo " OK"

echo ""
echo "Mestre Menu pronto"
echo ""
docker compose logs -f 2>&1 | grep --line-buffered -iE "error|exception|caused by|started|flyway"
