package com.employerfx.employeeapp.view;

import com.employerfx.employeeapp.entities.Employee;
import com.employerfx.employeeapp.service.EmployeService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class EditEmployeeController implements Initializable {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField skillsField;
    @FXML
    private TextField jobTitleField;
    @FXML
    private TextField managerField;
    @FXML
    private TextField hireDateField;
    @FXML
    private Button saveButton;

    private Employee employee;

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (employee != null) {
            firstNameField.setText(employee.getFirstName());
            lastNameField.setText(employee.getLastName());
            salaryField.setText(String.valueOf(employee.getSalary()));
            phoneField.setText(employee.getPhone());
            skillsField.setText(String.join(", ", employee.getSkills()));
            jobTitleField.setText(employee.getJobTitle());
            managerField.setText(String.valueOf(employee.isIsManager()));
            hireDateField.setText(employee.getHireDate().toString());
        }
    }
    @FXML
    private void saveEmployee() {
        // Retrieve the edited values from the fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        double salary = Double.parseDouble(salaryField.getText());
        String phone = phoneField.getText();
        String[] skills = skillsField.getText().split(", ");
        String jobTitle = jobTitleField.getText();
        boolean isManager = Boolean.parseBoolean(managerField.getText());
        // Parse the hire date from the field and handle any necessary date formatting
        Date hireDate = parseDate(hireDateField.getText());

        // Update the employee object with the edited values
        if (employee != null) {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setSalary(salary);
            employee.setPhone(phone);
            employee.setSkills(Arrays.asList(skills));
            employee.setJobTitle(jobTitle);
            employee.setIsManager(isManager);
            employee.setHireDate(hireDate);

            // Save the updated employee to your data source
            EmployeService employeeService = new EmployeService();
            employeeService.updateEmployee(employee.getId());

            Stage currentStage = (Stage) saveButton.getScene().getWindow();
            currentStage.close();        }
    }

    private Date parseDate(String dateString) {
        // Implement the logic to parse the hire date from the field and handle any necessary date formatting
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }





}
