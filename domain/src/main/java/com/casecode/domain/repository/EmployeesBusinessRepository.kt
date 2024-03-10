package com.casecode.domain.repository

import com.casecode.domain.model.users.Employee
import com.casecode.domain.utils.Resource

typealias AddEmployees = Resource<Boolean>

interface EmployeesBusinessRepository {
    suspend fun getEmployees(uid: String): List<Employee>
    suspend fun setEmployees(employees: MutableList<Employee>, uid: String): AddEmployees
}
