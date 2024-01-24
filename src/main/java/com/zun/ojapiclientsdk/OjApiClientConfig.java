package com.zun.ojapiclientsdk;

import cn.hutool.core.util.StrUtil;
import com.zun.ojapiclientsdk.client.OjApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan(basePackages = "com.zun.ojapiclientsdk")
@ConfigurationProperties("oj.api.client")
public class OjApiClientConfig {

    private String accessKey;

    private String secretKey;

    private String apiGatewayUrl;

    @Bean
    public OjApiClient ojApiClient() {
        return new OjApiClient(accessKey, secretKey, apiGatewayUrl);
    }
}
