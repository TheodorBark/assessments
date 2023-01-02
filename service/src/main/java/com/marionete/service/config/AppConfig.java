package com.marionete.service.config;

import io.github.majusko.grpc.jwt.GrpcJwtProperties;
import io.github.majusko.grpc.jwt.interceptor.AuthClientInterceptor;
import io.github.majusko.grpc.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ImportAutoConfiguration({
        net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration.class,
        net.devh.boot.grpc.common.autoconfigure.GrpcCommonTraceAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcAdviceAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcHealthServiceAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcMetadataConsulConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcMetadataEurekaConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcMetadataNacosConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcMetadataZookeeperConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcReflectionServiceAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcServerMetricAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration.class,
        net.devh.boot.grpc.server.autoconfigure.GrpcServerTraceAutoConfiguration.class
})
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public GrpcJwtProperties grpcJwtProperties() {
        return new GrpcJwtProperties();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService(environment, grpcJwtProperties());
    }

    @Bean
    public AuthClientInterceptor authClientInterceptor() {
        return new AuthClientInterceptor(jwtService());
    }
}
