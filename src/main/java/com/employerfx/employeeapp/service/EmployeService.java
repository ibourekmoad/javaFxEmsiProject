package com.employerfx.employeeapp.service;

import com.employerfx.employeeapp.dao.DaoAccountUser.impl.MariaDBDaoEmployees;
import com.employerfx.employeeapp.entities.Employee;

public class EmployeService {



    public void addEmployee(Employee newEmployee){
        MariaDBDaoEmployees dao = new MariaDBDaoEmployees();

        dao.saveEmployeeNoId(newEmployee);

    }
    public void deleteEmployee(long id) {
        MariaDBDaoEmployees dao = new MariaDBDaoEmployees();
        dao.deleteEmployee(dao.getEmployeeById(id).getId());
    }
    public void updateEmployee(Employee employee) {
        MariaDBDaoEmployees dao = new MariaDBDaoEmployees();
        dao.updateEmployee(employee);
    }

    public Employee getEmployeeById(long id) {
        MariaDBDaoEmployees dao = new MariaDBDaoEmployees();
        return dao.getEmployeeById(id);
    }

}
