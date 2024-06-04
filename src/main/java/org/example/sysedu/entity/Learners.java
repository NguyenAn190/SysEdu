package org.example.sysedu.entity;

import lombok.*;
import org.example.sysedu.enums.Role;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Learners", schema = "training_management")
public class Learners {
    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "note", columnDefinition = "LONGTEXT")
    private String note;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

}
