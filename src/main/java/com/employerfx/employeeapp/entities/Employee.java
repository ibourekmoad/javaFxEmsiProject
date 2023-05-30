package com.employerfx.employeeapp.entities;

import javafx.beans.property.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Employee {
    private final LongProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final DoubleProperty salary;
    private final StringProperty phone;
    private List<String> skills;
    private final StringProperty jobTitle;
    private final BooleanProperty isManager;
    private final ObjectProperty<Date> hireDate;

    public Employee() {
        this.id = new SimpleLongProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.salary = new SimpleDoubleProperty();
        this.phone = new SimpleStringProperty();
        this.skills = null; // Replace with your implementation for skills
        this.jobTitle = new SimpleStringProperty();
        this.isManager = new SimpleBooleanProperty();
        this.hireDate = new SimpleObjectProperty<>();
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public double getSalary() {
        return salary.get();
    }

    public DoubleProperty salaryProperty() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary.set(salary);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public List<String> getSkills() {
        return skills;
    }
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getJobTitle() {
        return jobTitle.get();
    }

    public StringProperty jobTitleProperty() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle.set(jobTitle);
    }

    public boolean isIsManager() {
        return isManager.get();
    }

    public BooleanProperty isManagerProperty() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager.set(isManager);
    }

    public Date getHireDate() {
        return hireDate.get();
    }

    public ObjectProperty<Date> hireDateProperty() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate.set(hireDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(getId(), employee.getId()) && Objects.equals(getFirstName(), employee.getFirstName()) && Objects.equals(getLastName(), employee.getLastName()) && Objects.equals(getSalary(), employee.getSalary()) && Objects.equals(getPhone(), employee.getPhone()) && Objects.equals(getSkills(), employee.getSkills()) && Objects.equals(getJobTitle(), employee.getJobTitle()) && Objects.equals(isIsManager(), employee.isIsManager()) && Objects.equals(getHireDate(), employee.getHireDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getSalary(), getPhone(), getSkills(), getJobTitle(), isIsManager(), getHireDate());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", salary=" + salary +
                ", phone=" + phone +
                ", skills=" + skills +
                ", jobTitle=" + jobTitle +
                ", isManager=" + isManager +
                ", hireDate=" + hireDate +
                '}';
    }
}
