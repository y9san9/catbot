package me.y9san9.db.migrations

import org.jetbrains.exposed.sql.Database


object MigrationsApplier {
    suspend fun apply (
        database: Database,
        migrations: List<DatabaseMigration>,
        config: MigrationsConfig.() -> Unit = {}
    ) {
        val configObject = MigrationsConfig().apply(config)
        val storage = MigrationsStorage(database, configObject.tableName)
        val version = storage.getVersion() ?: configObject.defaultVersion

        tailrec suspend fun migrate(version: Int): Int {
            val newVersion = migrations
                .firstOrNull { it.applyVersion == version }
                ?.apply { migrate(database) }
                ?.afterVersion ?: return version

            return migrate(newVersion)
        }

        if(version == null) {
            // Up to date, so saving the latest version
            (migrations.maxOfOrNull(DatabaseMigration::afterVersion) ?: 0)
                .let(storage::setVersion)
        } else {
            storage.setVersion(migrate(version))
        }
    }
}
