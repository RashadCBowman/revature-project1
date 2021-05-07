package com.revature.services;

import java.util.List;

import com.revature.beans.Employee;
import com.revature.beans.TuitionReimbursementForm;

public interface TuitionReimbursementFormService {
	
	//STANDARD INFORMATION
	double endReimburse(double currency);
	//The BenCo has the ability to alter the reimbursement amount.
	void changeReimbursementAmount(double change);
	TuitionReimbursementForm addForm(Employee e, TuitionReimbursementForm t);
	void autoApproved();
}
