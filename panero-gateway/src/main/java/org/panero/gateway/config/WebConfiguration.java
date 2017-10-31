package org.panero.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedMethods(HttpMethod.OPTIONS.name(), HttpMethod.GET.name(), HttpMethod.POST.name())
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE);
    }
}
