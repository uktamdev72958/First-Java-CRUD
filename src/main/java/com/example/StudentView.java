package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Route("")
public class StudentView extends VerticalLayout {

    private final Grid<Student> grid = new Grid<>(Student.class, false);
    private final TextField nameField = new TextField("Student name");
    private final TextField emailField = new TextField("Email");
    private final TextField majorField = new TextField("Major");

    private final List<Student> students = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    private Student selectedStudent;

    public StudentView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Student Management System"));

        grid.addColumn(Student::getId).setHeader("ID");
        grid.addColumn(Student::getName).setHeader("Name");
        grid.addColumn(Student::getEmail).setHeader("Email");
        grid.addColumn(Student::getMajor).setHeader("Major");
        grid.setWidthFull();

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedStudent = event.getValue();

            if (selectedStudent != null) {
                nameField.setValue(selectedStudent.getName());
                emailField.setValue(selectedStudent.getEmail());
                majorField.setValue(selectedStudent.getMajor());
            }
        });

        Button addButton = new Button("Add Student", event -> addStudent());
        Button updateButton = new Button("Update Student", event -> updateStudent());
        Button deleteButton = new Button("Delete Student", event -> deleteStudent());
        Button clearButton = new Button("Clear", event -> clearForm());

        HorizontalLayout form = new HorizontalLayout(
                nameField, emailField, majorField
        );
        form.setWidthFull();

        HorizontalLayout buttons = new HorizontalLayout(
                addButton, updateButton, deleteButton, clearButton
        );

        add(form, buttons, grid);

        // Example data
        students.add(new Student(
                nextId.getAndIncrement(),
                "Ali Karimov",
                "ali@example.com",
                "Computer Engineering"
        ));

        students.add(new Student(
                nextId.getAndIncrement(),
                "John Smith",
                "john@example.com",
                "Artificial Intelligence"
        ));

        refreshGrid();
    }

    private void addStudent() {
        if (!validateForm()) {
            return;
        }

        students.add(new Student(
                nextId.getAndIncrement(),
                nameField.getValue().trim(),
                emailField.getValue().trim(),
                majorField.getValue().trim()
        ));

        refreshGrid();
        clearForm();
        Notification.show("Student added successfully!");
    }

    private void updateStudent() {
        if (selectedStudent == null) {
            Notification.show("Select a student to update.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        selectedStudent.setName(nameField.getValue().trim());
        selectedStudent.setEmail(emailField.getValue().trim());
        selectedStudent.setMajor(majorField.getValue().trim());

        refreshGrid();
        clearForm();
        Notification.show("Student updated successfully!");
    }

    private void deleteStudent() {
        if (selectedStudent == null) {
            Notification.show("Select a student to delete.");
            return;
        }

        students.remove(selectedStudent);
        refreshGrid();
        clearForm();
        Notification.show("Student deleted successfully!");
    }

    private boolean validateForm() {
        if (nameField.getValue().isBlank()
                || emailField.getValue().isBlank()
                || majorField.getValue().isBlank()) {
            Notification.show("Please fill in all fields.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        majorField.clear();
        grid.deselectAll();
        selectedStudent = null;
    }

    private void refreshGrid() {
        grid.setItems(new ArrayList<>(students));
    }

    public static class Student {
        private long id;
        private String name;
        private String email;
        private String major;

        public Student() {
        }

        public Student(long id, String name, String email, String major) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.major = major;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }
    }
}

