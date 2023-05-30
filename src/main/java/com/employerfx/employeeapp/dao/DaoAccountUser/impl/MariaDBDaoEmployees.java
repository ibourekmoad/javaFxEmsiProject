package com.employerfx.employeeapp.dao.DaoAccountUser.impl;

import com.employerfx.employeeapp.dao.DaoAccountUser.DaoEmployees;
import com.employerfx.employeeapp.entities.Employee;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MariaDBDaoEmployees implements DaoEmployees {
    //    public static void main(String[] args) {
//        Date my = new Date(984863);
//        List<String> myskills = new ArrayList<>(Arrays.asList("qfaqf","sqgegvq"));
//        Employee moad = new Employee(51546,"wvswdv","seffq",
//                454.13,"wfsevv", myskills,"zrgzff",true,my);
//        saveEmployee(moad);
//    }
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/javaAV";
    private static final String USERNAME = "ubuntu";
    private static final String PASSWORD = "password";


    @Override
    public void saveEmployee(Employee employee) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            // Check if an employee with the same values exists
            String checkSql = "SELECT COUNT(*) FROM employees WHERE firstName = ? AND lastName = ? " +
                    "AND salary = ? AND phone = ? AND jobTitle = ? AND isManager = ? AND hireDate = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkSql);
            checkStatement.setString(1, employee.getFirstName());
            checkStatement.setString(2, employee.getLastName());
            checkStatement.setDouble(3, employee.getSalary());
            checkStatement.setString(4, employee.getPhone());
            checkStatement.setString(5, employee.getJobTitle());
            checkStatement.setBoolean(6, employee.isIsManager());
            checkStatement.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));

            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            // If no matching employee found, insert the new employee
            if (count == 0) {
                String insertSql = "INSERT INTO employees (firstName, lastName, salary, phone, jobTitle, isManager, hireDate) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertSql);
                insertStatement.setString(1, employee.getFirstName());
                insertStatement.setString(2, employee.getLastName());
                insertStatement.setDouble(3, employee.getSalary());
                insertStatement.setString(4, employee.getPhone());
                insertStatement.setString(5, employee.getJobTitle());
                insertStatement.setBoolean(6, employee.isIsManager());
                insertStatement.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));

                insertStatement.executeUpdate();
                insertStatement.close();
            } else {
                System.out.println("Employee already exists in the database.");
            }

            checkStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveEmployeeNoId(Employee employee) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            // Insert into the employees table
            String sql = "INSERT INTO employees (firstName, lastName, salary, phone, jobTitle, isManager, hireDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setDouble(3, employee.getSalary());
            statement.setString(4, employee.getPhone());
            statement.setString(5, employee.getJobTitle());
            statement.setBoolean(6, employee.isIsManager());
            statement.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));
            statement.executeUpdate();

            // Get the generated employeeId
            ResultSet generatedKeys = statement.getGeneratedKeys();
            long employeeId;
            if (generatedKeys.next()) {
                employeeId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Failed to retrieve the generated employeeId.");
            }

            // Insert into the employee_skills table
            if (employee.getSkills() != null && !employee.getSkills().isEmpty()) {
                sql = "INSERT INTO skills (employeeId, skill) VALUES (?, ?)";
                PreparedStatement skillStatement = conn.prepareStatement(sql);
                for (String skill : employee.getSkills()) {
                    skillStatement.setLong(1, employeeId);
                    skillStatement.setString(2, skill);
                    skillStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE employees SET firstName=?, lastName=?, salary=?, phone=?, jobTitle=?, isManager=?, " +
                    "hireDate=? WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setDouble(3, employee.getSalary());
            statement.setString(4, employee.getPhone());
            statement.setString(5, employee.getJobTitle());
            statement.setBoolean(6, employee.isIsManager());
            statement.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));
            statement.setLong(8, employee.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(long id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String deleteSkillsSql = "DELETE FROM skills WHERE employeeId=?";
            PreparedStatement deleteSkillsStatement = conn.prepareStatement(deleteSkillsSql);
            deleteSkillsStatement.setLong(1, id);
            deleteSkillsStatement.executeUpdate();

            String deleteEmployeeSql = "DELETE FROM employees WHERE id=?";
            PreparedStatement deleteEmployeeStatement = conn.prepareStatement(deleteEmployeeSql);
            deleteEmployeeStatement.setLong(1, id);
            deleteEmployeeStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Employee getEmployeeById(long id) {
        Employee employee = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM employees WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                employee = createEmployeeFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
             String sql = "SELECT e.*, s.skill FROM employees e " +
                    "JOIN skills s ON e.id = s.employeeId";

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Employee employee = createEmployeeFromResultSet(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private Employee createEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        double salary = resultSet.getDouble("salary");
        String phone = resultSet.getString("phone");
        String jobTitle = resultSet.getString("jobTitle");
        boolean isManager = resultSet.getBoolean("isManager");
        Date hireDate = resultSet.getDate("hireDate");

        List<String> skills = getSkillsForEmployee(id); // Retrieve skills from the skills table based on the employee ID

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setSalary(salary);
        employee.setPhone(phone);
        employee.setSkills(skills);
        employee.setJobTitle(jobTitle);
        employee.setIsManager(isManager);
        employee.setHireDate(hireDate);

        return employee;
    }

    private List<String> getSkillsForEmployee(long employeeId) {
        List<String> skills = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT skill FROM skills WHERE employeeId = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, employeeId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String skill = resultSet.getString("skill");
                skills.add(skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }


//    private Employee createEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
//        long id = resultSet.getLong("id");
//        String firstName = resultSet.getString("firstName");
//        String lastName = resultSet.getString("lastName");
//        double salary = resultSet.getDouble("salary");
//        String phone = resultSet.getString("phone");
//        // Retrieve the skills data from the ResultSet and convert it to a List<String>
//        String skillsString = resultSet.getString("skills");
//        List<String> skills = Arrays.asList(skillsString.split(",")); // Assuming skills are stored as comma-separated values
//        String jobTitle = resultSet.getString("jobTitle");
//        boolean isManager = resultSet.getBoolean("isManager");
//        Date hireDate = resultSet.getDate("hireDate");
//
//        Employee employee = new Employee();
//        employee.setId(id);
//        employee.setFirstName(firstName);
//        employee.setLastName(lastName);
//        employee.setSalary(salary);
//        employee.setPhone(phone);
//        employee.setSkills(skills);
//        employee.setJobTitle(jobTitle);
//        employee.setIsManager(isManager);
//        employee.setHireDate(hireDate);
//
//        return employee;
//    }



}