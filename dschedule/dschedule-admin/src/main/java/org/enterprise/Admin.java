package org.enterprise;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("org.enterprise.infrastructure.mysql.mapper")
public class Admin {
    public static void main(String[] args) {
        SpringApplication.run(Admin.class, args);
    }
}
