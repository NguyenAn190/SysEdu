package org.example.sysedu.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Courses", schema = "training_management")
public class Courses {
    @Id
    @Column(name = "id", length = 30)
    private String id;

    @Column(name = "courseName", nullable = false)
    private String courseName;

    @Column(name = "topic_id", nullable = false, length = 30)
    private String topicId;

    @Column(name = "users_id", nullable = false, length = 30)
    private String usersId;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "tuition_fee", nullable = false)
    private BigDecimal tuitionFee;

    @Column(name = "declaration_date", nullable = false)
    private LocalDate declarationDate;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Topics topics;

}
