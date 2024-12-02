package com.example.onlinebookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String DB_IMAGE = "mysql:8.0.33";
    private static CustomMySqlContainer instance;

    private CustomMySqlContainer() {
        super(DB_IMAGE);
        this.withDatabaseName("book_store")
                  .withUsername("test")
                  .withPassword("test");
    }

    public static CustomMySqlContainer getInstance() {
        if (instance == null) {
            instance = new CustomMySqlContainer();
            instance.start();
        }
        return instance;
    }

    @Override
    public void stop() {
    }
}
