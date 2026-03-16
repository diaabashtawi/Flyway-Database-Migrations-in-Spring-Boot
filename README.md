# 🛢️ Flyway Database Migration with Spring Boot & MySQL

## 📖 Overview

This project demonstrates how to manage database schema changes in a controlled, versioned, and repeatable way using **Flyway** — the industry-standard database migration tool — integrated seamlessly with **Spring Boot** and **MySQL**.

Whether you're working solo or in a team, Flyway ensures every developer and every environment runs the exact same database schema, eliminating "works on my machine" problems.

---

## ✨ Features

- ✅ **Automatic schema versioning** on application startup
- ✅ **Ordered, incremental SQL migrations** (`V1__`, `V2__`, ...)
- ✅ **Repeatable migrations** for views, stored procedures, and seed data
- ✅ **Flyway history table** (`flyway_schema_history`) for full audit trail
- ✅ **Spring Boot auto-configuration** — zero boilerplate setup
- ✅ **Environment-specific configs** via `application.properties`

---

## 🛠️ Tech Stack

| Technology       | Version | Purpose                        |
|------------------|---------|--------------------------------|
| Java             | 17      | Core language                  |
| Spring Boot      | 3.x     | Application framework          |
| Flyway           | 10.x     | Database migration engine      |
| MySQL            | 8.x     | Relational database            |
| Maven            | 3.8+    | Build & dependency management  |

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [MySQL 8.x](https://dev.mysql.com/downloads/)
- [Git](https://git-scm.com/)

---

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/flyway-springboot-mysql.git
cd flyway-springboot-mysql
```

### 2. Create the MySQL Database

Log into MySQL and run:

```sql
CREATE DATABASE flyway_demo;
```

> ⚠️ Flyway will handle all table creation — do **not** create tables manually.

### 3. Configure the Database Connection

Edit `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/flyway_demo
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

### 4. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

On startup, Flyway will automatically detect and apply any pending migration scripts. 🎉

---

## 📁 Project Structure

```
flyway-springboot-mysql/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── FlywayDemoApplication.java      # Main entry point
│   │   │   ├── model/                           # JPA entity classes
│   │   │   ├── repository/                      # Spring Data repositories
│   │   │
│   │   └── resources/
│   │       ├── application.properties           # App & DB configuration
│   │       └── db/
│   │           └── migrations/
│   │               ├── V1__create_users_table.sql
│   │               ├── V2__add_email_column.sql
│   │               ├── V3__insert_seed_data.sql
│   │               └── R__create_user_view.sql  # Repeatable migration
│   │
│   └── test/
│       └── java/com/example/
│           └── FlywayMigrationTest.java
│
├── pom.xml
└── README.md
```

---

## 📜 Migration Scripts

Flyway uses a strict **naming convention** for migration files:

```
V{version}__{description}.sql       → Versioned (runs once)
R__{description}.sql                → Repeatable (runs when changed)
U{version}__{description}.sql       → Undo (requires Flyway Teams)
```

### Example Migrations

**`V1__create_users_table.sql`**
```sql
CREATE TABLE users (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**`V2__add_email_column.sql`**
```sql
ALTER TABLE users
ADD COLUMN email VARCHAR(255) NOT NULL UNIQUE;
```

**`V3__insert_seed_data.sql`**
```sql
INSERT INTO users (name, email) VALUES
    ('Alice Johnson', 'alice@example.com'),
    ('Bob Smith',     'bob@example.com');
```

**`R__create_user_view.sql`** *(Repeatable)*
```sql
CREATE OR REPLACE VIEW active_users AS
SELECT id, name, email FROM users;
```

---

## ⚙️ Configuration

### Full `application.properties` Reference

```properties
# ── DataSource ─────────────────────────────────────────────
spring.datasource.url=jdbc:mysql://localhost:3306/flyway_demo?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=secret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ── JPA ────────────────────────────────────────────────────
spring.jpa.hibernate.ddl-auto=validate        # Let Flyway own schema, not Hibernate
spring.jpa.show-sql=true

# ── Flyway ──────────────────────────────────────────────────
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

> 💡 **Tip:** Set `spring.jpa.hibernate.ddl-auto=validate` so Hibernate validates the schema without modifying it — let Flyway be the sole owner of schema changes.

---

## 📊 Flyway Schema History

After running the application, you can inspect the migration history directly in MySQL:

```sql
SELECT * FROM flyway_schema_history;
```

| installed_rank | version | description          | type | script                          | checksum   | success |
|----------------|---------|----------------------|------|---------------------------------|------------|---------|
| 1              | 1       | create users table   | SQL  | V1__create_users_table.sql      | 1234567890 | 1       |
| 2              | 2       | add email column     | SQL  | V2__add_email_column.sql        | 0987654321 | 1       |
| 3              | 3       | insert seed data     | SQL  | V3__insert_seed_data.sql        | 1122334455 | 1       |

---

## 🔐 Best Practices

- **Never edit** a migration file after it has been applied — Flyway will detect the checksum mismatch and fail.
- **Always test** migrations on a staging database before production.
- **Use descriptive names** for migration files (`V2__add_email_to_users` not `V2__update`).
- **Keep migrations small** and focused — one change per file.
- **Store migration files in version control** alongside your application code.

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/my-migration`
3. Commit your changes: `git commit -m 'Add V4 migration for orders table'`
4. Push to the branch: `git push origin feature/my-migration`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with ❤️ using Spring Boot & Flyway

⭐ Star this repo if you found it helpful!

</div>
