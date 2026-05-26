# MestreMenu — Microserviços com Clean Architecture

## Descrição

O **MestreMenu** é um ecossistema de microserviços para gestão compartilhada de restaurantes, cardápios e pedidos. O projeto foi construído com foco em **modularidade**, **testabilidade** e **separação estrita de responsabilidades**, aplicando os princípios da **Clean Architecture** em cada serviço de forma não-negociável.

Ele cobre todo o ciclo de um pedido gastronômico: cadastro de usuários e restaurantes, montagem de cardápios, realização de pedidos e processamento de pagamentos de forma assíncrona.

---

## Tecnologias Utilizadas

### Back-end
- **Java 21** — Virtual Threads, Records, Pattern Matching
- **Spring Boot 3.4.0** — Web, Data JPA, Security, Actuator
- **Spring Cloud Gateway** — API Gateway com roteamento dinâmico e validação JWT
- **Spring Kafka** — Producer (pedido-service) e Consumer (pagamento-service)
- **Spring Security + JWT** — Autenticação stateless via HMAC256
- **Flyway** — Migrações versionadas de banco de dados
- **Resilience4j** — Circuit Breaker e retry (wiring disponível em pedido/pagamento)

### Infraestrutura
- **PostgreSQL 16** — Banco relacional (schemas isolados por serviço via Flyway)
- **Apache Kafka** (Confluent Platform 7.4.0, modo KRaft — sem Zookeeper)
- **Docker** + **Docker Compose** — Containerização multi-stage com imagem JRE mínima
- **Gradle** (multi-module) — Build e gerenciamento de dependências

### Observabilidade
- **Prometheus** — Coleta de métricas via `/actuator/prometheus`
- **Grafana** — Dashboards e alertas visuais

### Testes
- **JUnit 5** + **Mockito** — Testes unitários e de integração
- Testes de integração usam `@SpringBootTest` com PostgreSQL e Kafka reais

---

## Arquitetura

### Serviços

| Serviço | Porta (host) | Responsabilidade |
| :--- | :--- | :--- |
| **gateway-service** | `8090` | Único ponto de entrada público. Valida JWT e roteia para os serviços internos. |
| **usuario-service** | via gateway | Autenticação, cadastro de usuários e tipos de usuário (roles). |
| **restaurante-service** | via gateway | Restaurantes, cardápios e itens de cardápio. |
| **pedido-service** | via gateway | Criação e consulta de pedidos (produtor Kafka). |
| **pagamento-service** | — | Processamento de pagamentos (consumidor Kafka + chamada ao processador externo). |

> Os serviços internos não expõem sua porta HTTP ao host — todo o tráfego passa pelo gateway em `8090`. Cada serviço expõe apenas sua porta de debug JDWP (`5005`–`5009` no host).

### Infraestrutura

| Container | Porta (host) | Descrição |
| :--- | :--- | :--- |
| **PostgreSQL** | `5432` | Banco de dados relacional compartilhado. |
| **Kafka** | `9092` | Broker de mensagens para comunicação assíncrona. |
| **ProcPag** | `8089` | Mock do processador de pagamentos externo. |
| **Prometheus** | `9090` | Coleta de métricas dos serviços. |
| **Grafana** | `3000` | Visualização de métricas (`admin` / `GRAFANA_PASSWORD`). |

### Diagrama de Comunicação

```
Cliente HTTP
     │
     ▼
gateway-service :8090  ──── valida JWT ────► usuario-service :8081
     │
     ├──────────────────────────────────────────► restaurante-service :8082
     │                                            (lê usuário logado dos claims
     │                                             do JWT via SecurityContext —
     │                                             nenhuma chamada externa)
     │
     └──────────────────────────────────────────► pedido-service :8083
                                                        │
                                              Kafka: pagamentos-topic
                                                        │
                                                        ▼
                                               pagamento-service :8084
                                                        │
                                                        ▼
                                               ProcPag (mock) :8089
```

> **Nota:** `restaurante-service` e `usuario-service` são **completamente independentes em runtime**. O `restaurante-service` resolve a identidade do usuário logado lendo os claims do JWT (user ID e role) diretamente do `SecurityContextHolder` via `SpringSecurityUsuarioLogadoAdapter` — sem chamadas HTTP nem dependência compile-time entre os serviços.

---

## Clean Architecture

Cada serviço maduro segue a seguinte estrutura de pacotes:

```
core/                          ← zero dependências de frameworks
  domain/                      — Entidades e value objects de domínio
  rules/                       — Validadores de regra de negócio (interface Rule)
  usecase/                     — Interfaces dos casos de uso + implementações
  dto/                         — Context records imutáveis passados entre camadas
  gateway/                     — Interfaces de porta (Repository, Gateway)
  exception/                   — Exceções de domínio (extends DomainException)
  api/                         — Facades e DTOs para integração entre serviços

infra/                         ← depende de core, nunca o inverso
  config/                      — @Configuration: wiring de use cases e listas de rules
  controller/                  — Controllers REST (request/response records)
  gateway/
    entity/                    — @Entity JPA (nunca expostos ao core)
    *RepositoryJpaImpl         — Adapters das interfaces de Repository do core
    *EntityMapper              — Mappers estáticos domínio ↔ JPA
  security/                    — JWT filter, TokenService, SecurityConfig
```

