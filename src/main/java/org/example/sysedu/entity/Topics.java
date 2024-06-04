package org.example.sysedu.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Topics", schema = "training_management")
public class Topics {

    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(name = "tuition_fee", nullable = false)
    private BigDecimal tuitionFee;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "avatar", nullable = false, columnDefinition = "LONGTEXT")
    private String avatar;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "isDelete", nullable = false)
    private Boolean isDelete;

    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

}
