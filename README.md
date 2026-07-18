# Mathew Finance — Backend

API REST do produto de gestão financeira pessoal **Mathew Finance**, construída com Quarkus.

## 🎯 Visão do Produto

Plataforma de gestão financeira pessoal que reduz o atrito do registro de transações através de **categorização inteligente**: uma vez que um estabelecimento é categorizado pelo usuário, novas compras no mesmo local são categorizadas automaticamente.

> 📄 Documento completo de requisitos: [`PRD-Gestao-Financeira.md`](./PRD-Gestao-Financeira.md)
> 📐 Modelagem UML: [`backend.puml`](./backend.puml)

---

## 🏗️ Stack Técnica

| Camada | Tecnologia |
|---|---|
| Runtime | Java 25 + Quarkus (RESTEasy Reactive) |
| ORM | Hibernate ORM with Panache |
| Banco de dados | PostgreSQL (com extensão `pg_trgm`) |
| Autenticação | JWT (SmallRye JWT) — sessão stateless |
| Validação | Hibernate Validator (Jakarta Validation) |
| Documentação | SmallRye OpenAPI + Swagger UI |

---

## 📦 Funcionalidades (MVP)

| # | Funcionalidade | Prioridade |
|---|---|---|
| F1 | Cadastro e login de usuário (e-mail/senha) | P0 |
| F2 | CRUD de lançamentos (gasto/entrada) | P0 |
| F3 | Cadastro e gestão de categorias (padrão + customizadas) | P0 |
| F4 | Categorização automática por estabelecimento recorrente | P0 |
| F5 | Listagem de lançamentos com filtros (período, categoria, tipo) | P0 |
| F6 | Dashboard resumo (entradas, saídas, saldo, gastos por categoria) | P1 |
| F7 | Edição de categoria de estabelecimento (com recategorização retroativa) | P1 |
| F8 | Exportação de lançamentos (CSV) | P2 |

---

## 📊 Modelo de Dados

```
users                   categories              transactions
├─ id (UUID, PK)        ├─ id (UUID, PK)        ├─ id (UUID, PK)
├─ name                 ├─ user_id (FK, null)   ├─ user_id (FK)
├─ email (UNIQUE)       ├─ name                 ├─ type (GASTO/ENTRADA)
├─ password_hash        ├─ type (ENUM)          ├─ amount (DECIMAL)
└─ created_at           ├─ color                ├─ description
                        └─ created_at           ├─ transaction_date
                                                ├─ category_id (FK)
merchant_mappings                               ├─ merchant_mapping_id (FK, null)
├─ id (UUID, PK)                                ├─ notes
├─ user_id (FK)                                 └─ created_at
├─ normalized_merchant_name
├─ category_id (FK)
├─ created_at
└─ updated_at
```

**Índice chave:** `merchant_mappings (user_id, normalized_merchant_name)` — único composto.

---

## 🧠 Regra de Negócio Central — Categorização Automática

1. Usuário informa o estabelecimento (ex: `"Supermercado Pague Menos"`).
2. Sistema **normaliza** o texto (lowercase, remove acentos/pontuação).
3. Busca no `merchant_mappings` do usuário:
   - **Match exato** no nome normalizado.
   - **Match por similaridade** (`pg_trgm`, limiar ≥ 85%) para variações como `"Pague Menos Norte"` vs `"Pague Menos - Filial Norte"`.
4. Se encontrar, a categoria é **sugerida automaticamente**.
5. Se o usuário corrigir a categoria, o sistema **reaprende** — atualiza o mapeamento.

---

## 🚀 Rodando o projeto

### Pré-requisitos

- Java 25+
- PostgreSQL (com extensão `pg_trgm` habilitada)
- Maven Wrapper (`./mvnw`)

### Configuração do banco

```sql
CREATE EXTENSION IF NOT EXISTS pg_trgm;
```

Configure a conexão em [`application.properties`](./src/main/resources/application.properties):

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/mathewfinance
quarkus.datasource.username=seu_usuario
quarkus.datasource.password=sua_senha
quarkus.hibernate-orm.database.generation=drop-and-create
```

### Dev mode

```shell
./mvnw quarkus:dev
```

- API: <http://localhost:8080>
- Swagger UI: <http://localhost:8080/q/swagger-ui/>
- Dev UI: <http://localhost:8080/q/dev/>

### Build

```shell
./mvnw package
```

O JAR gerado fica em `target/quarkus-app/quarkus-run.jar`.

### Native (GraalVM)

```shell
./mvnw package -Dnative
```

---

## 🔐 Segurança

- Senhas hasheadas com **Argon2ID**.
- Autenticação via **JWT** com expiração configurável.
- **HTTPS obrigatório** em produção.
- **Isolamento total** por `user_id` — todas as queries filtram pelo usuário autenticado.

## 📁 Estrutura do Projeto

```
src/main/java/br/com/mathewfinance/
├── model/          # Entidades JPA/Hibernate (User, Transaction, Category, MerchantMapping)
├── dto/            # Data Transfer Objects (requests e responses da API)
├── mapper/         # Conversores Entity ↔ DTO
├── repository/     # Repositórios Panache
├── resource/       # Controllers REST (JAX-RS)
└── service/        # Lógica de negócio (categorização automática, dashboard, etc.)
```

---

## 📖 Guias Relacionados

- [REST](https://quarkus.io/guides/rest) — Jakarta REST (JAX-RS)
- [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache) — Camada de acesso a dados
- [SmallRye JWT](https://quarkus.io/guides/security-jwt) — Autenticação JWT
- [Hibernate Validator](https://quarkus.io/guides/validation) — Bean Validation
- [OpenAPI & Swagger UI](https://quarkus.io/guides/openapi-swaggerui) — Documentação da API
