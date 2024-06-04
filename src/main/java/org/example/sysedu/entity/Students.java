package org.example.sysedu.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Students", schema = "training_management")
public class Students {
    @Id
    @Column(name = "id", nullable = false, length = 30)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "learners_id", nullable = false, length = 30)
    private String learnersId;

    @Column(name = "courses_id", nullable = false, length = 30)
    private String coursesId;

    @Column(name = "score", nullable = false)
    private Float score;

    @Column(name = "dateCreated", nullable = false)
    private Timestamp dateCreated;

    @ManyToOne
    @JoinColumn(name = "learners_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Learners learner;

    @ManyToOne
    @JoinColumn(name = "courses_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Courses courses;
}
