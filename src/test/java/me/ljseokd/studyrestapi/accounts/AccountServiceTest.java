package me.ljseokd.studyrestapi.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static me.ljseokd.studyrestapi.accounts.AccountRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;
    
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

        accountRepository.save(account);

        //When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Then
        assertThat(userDetails.getPassword()).isEqualTo(password);


    }


}