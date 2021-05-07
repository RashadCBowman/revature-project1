package com.revature.services;

import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.TuitionReimbursementForm;

public interface EmployeeService {
	
	
	//STANDARD INFORMATION
	List<Employee> getEmployees(); //gets all employees
	Employee getEmployeeById(int id);
	Employee getEmployeeByName(String name);
	Employee getEmployeeByNameAndPass(String name, String pass);
	
	
	//ALL SECTION
	//Only interested parties should be able to access the grades/presentations.
	//Interested parties include the requester and approvers
	String viewGrade(Employee e);
	String viewGrade(Employee a, int Employeeid);
	String viewPresentation(Employee e);
	String viewPresentation(Employee a, int EmployeeId);
	
	
	//EMPLOYEE SECTION
	
	//If the BenCo changes the reimbursement amount, the Employee should be notified and given the option to cancel the request.
	boolean cancelRequest(Employee e);

	// DIRECT SUPERVISOR, 
	
	//The direct supervisor must provide approval for Tuition Reimbursement.
	boolean approveTuitionReimbursement(Employee a, int id);
	
	//If denied, the Direct Supervisor must provide a reason.  
	boolean denyTuitionReimbursement(Employee a, int id, String reason);
	
	//The Direct Supervisor can request additional information from the employee before approval. 
	boolean requestAdditionalInformation(Employee a, int id);
	
	//If the BenCo changes the reimbursement amount, the Employee should be notified and given the option to cancel the request.  
	boolean changeReimbursementAmount(Employee a, int id, double amount) ;
	//The BenCo is allowed to award an amount larger than the amount available for the employee.  
	boolean award(Employee a, int id);
	
	
	//BENCO ONLY
	//After upload of a grade, the BenCo must confirm that the grade is passing
	boolean isGradePassable(Employee a, Employee e, boolean pass);
	

	//DIRECT MANAGER ONLY
	//After upload of a presentation, the direct manager must confirm that the presentation was satisfactory
	//and presented to the appropriate parties
	boolean isPresentationGood(Employee a, Employee e, boolean pass);
	boolean addEmployee(Employee e);
	
	
}
