package com.revature.data;

import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.TuitionReimbursementForm;
import com.revature.beans.Employee.Role;

public interface EmployeeDao {
	
	//STANDARD INFORMATION
	List<Employee> getEmployees(); //gets all employees
	Employee getEmployeeById(int id);
	Employee getEmployeeByName(String name);
	Employee getEmployeeByNameAndPass(String name, String pass);
	TuitionReimbursementForm getEmployeeForm(Employee e);
	void updateEmployee(Employee e);
	List<Employee> getEmployeesByRole(Role role);
	void addEmployee(Employee e);
}
