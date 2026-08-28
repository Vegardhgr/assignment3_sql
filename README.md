# Assignment 3 SQL

## Explanation

A Spring Boot console application that talks to a PostgreSQL database (`StreamFlix`)
over a plain JDBC `DataSource`. It demonstrates raw SQL through small repository
classes instead of an ORM.

On startup `DemoConsole` (a `CommandLineRunner`) executes a series of queries and
prints the results to the console.

### Database schema (`src/main/resources/sql/schema/tables.sql`)

| Table          | Purpose                                                                    |
| -------------- | -------------------------------------------------------------------------- |
| `Users`        | Registered users and their subscription type (`Free`, `Basic`, `Premium`). |
| `Movies`       | Movie catalogue (title, genre, release year, rating, duration).            |
| `WatchHistory` | Which user watched which movie and the completion percentage.              |
| `Reviews`      | User ratings and comments per movie.                                       |

Schema and seed data (`sql/data/testData.sql`) are applied automatically by
`spring.sql.init` when the app boots.

### Repositories

- **`UserRepository`** – `getAllUsers`, `getUserById`, `getUsersByName`,
  `getUsersPage`, `addUser`, `updateUserFirstNameLastName`
- **`MovieRepository`** – `getMostWatchedMovies`, `getMostWatchedGenreByUser`
- **`SubscriptionTypeRepository`** – `getNumberOfUsersInSubscriptionType`

## Installation

### Prerequisites

- Docker and Docker Compose

Both the PostgreSQL database and the Spring Boot app run from Docker Compose.

### Steps

```bash
cd demo
docker compose up --build
```

This starts three containers:

| Service    | Port   | Notes                                                                                                                                                                            |
| ---------- | ------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `postgres` | `5432` | PostgreSQL 16. Runs `sql/db_init/01_dbCreate.sql` on first start to create the `test` user and the `StreamFlix` database. Storage is `tmpfs`, so the data is reset on every run. |
| `app`      | `8080` | Built from `Dockerfile`. Waits for the `postgres` healthcheck, then applies the schema + seed SQL and runs `DemoConsole`.                                                        |
| `pgadmin`  | `5050` | Web UI, login `admin@admin.com` / `admin`.                                                                                                                                       |

Database credentials (see `application.properties`): user `test`, password `123456`,
database `StreamFlix`.

## Usage

### See the console output

The app runs its queries once at startup. Follow its logs:

```bash
docker compose logs -f app
```

### Inspect the database with pgAdmin

1. Open http://localhost:5050 and log in with `admin@admin.com` / `admin`.
2. Register a new server:
    - Host: `postgres`
    - Port: `5432`
    - Database: `StreamFlix`
    - Username: `test`
    - Password: `123456`

### Connect with psql

```bash
docker exec -it posty psql -U test -d StreamFlix
```

### Stop

```bash
docker compose down
```
