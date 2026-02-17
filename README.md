# Taskflow API

Spring Boot を用いたタスク管理REST APIです。  
検索・ページング・ソート・永続化・Docker対応まで実装したポートフォリオプロジェクトです。

---

## 技術スタック

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 Database（file mode）
- Maven
- Swagger（OpenAPI）
- Docker / Docker Compose
- Lombok

---

## アーキテクチャ構成

Controller  
↓  
Service  
↓  
Repository  
↓  
Entity

- Entityを直接外部公開せずDTOを使用
- Specificationによる動的検索
- Pageableによるページング実装
- GlobalExceptionHandlerによる例外共通化
- PageResponseによるレスポンス固定

---

## 主な機能

- タスクのCRUD
- ステータス管理（TODO / DOING / DONE）
- 優先度管理（LOW / MEDIUM / HIGH）
- キーワード検索（title / description）
- ステータス・優先度フィルタ
- 期限範囲検索
- ページング対応
- ソート対応
- 永続化（H2 file mode）
- SwaggerによるAPIドキュメント生成
- Dockerによるコンテナ実行対応

---

## ローカルでの起動

```bash
./mvnw clean spring-boot:run
```

Swagger UI  
http://localhost:8081/swagger-ui/index.html

---

## Dockerでの起動

起動：

```bash
docker compose up --build
```

停止：

```bash
Ctrl + C
docker compose down
```

Swagger UI  
http://localhost:8081/swagger-ui/index.html

---

## 動作確認例

### タスク作成

```bash
curl -X POST http://localhost:8081/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title":"サンプルタスク",
    "description":"説明",
    "status":"TODO",
    "priority":"HIGH",
    "dueDate":"2026-02-28"
  }'
```

### 検索 + ページング

```bash
curl "http://localhost:8081/api/tasks?q=サンプル&page=0&size=10&sort=dueDate,desc"
```

---

## 設計方針

- 実務を想定したレイヤードアーキテクチャ
- DB変更（PostgreSQL等）を想定した構成
- 拡張性を考慮した設計
- 検索条件追加が容易なSpecification実装

---

## 今後の拡張予定

- PostgreSQL対応
- Spring Securityによる認証機能追加
- フロントエンド（React等）実装
- CI/CD導入

---

本プロジェクトは、バックエンドAPI設計力・保守性・拡張性を意識して構築したポートフォリオです。
