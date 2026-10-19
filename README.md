# Lost and Found — Porto

Lost and found application for the district of Porto, developed in Java with Hibernate and MySQL. It allows registering users and found items, searching for them by municipality, and managing administrators.

**Stack:** Java 27 · Maven · Hibernate ORM 6.4.4.Final · MySQL Connector/J 8.3.0

## Current Status

| Works | Missing |
|---|---|
| Hibernate entities and mappings | User interface (menu or web) |
| Hibernate startup and MySQL connection | Authentication (login) |
| Creation of users and administrators (US02, US08) | Search with more filters (category, status, color) |
| Publication of items (US04) | Usage of `RegistoContacto` in the application flow |
| Search for items by municipality (US01) | Password hashing |

## Prerequisites

- JDK 27 (version defined in `pom.xml`)
- Maven
- Running MySQL

## Configuration

1. Create the database:
```sql
   CREATE DATABASE objetos_perdidos_db;
```
2. Edit `src/main/resources/hibernate.cfg.xml` and adjust the URL, username, and password to your environment.
3. Do not commit real credentials to the repository.

With `hibernate.hbm2ddl.auto=update`, tables are automatically created or updated on the first startup.

## Build and Run

```bash
mvn compile
```

Run the `org.example.Main` class in your IDE. If the `pom.xml` has the `exec-maven-plugin`, you can also use:

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

## What `Main` does

When executed, the program runs a demonstration in 4 steps:

1. **Startup:** initializes Hibernate and opens the session.
2. **Initial data:** automatically creates, if they don't already exist:
   - the "Supreme Admin" (`admin@upt.pt`, password `"1234"`, document `"000000000"`);
   - the regular user `esteban@upt.pt` (US02);
   - an example item, published by that user (US04).
3. **Public search (US01):** lists items from the municipality of Porto, with title, category, and publisher.
4. **Administrator management (US08):**
   - a `USER` tries to create an `ADMIN` and is **refused**;
   - an `ADMIN` successfully creates a new `ADMIN`.

At the end, it closes the session and the database connection. Initial data is only created once, so the program can be run multiple times. On a second run, the creation of `admin2@upt.pt` fails because the email already exists, and the program displays an error message.

> **Warning:** demonstration accounts use simple passwords saved in plain text. They are intended for development purposes only.

## Structure

```
src/main/java/org/example/
├── Main.java        # entry point and demonstration
└── modelo/          # entities and enumerations
src/main/resources/
└── hibernate.cfg.xml
pom.xml
```

## Data Model

| Entity | Table | Description |
|---|---|---|
| `ObjetoAchado` | `objetos_achados` | Found item (title, description, category, color, municipality, location, date, contact, image, status). |
| `Utilizador` | `utilizadores` | Registered user (name, email, password, document, profile). Email and document are unique. |
| `RegistoContacto` | `registos_contacto` | Contact lookup (date/time, user, item). |

**Relations** (all many-to-one):
`ObjetoAchado → Utilizador` · `RegistoContacto → Utilizador` · `RegistoContacto → ObjetoAchado`

**Enumerations** (saved by name, `EnumType.STRING`):
`CategoriaObjeto` · `ConcelhoPorto` (18 municipalities) · `EstadoObjeto` (`ABERTO`, `RESOLVIDO`, `ARQUIVADO`) · `Perfil` (`USER`, `ADMIN`)

## Business Rules

- Only users with the `ADMIN` profile can create other `ADMIN` accounts (US08).
- Email and identification document cannot be duplicated.

## Next Steps

- [ ] Menu or interface to use the application
- [ ] Login and password hashing
- [ ] Search by category, status, and color
- [ ] Record contact lookups (`RegistoContacto`)
