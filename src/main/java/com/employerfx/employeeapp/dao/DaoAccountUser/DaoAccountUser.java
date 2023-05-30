package com.employerfx.employeeapp.dao.DaoAccountUser;

import com.employerfx.employeeapp.entities.AccountUser;

public interface DaoAccountUser {
    AccountUser getAccountUser(String name, String password);
}
