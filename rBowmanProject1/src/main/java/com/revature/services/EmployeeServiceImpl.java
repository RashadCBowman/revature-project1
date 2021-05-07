package com.revature.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.Employee;
import com.revature.beans.Employee.Role;
import com.revature.controllers.EmployeeController;
import com.revature.data.EmployeeDao;
import com.revature.data.cass.EmployeeDaoCass;

import com.revature.beans.TuitionReimbursementForm;
import com.revature.data.TuitionReimbursementFormDao;
import com.revature.data.cass.TuitionReimbursementFormDaoCass;

public class EmployeeServiceImpl implements EmployeeService {
	private EmployeeDao ed = new EmployeeDaoCass();
	private TuitionReimbursementFormDao td = new TuitionReimbursementFormDaoCass();
	private static final Logger log = LogManager.getLogger(EmployeeServiceImpl.class);

	// If the BenCo changes the reimbursement amount,
	// the Employee should be notified and given the option to cancel the request.
	@Override
	public boolean cancelRequest(Employee e) {
		if (e.getRole().equals(Employee.Role.EMPLOYEE)) {
			TuitionReimbursementForm t = td.getTuitionReimbursementForm();
			if (t != null && t.isAmountChanged()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean approveTuitionReimbursement(Employee a, int id) {
		log.trace("ApproveForm");
		Employee e = ed.getEmployeeById(id);
		log.trace(e);
		boolean updated = false;
		if (!a.getRole().equals(Employee.Role.EMPLOYEE) && e != null) {
			TuitionReimbursementForm t = ed.getEmployeeForm(e);
			log.trace(t);
			if (t != null) {
				// The direct supervisor must provide approval for Tuition Reimbursement.
				log.trace("Check DirectSupervisor");
				if (!t.isDirectSupervisorApproved() && a.getRole().equals(Employee.Role.DIRECTSUPERVISOR)) {
					log.trace("Approving DirectSupervisor");
					t.setDirectSupervisorApproved(true);
					if (a.isDepartmentHead()) {
						log.trace("Approving DepartmentHead");
						t.setDepartmentHeadApproved(true);
					}
					updated = true;
					// The department head must provide approval for Tuition Reimbursement.
				}
				log.trace("Check DepartmentHead");
				if (!t.isDepartmentHeadApproved() && t.isDirectSupervisorApproved()
						&& (a.getRole().equals(Employee.Role.DEPARTMENTHEAD))) {
					log.trace("Approving DepartmentHead");
					t.setDepartmentHeadApproved(true);
					updated = true;
				}
				log.trace("Check BenefitsCorrdinator");
				if (!t.isBenefitsCorrdinatorApproved() && t.isDirectSupervisorApproved() && t.isDepartmentHeadApproved()
						&& a.getRole().equals(Employee.Role.BENEFITSCORRDINATOR)) {
					log.trace("Approving BenefitsCorrdinator");
					t.setBenefitsCorrdinatorApproved(true);
					updated = true;
				}
				if (updated) {
					log.trace("Adding ID to Form for information to others!");
					t.addOtherId(a);
					log.trace("Updating Employee information");
					ed.updateEmployee(e);
					log.trace("Updating Admin information");
					ed.updateEmployee(a);
					log.trace("Updating Form information");
					td.updateTuitionReimbursementForm(t);
					log.trace("TRUE");
					return true;
				}
			}
		}
		log.trace("Nothing was changed FALSE");
		return false;
	}

	// If denied, the Direct Supervisor must provide a reason.
	@Override
	public boolean denyTuitionReimbursement(Employee a, int id, String reason) {
		Employee e = ed.getEmployeeById(id);
		log.trace(e);
		if (e != null) {
			if (e.getRole().equals(Employee.Role.EMPLOYEE)) {
				TuitionReimbursementForm t = ed.getEmployeeForm(e);
				log.trace(t);
				if (t != null) {

					if (!t.isDirectSupervisorApproved() && (a.getRole().equals(Employee.Role.DIRECTSUPERVISOR))) {
						e.addMessage(reason);
						log.trace("Reason sent from DS");
					}
					if (!t.isDepartmentHeadApproved()
							&& (a.getRole().equals(Employee.Role.DEPARTMENTHEAD) || a.isDepartmentHead())) {
						log.trace("Reason sent from DH");
						e.addMessage(reason);
					}
					if (!t.isBenefitsCorrdinatorApproved() && a.getRole().equals(Employee.Role.BENEFITSCORRDINATOR)) {
						log.trace("Reason sent from BC");
						e.addMessage(reason);
					}

					ed.updateEmployee(e);
					td.updateTuitionReimbursementForm(t);
					return true;
				}
			}
		}
		return false;
	}

	// The Direct Supervisor can request additional information from the employee
	// before approval.
	// The Department Head can request additional information from the employee or
	// direct supervisor before approval.
	// The BenCo can request additional information from the employee, direct
	// supervisor, or department head before approval.
	public boolean requestAdditionalInformation(Employee a, int id) {
		Employee e = ed.getEmployeeById(id);
		log.trace(e);
		TuitionReimbursementForm t = ed.getEmployeeForm(e);
		log.trace(t);
		List<Employee> arr = td.getEmployeesRelatedToForm(t);
		if (arr.size() > 0) {
			if (a.getRole().equals(Role.DIRECTSUPERVISOR)) {
				log.trace("Role is Direct Supervisor!");
				arr.forEach(employ -> {
					if (employ.getRole().equals(Role.EMPLOYEE)) {
						log.trace(employ.getRole() + " being contacted by DirectSupervisor!");
						employ.addMessage("Direct Supervisor " + a.getName()
								+ " requires additional information about your Tuition Reimbursement Form!");
						ed.updateEmployee(employ);
					}
				});

			} else if (a.getRole().equals(Role.DEPARTMENTHEAD)) {
				log.trace("Role is Department Head!");
				arr.forEach(employ -> {
					if (employ.getRole().equals(Role.EMPLOYEE) || employ.getRole().equals(Role.DIRECTSUPERVISOR)) {
						log.trace(employ.getRole() + " being contacted by DepartmentHead!");
						employ.addMessage("Department Head " + a.getName()
								+ " requires additional information about your Tuition Reimbursement Form!");
						ed.updateEmployee(employ);
					}
				});
			} else if (a.getRole().equals(Role.BENEFITSCORRDINATOR)) {
				log.trace("Role is Benefits Corrdinator!");
				arr.forEach(employ -> {
					if (!employ.getRole().equals(Role.BENEFITSCORRDINATOR)) {
						log.trace(employ.getRole() + " being contacted by BenefitsCorrdinator!");
						employ.addMessage("Benefits Corrdinator " + a.getName()
								+ " requires additional information about your Tuition Reimbursement Form!");
						ed.updateEmployee(employ);
					}
				});
			} else {
				log.trace("No Role confusion?!");
				return false;
			}
		} else {
			log.trace("No one related to Form");
			return false;
		}
		log.trace("Message sent!");
		return true;
	}

	public boolean changeReimbursementAmount(Employee a, int id, double amount) {
		Employee e = ed.getEmployeeById(id);
		log.trace(e);
		if (e != null) {
			TuitionReimbursementForm t = ed.getEmployeeForm(e);
			log.trace(t);
			if (t != null) {
				double previous = t.getAvailableReimbursement();
				log.trace("Previous amount $" + previous);
				t.setAvailableReimbursement(amount);
				log.trace("Amount is going to change to " + amount);
				t.setAmountChanged(true);
				e.addMessage("Benefits Corrdinator " + a.getName() + " has changed your available reimbursement from $"
						+ previous + " to $" + t.getAvailableReimbursement() + "!");
				log.trace("Complete change!");
				return true;
			}
		}
		log.trace("Null");
		return false;
	}

	@Override
	public boolean award(Employee a, int id) {
		Employee e = ed.getEmployeeById(id);
		if (e != null) {
			TuitionReimbursementForm trf = ed.getEmployeeForm(e);
			if (trf != null) {
				if (trf.isBenefitsCorrdinatorApproved() && trf.isDepartmentHeadApproved()
						&& trf.isDirectSupervisorApproved()) {
					trf.setFormComplete(true);
					td.updateTuitionReimbursementForm(trf);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String viewGrade(Employee e) {
		return ed.getEmployeeForm(e).getGradeFile();
	}

	// Only interested parties should be able to access the grades/presentations.
	// Interested parties include the requestor and approvers.
	@Override
	public String viewGrade(Employee a, int EmployeeId) {
		Employee e = ed.getEmployeeById(EmployeeId);
		if (e != null) {
			TuitionReimbursementForm t = ed.getEmployeeForm(e);
			if (t != null) {
				for (int arr : e.getApprovedForm()) {
					if (t.getEmployeeId() == arr) {
						return t.getGradeFile();
					}
				}
			}
		}
		return null;
	}

	@Override
	public String viewPresentation(Employee e) {
		return ed.getEmployeeForm(e).getPresentationFile();
	}

	@Override
	public String viewPresentation(Employee a, int EmployeeId) {
		Employee e = ed.getEmployeeById(EmployeeId);
		if (e != null) {
			TuitionReimbursementForm t = ed.getEmployeeForm(e);
			if (t != null) {
				for (int arr : e.getApprovedForm()) {
					if (t.getEmployeeId() == arr) {
						return t.getPresentationFile();
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isGradePassable(Employee a, Employee e, boolean pass) {
		TuitionReimbursementForm t = ed.getEmployeeForm(e);
		return false;
	}

	@Override
	public boolean isPresentationGood(Employee a, Employee e, boolean pass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Employee> getEmployees() {
		
		return ed.getEmployees();
	}

	@Override
	public Employee getEmployeeById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addEmployee(Employee e) {
		ed.addEmployee(e);
		if (e == null) {
			return false;
		}
		return true;
	}

	@Override
	public Employee getEmployeeByName(String name) {
		ed.getEmployeeByName(name);
		return null;
	}
	
	public Employee getEmployeeByNameAndPass(String name, String pass) {
		return ed.getEmployeeByNameAndPass(name, pass);
	}

}
