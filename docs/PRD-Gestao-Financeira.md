# PRD — Produto de Gestão Financeira Pessoal

**Versão:** 1.0
**Data:** Julho/2026
**Status:** Rascunho para validação

---

## 1. Visão Geral

### 1.1 Problema
Pessoas físicas têm dificuldade em manter controle consistente de seus gastos e entradas financeiras. Ferramentas existentes (planilhas, apps genéricos) exigem esforço manual repetitivo, principalmente na categorização de despesas, o que leva ao abandono do hábito de registro após poucas semanas.

### 1.2 Visão do Produto
Uma plataforma de gestão financeira pessoal que reduz o atrito do registro de transações através de categorização inteligente e, no futuro, captura automática via notificações do celular — eliminando a necessidade de digitação manual repetitiva.

### 1.3 Proposta de Valor
- Registro rápido de gastos e entradas.
- Categorização que "aprende" com o comportamento do usuário: uma vez que um estabelecimento é categorizado, novas compras no mesmo local são categorizadas automaticamente.
- Evolução natural para captura automática via notificações (fase futura), sem precisar redesenhar a lógica de negócio.

### 1.4 Objetivos do MVP
1. Validar se usuários mantêm o hábito de registro manual quando a fricção de categorização é reduzida.
2. Validar a lógica de categorização automática por estabelecimento como diferencial percebido.
3. Coletar aprendizado de uso real antes de investir em app mobile e captura de notificações.

### 1.5 Fora de Escopo (MVP)
- Aplicativo mobile nativo ou híbrido.
- Captura automática de notificações de gasto/entrada.
- Integração bancária via Open Finance.
- Múltiplas moedas.
- Contas compartilhadas/família.
- Metas de economia, orçamentos, relatórios avançados de BI.

---

## 2. Personas

### Persona Primária — "Usuário Organizador Iniciante"
- Pessoa física, 22–40 anos, renda variável.
- Já tentou controlar gastos em planilha ou app, mas abandonou por trabalho manual excessivo.
- Quer visão simples de "quanto entrou, quanto saiu, em quê".
- Baixa tolerância a fricção: se o registro de uma transação leva mais de 15 segundos, tende a desistir.

---

## 3. Escopo do MVP

### 3.1 Resumo
Aplicação **web** (frontend Next.js + backend Quarkus) onde o usuário:
1. Cria conta e faz login.
2. Registra lançamentos manuais de gastos e entradas.
3. Categoriza cada lançamento — na primeira vez que compra em um estabelecimento, escolhe a categoria; nas compras seguintes no mesmo estabelecimento, a categoria é sugerida automaticamente.
4. Visualiza seus lançamentos em uma listagem/dashboard simples.

### 3.2 Funcionalidades incluídas
| # | Funcionalidade | Prioridade |
|---|---|---|
| F1 | Cadastro e login de usuário (e-mail/senha) | P0 |
| F2 | CRUD de lançamentos (gasto/entrada) | P0 |
| F3 | Cadastro e gestão de categorias (padrão + customizadas) | P0 |
| F4 | Categorização automática por estabelecimento recorrente | P0 |
| F5 | Listagem de lançamentos com filtros (período, categoria, tipo) | P0 |
| F6 | Dashboard resumo (total de entradas, saídas, saldo do período) | P1 |
| F7 | Edição de categoria de um estabelecimento (recategorizar retroativamente) | P1 |
| F8 | Exportação de lançamentos (CSV) | P2 |

---

## 4. Requisitos Funcionais Detalhados

### RF01 — Autenticação
- Usuário se cadastra com nome, e-mail e senha.
- Login via e-mail/senha, sessão via JWT.
- Recuperação de senha via e-mail (pode ficar para P1 se o tempo apertar).

### RF02 — Lançamento manual de transação
Campos do lançamento:
- Tipo: `GASTO` ou `ENTRADA`.
- Valor (decimal, positivo).
- Data da transação.
- Estabelecimento/Descrição (texto livre, ex: "Supermercado Pague Menos").
- Categoria (associada automaticamente ou escolhida manualmente).
- Observação (opcional).

Fluxo:
1. Usuário clica em "Novo lançamento".
2. Preenche tipo, valor, data e descrição do estabelecimento.
3. Sistema normaliza o texto do estabelecimento (lowercase, remove acentos/pontuação/espaços extras) e busca correspondência na tabela de mapeamento do próprio usuário.
4. **Se encontrar correspondência:** categoria é preenchida automaticamente e exibida como sugestão editável.
5. **Se não encontrar:** usuário escolhe a categoria manualmente; o par (estabelecimento normalizado → categoria) é salvo para uso futuro.
6. Usuário confirma e o lançamento é salvo.

### RF03 — Categorização automática (regra de negócio central do produto)
- O matching é **por usuário** (o mesmo estabelecimento pode ter categorias diferentes para usuários diferentes).
- Estratégia de matching, em ordem:
  1. Correspondência exata do texto normalizado.
  2. Correspondência por similaridade (ex: `pg_trgm`/similarity no Postgres, ou Levenshtein) acima de um limiar configurável (ex: 85%), para lidar com pequenas variações ("Pague Menos Norte" vs "Pague Menos - Filial Norte").
- Se o usuário corrigir manualmente a categoria sugerida, o sistema atualiza o mapeamento para esse estabelecimento (o sistema deve "reaprender").
- Usuário pode, a qualquer momento, ver e editar a lista de mapeamentos estabelecimento→categoria (RF07).

### RF04 — Categorias
- Categorias padrão pré-cadastradas (ex: Alimentação, Transporte, Moradia, Saúde, Lazer, Salário, Outros).
- Usuário pode criar categorias customizadas.
- Cada categoria tem: nome, tipo (gasto/entrada/ambos), cor/ícone (opcional, para UI).