### Regras de Fronteira (não-negociáveis)

- `core/` **nunca** importa Spring, JPA ou qualquer framework
- Entidades de domínio em `core/domain/` **nunca** carregam `@Entity`, `@Column` etc.
- Objetos de domínio **nunca** são passados diretamente à camada JPA ou HTTP
- Interfaces de Gateway ficam em `core/gateway/`; implementações em `infra/gateway/`
- Tipos HTTP (request/response) ficam em `infra/controller/`, nunca em `core/`

### Fluxo de Execução de um Caso de Uso

```
1. Busca o usuário logado via ObterUsuarioLogadoGateway
2. Monta um Context record imutável com os dados de entrada e lambdas lazy para DB
3. Executa a lista de permission rules (quem pode agir) → lança exceção se falhar
4. Executa a lista de business rules (a ação é válida?) → lança exceção se falhar
5. Persiste dentro de transactionGateway.execute() e retorna
```

A classe `@Configuration` de cada use case injeta duas listas ordenadas de `Rule`: uma de permissão e uma de negócio. Adicionar uma nova regra = implementar `Rule` + registrar na config.

---

## Padrões de Design

| Padrão | Onde | Descrição |
| :--- | :--- | :--- |
| **Rule Pattern** | `core/rules/` | Cada regra é uma classe isolada que implementa `Rule<Context>`. Listas ordenadas de regras são injetadas nas implementações de use case. |
| **Context Record** | `core/dto/` | Records Java imutáveis que carregam input, usuário logado e `BooleanSupplier` lambdas para consultas lazy ao banco. |
| **Gateway / Port** | `core/gateway/` | Interfaces puras definem o contrato; implementações JPA ficam em `infra/`. Use cases nunca dependem de JPA diretamente. |
| **Entity Mapper** | `infra/gateway/` | Classes de mapeamento estático entre objetos de domínio e `@Entity` JPA — evita que anotações de persistência contaminem o domínio. |
| **JWT Claims como identidade** | `infra/gateway/` | `SpringSecurityUsuarioLogadoAdapter` resolve o usuário logado lendo os claims do JWT já validado (user ID + role) do `SecurityContextHolder` — sem chamadas ao `usuario-service`. |
| **Async via Kafka** | `pedido-service` / `pagamento-service` | Pedidos confirmados são publicados no topic `pagamentos-topic`; `pagamento-service` consome e aciona o processador externo. |

---

## Configuração do Ambiente (`.env`)

O arquivo `.env` **não está versionado** por conter credenciais. Antes de subir qualquer serviço, crie-o a partir do template:

```bash
cp .env.example .env
```

Em seguida, preencha as variáveis:

### `POSTGRES_DB` / `POSTGRES_USER` / `POSTGRES_PASSWORD` *(obrigatório)*

Credenciais do banco PostgreSQL. Em desenvolvimento, qualquer valor serve.

```env
POSTGRES_DB=mestremenu
POSTGRES_USER=mestremenu
POSTGRES_PASSWORD=suasenha
```

### `JWT_SECRET` *(obrigatório)*

Segredo compartilhado usado pelo `usuario-service` para **assinar** tokens JWT e pelo `gateway-service` para **verificá-los**. Deve ser **idêntico** em todos os serviços.

Em produção, use uma string longa e aleatória:

```bash
openssl rand -hex 32
```

```env
JWT_SECRET=cole_o_valor_gerado_aqui
```

> **Algoritmo:** HMAC256 · **Issuer:** `mestre-menu-api` · **Expiração:** 2 horas

### `SPRING_PROFILES_ACTIVE` *(obrigatório no Docker)*

Controla qual perfil Spring é ativado. **Não altere** este valor ao rodar via Docker Compose — o perfil `docker` usa os hostnames internos (`db`, `kafka`) que não resolvem no host.

```env
SPRING_PROFILES_ACTIVE=docker
```

### `GRAFANA_PASSWORD` *(opcional)*

Senha do usuário `admin` na interface do Grafana em `http://localhost:3000`. Padrão: `admin`.

```env
GRAFANA_PASSWORD=suasenha
```

### `GRAFANA_CLOUD_URL` / `GRAFANA_CLOUD_USER` / `GRAFANA_CLOUD_KEY` *(opcional)*

