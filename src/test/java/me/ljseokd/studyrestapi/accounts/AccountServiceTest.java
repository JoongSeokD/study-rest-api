package me.ljseokd.studyrestapi.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static me.ljseokd.studyrestapi.accounts.AccountRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test 
    void findByUsername() throws Exception {
        //Given
        String password = "ljseokd";
        String username = "ljseokd@gmail.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(ADMIN, USER))
                .build();

        accountService.saveAccount(account);

        //When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Then
        assertThat(passwordEncoder.matches(password,userDetails.getPassword())).isTrue();


    }

    @Test
    void findByUsernameFail() throws Exception { 
        //given 
        String username = "asdwe@email.com";

        assertThrows(UsernameNotFoundException.class,()->{
            accountService.loadUserByUsername(username);
        });
        //when
         
        //then 
    }


}