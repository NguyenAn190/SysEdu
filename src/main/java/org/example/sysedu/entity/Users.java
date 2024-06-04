package org.example.sysedu.entity;
import javax.persistence.*;
import java.sql.Timestamp;

import lombok.*;
import org.example.sysedu.enums.Role;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Users",  schema = "training_management")
public class Users {
    @Id
    @Column(name = "id", length = 30)
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullName")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Column(name = "date_created")
    private Timestamp dateCreated;

}
