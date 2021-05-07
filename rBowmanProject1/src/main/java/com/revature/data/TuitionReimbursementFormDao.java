package com.revature.data;


import java.util.List;

import com.revature.beans.*;

public interface TuitionReimbursementFormDao {
	List<TuitionReimbursementForm> getTuitionReimbursementForms();
	TuitionReimbursementForm getTuitionReimbursementForm();
	void updateTuitionReimbursementForm(TuitionReimbursementForm t);
	List<Employee> getEmployeesRelatedToForm(TuitionReimbursementForm t);
	TuitionReimbursementForm addForm(Employee e, TuitionReimbursementForm t);
}
