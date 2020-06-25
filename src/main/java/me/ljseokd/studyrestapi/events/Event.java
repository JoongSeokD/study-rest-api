package me.ljseokd.studyrestapi.events;

import lombok.*;
import me.ljseokd.studyrestapi.accounts.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    @ManyToOne(fetch = LAZY)
    private Account manager;

    @Enumerated(STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        //Update Free
        if (basePrice == 0 && maxPrice == 0){
            free = true;
        }else {
            free = false;
        }

        //Update offline JAVA11 Version isBlank()
        if (location == null || location.isBlank()){
            offline = false;
        }else {
            offline = true;
        }
    }
}
