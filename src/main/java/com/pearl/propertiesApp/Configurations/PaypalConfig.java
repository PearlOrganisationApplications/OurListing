package com.pearl.propertiesApp.Configurations;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode; // sandbox or live

    @Bean
    public OAuthTokenCredential authTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, sdkConfig());
    }
    public APIContext getAPIContext() {
        return new APIContext(clientId, clientSecret, "sandbox"); // or "live"
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(authTokenCredential().getAccessToken());
        context.setConfigurationMap(sdkConfig());
        return context;
    }

    private Map<String, String> sdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);
        return configMap;
    }
}
