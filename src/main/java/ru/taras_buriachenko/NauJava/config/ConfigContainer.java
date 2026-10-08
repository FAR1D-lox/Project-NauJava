package ru.taras_buriachenko.NauJava.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.taras_buriachenko.NauJava.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConfigContainer {

    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<User> userContainer() {
        return new ArrayList<>();
    }

}