Credenciais para envio de métricas ao Grafana Cloud. Crie uma conta gratuita em [grafana.com](https://grafana.com), acesse **My Account → Stacks → Send metrics** e copie os valores. Depois de preencher, descomente o bloco `remote_write` em `monitoring/prometheus.yml` e reinicie:

```bash
docker compose up -d --no-deps prometheus
```

---

## Como Executar

### Pré-requisito

Crie e preencha o `.env` conforme a seção acima.

### 1. Docker Compose (recomendado)

```bash
# Primeira execução — constrói as imagens e sobe tudo
docker compose up -d --build

# Execuções subsequentes (sem mudanças de código)
docker compose up -d

# Acompanhar logs de todos os serviços
docker compose logs -f

# Acompanhar logs de um serviço específico
docker compose logs -f gateway-service

# Verificar saúde dos containers
docker compose ps
```

> **Importante:** use `--build` sempre que alterar `build.gradle`, `application.properties` ou qualquer código Java. Sem `--build`, o Docker reutiliza a imagem anterior.

Após subir, a API estará disponível em:

| Endpoint | URL |
| :--- | :--- |
| API pública (gateway) | `http://localhost:8090` |
| Prometheus | `http://localhost:9090` |
| Grafana | `http://localhost:3000` |

### 2. Desenvolvimento Local (Gradle + infra no Docker)

Suba apenas a infraestrutura necessária:

```bash
docker compose up -d db kafka procpag
```

Execute o serviço desejado via Gradle com o perfil `dev`:

```bash
./gradlew :usuario-service:bootRun -Dspring.profiles.active=dev
./gradlew :restaurante-service:bootRun -Dspring.profiles.active=dev
./gradlew :pedido-service:bootRun -Dspring.profiles.active=dev
./gradlew :pagamento-service:bootRun -Dspring.profiles.active=dev
```

> **Por que `-Dspring.profiles.active=dev`?** As credenciais de banco e os endereços de Kafka ficam nos arquivos `application-dev.*` de cada serviço. O `application.properties` base não contém credenciais — isso evita que valores hardcoded sejam versionados. Não use o perfil `docker` localmente: ele usa os hostnames internos do Docker (`db`, `kafka`) que não resolvem no host.

---

## Build e Testes

```bash
# Compilar todos os módulos
./gradlew build

# Rodar todos os testes
./gradlew test

# Rodar testes de um módulo específico
./gradlew :usuario-service:test
./gradlew :restaurante-service:test
./gradlew :pedido-service:test
./gradlew :pagamento-service:test

# Rodar uma classe de teste específica
./gradlew :usuario-service:test --tests "com.github.lilianjaf.usuario_service.core.rules.UsuarioDeveEstarAutenticadoRuleTest"

# Ver relatório de testes (após rodar)
open usuario-service/build/reports/tests/test/index.html
```

> Os testes unitários (`*Test`) em `core/` rodam sem infraestrutura. Os testes de integração (`*IT`) requerem PostgreSQL e Kafka ativos — suba a infra com `docker compose up -d db kafka` antes.

---

## Funcionalidades Implementadas

### Autenticação e Usuários (`usuario-service`)
- Cadastro de novos usuários
- Cadastro e gerenciamento de tipos de usuário (roles)
- Login com retorno de token JWT
- Troca de senha
- Controle de acesso baseado em tipo de usuário

### Restaurantes e Cardápios (`restaurante-service`)
- Cadastro e atualização de restaurantes
- Listagem e busca de restaurantes
- Criação e edição de cardápios vinculados a restaurantes
- Gestão de itens de cardápio (nome, descrição, preço, disponibilidade, foto)
- Exclusão de cardápios e itens

### Pedidos (`pedido-service`)
- Criação de pedidos com itens do cardápio
- Consulta de pedido por ID
- Listagem de pedidos por cliente
- Confirmação de pedido (publica evento no Kafka)

### Pagamentos (`pagamento-service`)
- Consumo de eventos do Kafka (`pagamentos-topic`)
- Acionamento do processador de pagamentos externo (ProcPag)

---

## Observabilidade

Todos os serviços expõem métricas via `/actuator/prometheus`. O Prometheus coleta automaticamente e o Grafana exibe os dashboards.

```bash
# Verificar métricas de um serviço (exemplo com gateway rodando localmente)
curl http://localhost:8090/actuator/health
curl http://localhost:9090/targets  # status de todos os scrapers no Prometheus
```

Acesse o Grafana em `http://localhost:3000` com usuário `admin` e a senha definida em `GRAFANA_PASSWORD`.

---

## Documentação da API

A coleção do Postman está disponível na raiz do projeto:

```
postmanCollection.json
```

Importe-a no Postman ou Insomnia. O fluxo padrão é:

1. `POST /api/v1/publico/usuarios` — criar usuário
2. `POST /api/v1/login` — autenticar e obter o JWT
3. Usar o token no header `Authorization: Bearer <token>` nas demais requisições

Todas as rotas (exceto `/api/v1/login`, `/api/v1/publico/**` e `/actuator/**`) exigem autenticação.
