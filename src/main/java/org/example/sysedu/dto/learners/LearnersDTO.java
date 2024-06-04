package org.example.sysedu.dto.learners;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LearnersDTO {
    private String id;
    private String fullName;
    private Boolean gender;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String note;
    private Timestamp dateCreated;
    private Boolean isDelete;
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    public Boolean getSelected() {
        return selected.get();
    }

    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
