package com.casecode.data.mapper

import com.casecode.domain.model.users.Employee
import com.casecode.domain.utils.EMPLOYEES_FIELD
import com.casecode.domain.utils.EMPLOYEE_BRANCH_NAME_FIELD
import com.casecode.domain.utils.EMPLOYEE_NAME_FIELD
import com.casecode.domain.utils.EMPLOYEE_PASSWORD_FIELD
import com.casecode.domain.utils.EMPLOYEE_PERMISSION_FIELD
import com.casecode.domain.utils.EMPLOYEE_PHONE_NUMBER_FIELD

/**
 * Created by Mahmoud Abdalhafeez
 */
fun List<Employee>.toEmployeesRequest(): HashMap<String, MutableList<Map<String, Any?>>> {
    val employeesRequest = mutableListOf<Map<String, Any?>>()
    this.forEach {
        val employeeData = hashMapOf(
            EMPLOYEE_NAME_FIELD to it.name,
            EMPLOYEE_PHONE_NUMBER_FIELD to it.phoneNumber,
            EMPLOYEE_PASSWORD_FIELD to it.password,
            EMPLOYEE_BRANCH_NAME_FIELD to it.branchName,
            EMPLOYEE_PERMISSION_FIELD to it.permission
        )
        employeesRequest.add(employeeData)
    }
    return hashMapOf(EMPLOYEES_FIELD to employeesRequest)
}