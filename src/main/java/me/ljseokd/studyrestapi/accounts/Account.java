package me.ljseokd.studyrestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;

    @ElementCollection(fetch = EAGER)
    @Enumerated(value = STRING)
    private Set<AccountRole> roles;
}
