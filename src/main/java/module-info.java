module org.example.sysedu {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires org.controlsfx.controls;
    requires sisu.guice;
    requires com.jfoenix;
    requires javafaker;
    requires java.desktop;
    requires modelmapper;

    opens org.example.sysedu.controller;
    opens org.example.sysedu to javafx.fxml;
    opens org.example.sysedu.entity;
    opens org.example.sysedu.dto.topics;
    opens org.example.sysedu.dto.students;
    opens org.example.sysedu.dto.learners;
    opens org.example.sysedu.dto.courses;
    opens org.example.sysedu.controller.dialog.users;
    opens org.example.sysedu.controller.dialog.topics;
    opens org.example.sysedu.controller.dialog.courses;
    opens org.example.sysedu.controller.dialog.students;
    opens org.example.sysedu.controller.dialog.learners;
    opens org.example.sysedu.utils;

    exports org.example.sysedu;
    exports org.example.sysedu.dto.topics;
    exports org.example.sysedu.dto.students;
    exports org.example.sysedu.dto.learners;
    exports org.example.sysedu.dto.courses;
    exports org.example.sysedu.controller;
    exports org.example.sysedu.controller.components;
    exports org.example.sysedu.controller.dialog.users;
    exports org.example.sysedu.controller.dialog.topics;
    exports org.example.sysedu.controller.dialog.courses;
    exports org.example.sysedu.controller.dialog.students;
    exports org.example.sysedu.controller.dialog.learners;
    exports org.example.sysedu.utils;

    opens org.example.sysedu.controller.components;
}