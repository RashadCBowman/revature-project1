package com.revature.services;

import java.util.Date;
import java.util.List;


import com.revature.beans.Employee;
import com.revature.beans.Employee.Role;
import com.revature.beans.TuitionReimbursementForm;
import com.revature.data.EmployeeDao;
import com.revature.data.cass.EmployeeDaoCass;
import com.revature.data.TuitionReimbursementFormDao;
import com.revature.data.cass.TuitionReimbursementFormDaoCass;

public class TuitionReimbursementFormServiceImpl implements TuitionReimbursementFormService{
	private EmployeeDao ed = new EmployeeDaoCass();
	private TuitionReimbursementFormDao td = new TuitionReimbursementFormDaoCass();

	@Override
	public double endReimburse(double currency) {
		TuitionReimbursementForm t = td.getTuitionReimbursementForm();
		
		return t.getAvailableReimbursement()*t.getPercent();
	}

	//The BenCo has the ability to alter the reimbursement amount.
	public void changeReimbursementAmount(double change) {
		TuitionReimbursementForm t = td.getTuitionReimbursementForm();
		t.setAvailableReimbursement(change);
		t.setAmountChanged(true);
		td.updateTuitionReimbursementForm(t);
	}

	
	public TuitionReimbursementForm addForm(Employee e, TuitionReimbursementForm t) {
		return td.addForm(e,t);
	}
	
	
	//If the direct supervisor does not complete this task in a timely matter,
	//the request is auto-approved. 
	//If the Department Head does not complete this task in a timely matter,
	//the request is auto-approved.
	//If the BenCo does not approval in a timely matter,
	//an escalation email should be sent to the BenCo’s direct supervisor.  
	public void autoApproved() {
		List<TuitionReimbursementForm> forms = td.getTuitionReimbursementForms();
		forms.forEach((trf) -> {
			if (trf.isApprovalTimeLimit()) {
				Date dateNow = new Date();
				if (dateNow.after(trf.getDateDue()) && !trf.isDirectSupervisorApproved()) {
					trf.setDirectSupervisorApproved(true);
				} else if (dateNow.after(trf.getDateDue()) && !trf.isDepartmentHeadApproved()) {
					trf.setDepartmentHeadApproved(true);
				} else if (dateNow.after(trf.getDateDue()) && !trf.isBenefitsCorrdinatorApproved()) {
					List<Employee> ds = ed.getEmployeesByRole(Role.DIRECTSUPERVISOR);
					ds.forEach(e ->{
						e.addMessage("An email has been sent to the you regarding BenCo not approving the tuition Reimbursement Form of ID #" + trf.getEmployeeId()+ "!");
						ed.updateEmployee(e);
					});	
				}
				td.updateTuitionReimbursementForm(trf);
			}
		});
	}
}
