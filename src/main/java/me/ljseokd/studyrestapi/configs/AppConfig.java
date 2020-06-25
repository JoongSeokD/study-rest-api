package me.ljseokd.studyrestapi.configs;

import me.ljseokd.studyrestapi.accounts.Account;
import me.ljseokd.studyrestapi.accounts.AccountRole;
import me.ljseokd.studyrestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static me.ljseokd.studyrestapi.accounts.AccountRole.*;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account ljseokd = Account.builder()
                        .email("ljseokd@gmail.com")
                        .password("ljseokd")
                        .roles(Set.of(ADMIN, USER))
                        .build();
                accountService.saveAccount(ljseokd);
            }
        };
    }
}
