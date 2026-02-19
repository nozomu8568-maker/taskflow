# TaskFlow API

Spring Boot 4 × JPA × Docker × PostgreSQL 対応の  
**実務想定タスク管理REST API** です。

設計・拡張性・再現性を重視し、  
検索・ページング・例外設計・プロファイル分離まで実装しています。

---

# 🚀 30秒で起動（Docker推奨）

```bash
docker compose up --build
```

Swagger:
http://localhost:8081/swagger-ui/index.html

PostgreSQLを含めた完全再現環境で起動します。

---

# 🏗 アーキテクチャ

```
Controller
   ↓
Service
   ↓
Repository
   ↓
Entity
```

## 設計上の工夫

- Entityを外部公開せずDTOで分離
- Specificationによる動的検索
- Pageableによるページング
- PageResponseでレスポンス形式を統一
- GlobalExceptionHandlerで例外共通化
- H2 / PostgreSQL プロファイル分離
- Docker + healthcheck による安定起動

---

# 🧠 技術スタック

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 (file mode)
- PostgreSQL 16
- Docker / Docker Compose
- OpenAPI (Swagger)
- Lombok

---

# 📌 主な機能

### CRUD
- タスク作成
- タスク取得
- タスク更新
- タスク削除

### 検索機能
- キーワード検索（title / description）
- ステータスフィルタ
- 優先度フィルタ
- 期限範囲検索
- ページング対応
- ソート対応

---

# 🔎 API使用例

## タスク作成

```bash
curl -X POST http://localhost:8081/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title":"TaskFlow確認",
    "description":"docker + postgres",
    "status":"TODO",
    "priority":"HIGH",
    "dueDate":"2026-03-01"
  }'
```

---

## タスク検索（URLエンコード推奨）

```bash
curl -G http://localhost:8081/api/tasks \
  --data-urlencode "q=確認" \
  --data-urlencode "page=0" \
  --data-urlencode "size=10" \
  --data-urlencode "sort=dueDate,desc"
```

---

# ⚙️ プロファイル構成

| Profile | Database |
|----------|-----------|
| h2 | ローカル検証用（file mode永続化） |
| postgres | Docker本番想定 |

### H2で起動する場合

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

---

# 🎯 設計意図

本プロジェクトは以下を目的としています：

- 実務レベルのレイヤード設計
- 検索拡張が容易なSpecification設計
- DB変更に耐えられる構造
- 環境差異のないDocker構成
- 保守性を意識した例外設計
- レスポンス構造の安定化

---

# 🔮 今後の拡張予定

- JWT認証（Spring Security）
- Flyway導入
- Reactフロントエンド接続
- AWSデプロイ
- CI/CD構築

---

# 📌 ポイント

- 学習用ではなく「実務設計」を意識
- DB切替可能な構成
- 再現性のあるDocker環境
- 拡張しやすい検索設計

---

## 👤 Author

Kudo Nozomu  
Backend Engineer Portfolio
