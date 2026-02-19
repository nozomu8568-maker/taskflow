# TaskFlow API

Spring Boot を用いたタスク管理 REST API です。  
JWT認証・検索・ページング・ソート・永続化（H2 / PostgreSQL）・Docker 対応まで実装したポートフォリオプロジェクトです。

---

# 🚀 技術スタック

- Java 21
- Spring Boot 4
- Spring Data JPA
- Spring Security
- JWT（jjwt）
- H2 Database（file mode / ローカル用）
- PostgreSQL（Docker / 本番想定）
- Maven
- Swagger（OpenAPI）
- Docker / Docker Compose
- Lombok

---

# 🏗 アーキテクチャ

Client  
↓  
Controller  
↓  
Service  
↓  
Repository  
↓  
Database

## 設計ポイント

- Entity を直接外部公開せず DTO を使用
- Specification による動的検索
- Pageable によるページング実装
- GlobalExceptionHandler による例外共通化
- PageResponse によるレスポンス形式の統一
- JWT による stateless 認証
- DB切替（H2 / PostgreSQL）をプロファイルで分離

---

# 📦 主な機能

- タスクの CRUD
- ステータス管理（TODO / DOING / DONE）
- 優先度管理（LOW / MEDIUM / HIGH）
- キーワード検索（title / description）
- ステータス・優先度フィルタ
- 期限範囲検索
- ページング対応
- ソート対応
- JWT認証（Bearerトークン）
- 永続化
  - H2 file mode
  - PostgreSQL（Docker）
- Swagger による API ドキュメント生成

---

# 🔐 認証仕様（JWT）

1. `POST /api/auth/register` でユーザー作成
2. `POST /api/auth/login` でJWT発行
3. `/api/tasks/**` は Authorization: Bearer <token> が必須
4. 未認証アクセスは 403

---

# 🔌 エンドポイント

## 認証

| Method | URL | 説明 |
|--------|-----|------|
| POST | /api/auth/register | ユーザー登録 |
| POST | /api/auth/login | JWT取得 |

## タスク

| Method | URL | 説明 |
|--------|-----|------|
| POST | /api/tasks | タスク作成（認証必須） |
| GET | /api/tasks | 一覧 + 検索 + ページング（認証必須） |
| GET | /api/tasks/{id} | 単体取得（認証必須） |
| PUT | /api/tasks/{id} | 更新（認証必須） |
| DELETE | /api/tasks/{id} | 削除（認証必須） |

---

# 🖥 ローカル起動（H2）

※ ローカルは 8082 で起動（Dockerとポート衝突回避）

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

Swagger UI  
http://localhost:8082/swagger-ui/index.html

H2 Console  
http://localhost:8082/h2-console  
JDBC URL: jdbc:h2:file:./data/taskflow

---

# 🐳 Docker起動（PostgreSQL）

```bash
docker compose up --build
```

停止：

```bash
docker compose down
```

Swagger UI  
http://localhost:8081/swagger-ui/index.html

---

# 🧪 動作確認（curl例）

## 1. ユーザー登録

```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"kudo","password":"pass1234"}'
```

---

## 2. ログイン（JWT取得）

```bash
TOKEN=$(curl -s -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"kudo","password":"pass1234"}' | jq -r .token)

echo $TOKEN
```

---

## 3. タスク一覧取得（JWT付き）

```bash
curl http://localhost:8082/api/tasks \
  -H "Authorization: Bearer $TOKEN"
```

---

## 4. タスク作成（JWT付き）

```bash
curl -X POST http://localhost:8082/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title":"JWT確認",
    "description":"ok",
    "status":"TODO",
    "priority":"HIGH",
    "dueDate":"2026-02-28"
  }'
```

---

## 5. 検索 + ページング + ソート

```bash
curl -G "http://localhost:8082/api/tasks" \
  -H "Authorization: Bearer $TOKEN" \
  --data-urlencode "q=JWT" \
  --data-urlencode "page=0" \
  --data-urlencode "size=10" \
  --data-urlencode "sort=dueDate,desc"
```

---

# 🐳 Docker構成

taskflow-app（Spring Boot / profile=postgres / port=8081）  
↓  
PostgreSQL 16（taskflow DB）

---

# 📁 永続化

H2  
./data/taskflow.mv.db

PostgreSQL  
Docker Volume: taskflow-pgdata

---

# 🎯 設計思想

- 実務レベルを意識したレイヤードアーキテクチャ
- Stateless JWT認証
- DB切替可能な設計
- 検索条件追加が容易なSpecification設計
- 保守性・拡張性を意識

---

# 🔮 今後の拡張

- ユーザーごとのタスク分離（owner管理）
- 権限管理（ROLE_USER / ROLE_ADMIN）
- CI/CD（GitHub Actions）
- AWSデプロイ
- フロントエンド（React / Next.js）
- マイグレーション管理（Flyway）

---

# 💡 このプロジェクトで証明できること

- REST API 設計力
- JWT認証実装力
- 検索 + ページング + ソート実装力
- DB切替設計（H2 → PostgreSQL）
- Docker対応スキル
- 実務レベルの構造設計理解

---

作成者: Nozomu Kudo
