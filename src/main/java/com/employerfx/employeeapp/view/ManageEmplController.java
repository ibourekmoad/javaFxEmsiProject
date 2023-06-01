package com.employerfx.employeeapp.view;

import com.employerfx.employeeapp.dao.DaoAccountUser.impl.MariaDBDaoEmployees;
import com.employerfx.employeeapp.entities.Employee;
import com.employerfx.employeeapp.service.EmployeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;


import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManageEmplController implements Initializable {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> idCol;
    @FXML
    private TableColumn<Employee, String> firstNameCol;
    @FXML
    private TableColumn<Employee, String> lastNameCol;
    @FXML
    private TableColumn<Employee, String> salaryCol;
    @FXML
    private TableColumn<Employee, String> phoneCol;
    @FXML
    private TableColumn<Employee, String> skillsCol;
    @FXML
    private TableColumn<Employee, String> titleCol;
    @FXML
    private TableColumn<Employee, String> managerCol;
    @FXML
    private TableColumn<Employee, String> hireCol;

    @FXML
    private TextField empFirstName;
    @FXML
    private TextField empLastName;
    @FXML
    private TextField empSalary;
    @FXML
    private TextField empPhone;
    @FXML
    private TextField empSkills;
    @FXML
    private TextField empJobTitle;
    @FXML
    private TextField empManager;
    @FXML
    private TextField empHireDate;
    @FXML
    private TableColumn deleteCol;
    @FXML
    private TableColumn editCol;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadData();
        deleteCol.setCellFactory(column -> {
            TableCell<Employee, Boolean> cell = new TableCell<Employee, Boolean>() {
                final Button deleteButton = new Button("Delete");

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                        deleteButton.setOnAction(event -> {
                            Employee employee = getTableRow().getItem();
                            // Call the deleteEmployee method with the employee object
                            deleteEmployee(employee);
                        });
                    }
                }
            };

            return cell;
        });
        editCol.setCellFactory(col -> {
            TableCell<Employee, Void> cell = new TableCell<>() {
                private final Button editButton = new Button("Edit");

                {

                    editButton.setOnAction(event -> {
                        Employee employee = getTableView().getItems().get(getIndex());
                        System.out.println("set on Action");
                        System.out.println(employee);

                        openEditEmployeeDialog(employee);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };
            return cell;
        });
    }
    private void openEditEmployeeDialog(Employee employee) {
        // Create a new stage for the edit dialog or form
        Stage editStage = new Stage();
        editStage.setTitle("Edit Employee");

        // Load the FXML file for the edit dialog or form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Edit-View.fxml"));
        Parent editRoot;
        try {
            editRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get the controller instance from the loader
        EditEmployeeController editController = loader.getController();
        System.out.println("openEditEmployeeDialog:called");
        System.out.println(employee);

        // Pass the selected employee to the edit controller
        editController.setEmployee(employee); // Set the employee object

        // Set the scene for the edit stage
        Scene editScene = new Scene(editRoot);
        editStage.setScene(editScene);
        editStage.show();
    }


    private void deleteEmployee(Employee employee) {


        EmployeService employeeService = new EmployeService();
        employeeService.deleteEmployee(employee.getId());
        loadData();
    }


    private void loadData() {
        MariaDBDaoEmployees dao = new MariaDBDaoEmployees();
        List<Employee> employeeList = dao.getAllEmployees();

        Set<Long> uniqueIds = new HashSet<>();
        List<Employee> filteredList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (!uniqueIds.contains(employee.getId())) {
                filteredList.add(employee);
                uniqueIds.add(employee.getId());
            }
        }

        System.out.println(filteredList);
        ObservableList<Employee> observableEmployeeList = FXCollections.observableArrayList(filteredList);
        employeeTable.setItems(observableEmployeeList);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        skillsCol.setCellValueFactory(param -> {
            Employee employee = param.getValue();
            List<String> skills = employee.getSkills();
            String skillsString = String.join(", ", skills);
            return new SimpleStringProperty(skillsString);
        });
        titleCol.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        managerCol.setCellValueFactory(new PropertyValueFactory<>("isManager"));
        hireCol.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
    }

    public void addFromtxtFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        EmployeService newService = new EmployeService();

        if (selectedFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] employeeData = line.split(",");

                    // Extract the individual data fields from the line
                    long id = Long.parseLong(employeeData[0]);
                    String firstName = employeeData[1];
                    String lastName = employeeData[2];
                    double salary = Double.parseDouble(employeeData[3]);
                    String phone = employeeData[4];
                    List<String> skills = Arrays.asList(employeeData[5].split(";"));
                    String jobTitle = employeeData[6];
                    boolean isManager = Boolean.parseBoolean(employeeData[7]);
                    Date hireDate = new SimpleDateFormat("yyyy-MM-dd").parse(employeeData[8]);

                    // Check if the employee already exists
                    Employee existingEmployee = newService.getEmployeeById(id);
                    if (existingEmployee != null) {
                        System.out.println("Employee with ID " + id + " already exists.");
                        continue;  // Skip adding this employee and move to the next one
                    }

                    // Create an Employee object and set its properties
                    Employee newEmployee = new Employee();
                    newEmployee.setId(id);
                    newEmployee.setFirstName(firstName);
                    newEmployee.setLastName(lastName);
                    newEmployee.setSalary(salary);
                    newEmployee.setPhone(phone);
                    newEmployee.setSkills(skills);
                    newEmployee.setJobTitle(jobTitle);
                    newEmployee.setIsManager(isManager);
                    newEmployee.setHireDate(hireDate);

                    // Add the employee to the system
                    newService.addEmployee(newEmployee);
                }

                loadData();
                System.out.println("Employees added from the file.");
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void addFromExcelFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(null);
        EmployeService newService = new EmployeService();

        if (selectedFile != null) {
            try (Workbook workbook = WorkbookFactory.create(selectedFile)) {
                Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

                for (Row row : sheet) {
                    // Skip the header row
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    // Extract the individual data fields from the row
                    long id = (long) row.getCell(0).getNumericCellValue();
                    String firstName = row.getCell(1).getStringCellValue();
                    String lastName = row.getCell(2).getStringCellValue();
                    double salary = row.getCell(3).getNumericCellValue();
                    String phone = row.getCell(4).getStringCellValue();
                    List<String> skills = Arrays.asList(row.getCell(5).getStringCellValue().split(";"));
                    String jobTitle = row.getCell(6).getStringCellValue();
                    boolean isManager = row.getCell(7).getBooleanCellValue();
                    Date hireDate = row.getCell(8).getDateCellValue();

                    // Create an Employee object and set its properties
                    Employee newEmployee = new Employee();
                    newEmployee.setId(id);
                    newEmployee.setFirstName(firstName);
                    newEmployee.setLastName(lastName);
                    newEmployee.setSalary(salary);
                    newEmployee.setPhone(phone);
                    newEmployee.setSkills(skills);
                    newEmployee.setJobTitle(jobTitle);
                    newEmployee.setIsManager(isManager);
                    newEmployee.setHireDate(hireDate);

                    // Add the employee to the system
                    newService.addEmployee(newEmployee);
                }

                // Data successfully added from the file
                System.out.println("Employees added from the Excel file.");
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any exceptions that occur during file reading or parsing
            }
        }
    }
    public void addEmployeeController() {
        System.out.println("First Name: " + empFirstName.getText());
        System.out.println("Last Name: " + empLastName.getText());
        System.out.println("Salary: " + empSalary.getText());
        System.out.println("Phone: " + empPhone.getText());
        System.out.println("Skills: " + empSkills.getText());
        System.out.println("Job Title: " + empJobTitle.getText());
        System.out.println("Manager: " + empManager.getText());
        System.out.println("Hire Date: " + empHireDate.getText());

        Employee enteredEmployee = new Employee();
        EmployeService newService = new EmployeService();

        Date hireDate;


        enteredEmployee.setFirstName(empFirstName.getText());
        enteredEmployee.setLastName(empLastName.getText());
        enteredEmployee.setSalary(Double.parseDouble(empSalary.getText()));
        enteredEmployee.setPhone(empPhone.getText());
        enteredEmployee.setSkills(Arrays.asList(empSkills.getText().split(";")));
        enteredEmployee.setJobTitle(empJobTitle.getText());
        enteredEmployee.setIsManager(Boolean.parseBoolean(empManager.getText()));
        if (empHireDate.getText().equals("null")) {
            hireDate = null;
        } else {
            try {
                hireDate = new SimpleDateFormat("yyyy-MM-dd").parse(empHireDate.getText());
            } catch (ParseException e) {
                hireDate = null;
            }
        }
        enteredEmployee.setHireDate(hireDate);

        newService.addEmployee(enteredEmployee);

        empFirstName.clear();
        empLastName.clear();
        empSalary.clear();
        empPhone.clear();
        empSkills.clear();
        empJobTitle.clear();
        empManager.clear();
        empHireDate.clear();

        loadData();
    }
    public void exportToTextFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                // Write the header row
                writer.write("ID,First Name,Last Name,Salary,Phone,Skills,Job Title,Manager,Hire Date");
                writer.newLine();

                // Write the employee data
                for (Employee employee : employeeTable.getItems()) {
                    StringBuilder employeeData = new StringBuilder();
                    employeeData.append(employee.getId()).append(",");
                    employeeData.append(employee.getFirstName()).append(",");
                    employeeData.append(employee.getLastName()).append(",");
                    employeeData.append(employee.getSalary()).append(",");
                    employeeData.append(employee.getPhone()).append(",");
                    employeeData.append(String.join(";", employee.getSkills())).append(",");
                    employeeData.append(employee.getJobTitle()).append(",");
                    employeeData.append(employee.isIsManager()).append(",");
                    employeeData.append(employee.getHireDate());

                    writer.write(employeeData.toString());
                    writer.newLine();
                }

                System.out.println("Employee data exported to the text file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
