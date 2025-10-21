package back.code.config;

import back.code.common.listener.P6SpyEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6SpyConfig {

    @Bean
    public P6SpyEventListener p6SpyEventListener() {
        return new P6SpyEventListener();
    }

    @Bean
    public P6sypSqlFormatter p6sypSqlFormater() {
        return new P6sypSqlFormatter();
    }
}