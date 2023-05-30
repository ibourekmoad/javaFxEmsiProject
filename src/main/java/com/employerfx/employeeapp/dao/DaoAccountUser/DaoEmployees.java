package com.employerfx.employeeapp.dao.DaoAccountUser;



import com.employerfx.employeeapp.entities.Employee;

import java.util.List;

public interface DaoEmployees {
    void saveEmployee(Employee employee);
    void saveEmployeeNoId(Employee employee);

    void updateEmployee(Employee employee);
    void deleteEmployee(long id);
    Employee getEmployeeById(long id);
    List<Employee> getAllEmployees();
}