### RF05 — Listagem e filtros
- Lista paginada de lançamentos.
- Filtros: intervalo de datas, categoria, tipo (gasto/entrada), busca por texto no estabelecimento.
- Ordenação por data (padrão: mais recente primeiro).

### RF06 — Dashboard resumo
- Total de entradas no período selecionado.
- Total de gastos no período selecionado.
- Saldo (entradas - gastos).
- Distribuição de gastos por categoria (gráfico simples, ex: pizza ou barras).

### RF07 — Gestão de mapeamento estabelecimento→categoria
- Tela onde o usuário vê todos os estabelecimentos já categorizados.
- Pode editar a categoria de um estabelecimento — o sistema pergunta se deve recategorizar lançamentos passados daquele estabelecimento também (opcional, mas recomendado para consistência).

---

## 5. Requisitos Não Funcionais

| Categoria | Requisito |
|---|---|
| Performance | Resposta de API < 500ms para operações de CRUD em condições normais |
| Disponibilidade | MVP pode aceitar cold start (infra free tier); não é crítico 24/7 nesta fase |
| Segurança | Senhas com hash (bcrypt/argon2); JWT com expiração; HTTPS obrigatório |
| Privacidade | Dados financeiros são sensíveis — sem compartilhamento entre usuários; isolamento total por `user_id` em todas as queries |
| Escalabilidade | Arquitetura deve permitir evolução para múltiplos clientes (mobile) sem redesenho do backend |
| Usabilidade | Registro de um lançamento completo em até 3 interações/cliques após preencher os campos |

---

## 6. Modelo de Dados (proposta inicial)

```
users
- id (uuid, pk)
- name
- email (unique)
- password_hash
- created_at

categories
- id (uuid, pk)
- user_id (fk -> users, nullable para categorias padrão do sistema)
- name
- type (GASTO | ENTRADA | AMBOS)
- color
- created_at

transactions
- id (uuid, pk)
- user_id (fk -> users)
- type (GASTO | ENTRADA)
- amount (decimal)
- description (texto original informado pelo usuário)
- transaction_date
- category_id (fk -> categories)
- merchant_mapping_id (fk -> merchant_mappings, nullable)
- created_at

merchant_mappings
- id (uuid, pk)
- user_id (fk -> users)
- normalized_merchant_name (texto normalizado, indexado)
- category_id (fk -> categories)
- created_at
- updated_at
```

**Índices recomendados:**
- `merchant_mappings (user_id, normalized_merchant_name)` — unique composto.
- Extensão `pg_trgm` habilitada no Postgres para busca por similaridade em `normalized_merchant_name`.

---

## 7. Arquitetura e Stack Técnica

| Camada | Tecnologia | Observação |
|---|---|---|
| Frontend | Next.js | SSR/CSR conforme necessidade; consome API REST do backend |
| Backend | Java + Quarkus | API REST (JAX-RS/RESTEasy Reactive); baixo consumo de memória, ideal para infra de baixo custo |
| Banco de dados | PostgreSQL | Uso de `pg_trgm` para matching por similaridade |
| Autenticação | JWT (SmallRye JWT no Quarkus) | Sessão stateless |
| ORM | Hibernate ORM with Panache (Quarkus) | Produtividade no acesso a dados |

### Infraestrutura de deploy (baixo custo)
| Camada | Serviço | Custo inicial |
|---|---|---|
| Frontend | Vercel (Free/Hobby) | R$ 0 |
| Backend | Render (Free tier, com cold start) | R$ 0 → US$ 7/mês quando precisar de always-on |
| Banco de dados | Neon (Free tier, Postgres serverless) | R$ 0 |

---

## 8. Métricas de Sucesso do MVP

| Métrica | Meta inicial |
|---|---|
| Retenção D7 (usuários que voltam a registrar lançamento em 7 dias) | ≥ 30% |
| Nº médio de lançamentos por usuário ativo/semana | ≥ 5 |
| % de lançamentos que usam a categoria sugerida automaticamente (sem edição) | ≥ 50% (indica que o matching está funcionando) |
| Taxa de abandono no cadastro de lançamento (iniciou mas não concluiu) | < 15% |

---

## 9. Roadmap Futuro (pós-MVP)

| Fase | Entrega |
|---|---|
| Fase 2 | App mobile (captura de notificações de gasto/entrada, com parsing automático do valor e estabelecimento) |
| Fase 2 | Reaproveitamento da engine de categorização (`merchant_mappings`) para os lançamentos capturados automaticamente |
| Fase 3 | Metas e orçamentos por categoria |
| Fase 3 | Relatórios e insights (ex: "você gastou 20% a mais em Lazer este mês") |
| Fase 4 | Integração via Open Finance (importação automática de extratos bancários) |

---

## 10. Riscos e Mitigações

| Risco | Mitigação |
|---|---|
| Usuário não sustenta o hábito de registro manual | Reduzir fricção ao máximo (categorização automática, poucos campos obrigatórios) |
| Matching por similaridade gera categorização incorreta | Permitir correção fácil e retroativa; ajustar limiar de similaridade com base em feedback |
| Cold start do backend (infra free) prejudica percepção de qualidade | Aceitável na fase de validação; migrar para plano pago assim que houver tração |
| Dados financeiros sensíveis exigem cuidado extra de segurança | Isolamento rígido por `user_id`, hashing de senha, HTTPS obrigatório desde o MVP |

---

## 11. Próximos Passos
1. Validar este PRD e priorizar funcionalidades P0 para o primeiro sprint.
2. Modelar o schema do banco (scripts de migration).
3. Definir contratos de API (endpoints REST) entre frontend e backend.
4. Estruturar os projetos (Next.js e Quarkus) e configurar pipelines de deploy.
