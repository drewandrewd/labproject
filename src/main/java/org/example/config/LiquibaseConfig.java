package org.example.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.sql.SQLException;

public class LiquibaseConfig {

    private static final String CHANGELOG_FILE = "db.changelog/changelog.xml";

    public static void runMigrations() {
        try (Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(DatabaseConfig.getConnection())))
        {
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
            Liquibase liquibase = new Liquibase(CHANGELOG_FILE, resourceAccessor, database);
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }
}
