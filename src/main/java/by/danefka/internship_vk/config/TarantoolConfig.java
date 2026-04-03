package by.danefka.internship_vk.config;

import io.tarantool.client.box.TarantoolBoxClient;
import io.tarantool.client.factory.TarantoolFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarantoolConfig {

    @Bean
    public TarantoolBoxClient tarantoolClient(
            @Value("${tarantool.host:localhost}") String host,
            @Value("${tarantool.port:3301}") int port,
            @Value("${tarantool.username:app}") String username,
            @Value("${tarantool.password:app123}") String password
    ) {
        var builder = TarantoolFactory.box()
                .withHost(host)
                .withPort(port)
                .withUser(username);

        if (password != null && !password.isBlank()) {
            builder.withPassword(password);
        }

        try {
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}