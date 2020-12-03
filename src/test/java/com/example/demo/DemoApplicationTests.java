package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@DataJpaTest
@Testcontainers
@ContextConfiguration(classes = {
        DemoApplicationTests.Config.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DemoApplicationTests {

    @Autowired
    private Artifacts artifacts;

    @Test
    void contextLoads() {
        assertThat(artifacts).isNotNull();
    }

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:5.7");

    @Configuration
    static class Config {
        @Bean
        public DataSource dataSource() {

            final DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(MYSQL.getJdbcUrl());
            dataSource.setUsername(MYSQL.getUsername());
            dataSource.setPassword(MYSQL.getPassword());

            await().pollInterval(Duration.ofSeconds(2L))
                    .atMost(Duration.ofMinutes(2L))
                    .until(() -> {
                        try (final Connection c = dataSource.getConnection()) {
                            c.prepareStatement("select 1 from dual");
                            return true;
                        } catch (SQLException e) {
                            return false;
                        }
                    });

            return dataSource;

        }

    }

}
