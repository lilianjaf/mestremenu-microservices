# MestreMenu Microservices

Este projeto é uma arquitetura de microserviços para o sistema MestreMenu, construído com **Spring Boot 3**, **Java 21**, **Gradle** e **Docker**.

## Arquitetura

O sistema é composto pelos seguintes serviços:

- **usuario-service**: Gerenciamento de usuários e segurança.
- **restaurante-service**: Gerenciamento de restaurantes e cardápios.
- **pedido-service**: Gerenciamento de pedidos e integração via Kafka.
- **pagamento-service**: Processamento de pagamentos integrado com Kafka e processador externo.

### Infraestrutura
- **PostgreSQL**: Banco de dados relacional.
- **Kafka**: Broker de mensagens para comunicação assíncrona.
- **ProcPag**: Mock de processador de pagamentos externo.

---

## Como Executar

### 1. Usando Docker Compose (Recomendado)
A maneira mais simples de subir todo o ecossistema (serviços + infraestrutura) é usando o Docker Compose.

```bash
# Sobe todos os containers em background
docker compose up -d

# Acompanha os logs
docker compose logs -f
```

### 2. Executando Módulos Individualmente (Gradle)
Para desenvolvimento local, você pode subir apenas a infraestrutura e rodar os serviços via Gradle.

**Subir Infraestrutura:**
```bash
docker compose up -d db kafka procpag
```

**Executar um Serviço:**
```bash
./gradlew :usuario-service:bootRun
./gradlew :restaurante-service:bootRun
./gradlew :pedido-service:bootRun
./gradlew :pagamento-service:bootRun
```

---

## Portas e Endereços

Quando rodando via Docker Compose, os serviços ficam expostos nas seguintes portas:

| Serviço | Porta Local | Descrição |
| :--- | :--- | :--- |
| **usuario-service** | `8081` | API de Usuários |
| **restaurante-service** | `8082` | API de Restaurantes |
| **pedido-service** | `8083` | API de Pedidos |
| **pagamento-service** | `8084` | API de Pagamentos |
| **PostgreSQL** | `5432` | Banco de Dados |
| **Kafka** | `9092` | Broker de Mensagens |
| **ProcPag** | `8080` | Mock de Pagamento |

---

## Build e Testes

Para compilar o projeto e gerar os artefatos (.jar):
```bash
./gradlew build
```

Para rodar os testes de todos os módulos:
```bash
./gradlew test
```

---

## Configuração
As variáveis de ambiente principais estão definidas no arquivo `.env` na raiz do projeto. Caso execute os serviços via IDE, certifique-se de que essas variáveis estejam configuradas ou que o perfil `docker` não esteja ativo (para evitar conflito de nomes de host como `db` e `kafka`).
