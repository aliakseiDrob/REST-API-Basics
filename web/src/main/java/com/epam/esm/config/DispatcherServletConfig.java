package com.epam.esm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherServletConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    public static final String ALL_REQUESTS = "/";
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringJdbcConfig.class, ServiceConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfiguration.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ALL_REQUESTS};
    }
}
