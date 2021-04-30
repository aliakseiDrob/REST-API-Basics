package com.epam.esm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.esm.service"})
@EnableTransactionManagement
public class ServiceConfiguration {
}
