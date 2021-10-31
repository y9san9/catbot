# DB Migrations
## Structure

The migrations are designed with [MigrationsApplier](src/main/kotlin/me/y9san9/db/migrations/MigrationsApplier.kt) as main entity. It performs all migrations in recursion-way (because personally I hate cycles and mutable state)

[MigrationsStorage](src/main/kotlin/me/y9san9/db/migrations/MigrationsStorage.kt) is a simple set-get table model for tracking schema version
