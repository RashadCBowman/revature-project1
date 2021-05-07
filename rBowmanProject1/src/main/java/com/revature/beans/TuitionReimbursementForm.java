package com.revature.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.revature.beans.Employee.Role;

public class TuitionReimbursementForm {
	//private Employee employee;

	private int employeeId;
	private double availableReimbursement;
	// totalReimbursement($1000)
	private double totalReimbursement;
	private double pendingReimbursements;
	private double awardedReimbursements;
	//private Date dateDue;
	private Date dateReceived;
	private Date dateDue;
	private Process process;

	private boolean approvalTimeLimit;
	private boolean directSupervisorApproved;
	private boolean departmentHeadApproved;
	private boolean benefitsCorrdinatorApproved;
	private boolean formComplete;

	//If the BenCo changes the reimbursement amount,
	//the Employee should be notified and given the option to cancel the request.  
	private ArrayList<Integer> otherId;
	private boolean isAmountChanged;
	private String gradeFile;
	private String presentationFile;
	
	public String getGradeFile() {
		return gradeFile;
	}

	public void setGradeFile(String gradeFile) {
		this.gradeFile = gradeFile;
	}

	public String getPresentationFile() {
		return presentationFile;
	}

	public void setPresentationFile(String presentationFile) {
		this.presentationFile = presentationFile;
	}

	private boolean urgent;

	/*
	 * . Currently, TRMS provides reimbursements for university courses, seminars,
	 * certification preparation classes, certifications, and technical training
	 */

	public enum Process {
		UNIVERSITYCOURSES, SEMINARS, CERTIFICATIONPREPARATIONCLASSES, CERTIFICATION, TECHNICALTRAINING, OTHER
	}

	public TuitionReimbursementForm() {
		super();
		this.setEmployeeId(0);
		this.setTotalReimbursement(1000);
		this.setPendingReimbursements(0);
		this.setAwardedReimbursements(0);
		this.setAvailableReimbursement(calculateAvailableReimbursement());
		this.setProcess(Process.OTHER);
		this.setDateReceived(new Date());
		this.setDateDue(7);
		
		this.setApprovalTimeLimit(false);
		this.setDirectSupervisorApproved(false);
		this.setDepartmentHeadApproved(false);
		this.setBenefitsCorrdinatorApproved(false);
		this.setFormComplete(false);
		this.setAmountChanged(false);
		this.setUrgent(false);
		
		this.setGradeFile("");
		this.setPresentationFile("");
		this.otherId = new ArrayList<Integer>();
	}

	public TuitionReimbursementForm(double pending, double awarded) {
		this(1000, pending, awarded);
	}

	public TuitionReimbursementForm(double total, double pending, double awarded) {
		this();
		this.setTotalReimbursement(total);
		this.setPendingReimbursements(pending);
		this.setAwardedReimbursements(awarded);
		this.setAvailableReimbursement(calculateAvailableReimbursement());
	}

	public TuitionReimbursementForm(TuitionReimbursementForm trf) {
		this();
		this.setTotalReimbursement(trf.getTotalReimbursement());
		this.setPendingReimbursements(trf.getPendingReimbursements());
		this.setAwardedReimbursements(trf.getAwardedReimbursements());
		this.setAvailableReimbursement(calculateAvailableReimbursement());
	}

	public void setTotalReimbursement(double totalReimbursement) {
		this.totalReimbursement = totalReimbursement;
	}

	public double getAwardedReimbursements() {
		return this.awardedReimbursements;
	}

	public void setAwardedReimbursements(double awardedReimbursements) {
		this.awardedReimbursements = awardedReimbursements;
	}

	public double getTotalReimbursement() {
		return totalReimbursement;
	}

	public double getPendingReimbursements() {
		return pendingReimbursements;
	}

	/*
	 * Event types have different standard reimbursement coverage: University
	 * Courses 80%, Seminars 60%, Certification Preparation Classes 75%,
	 * Certification 100%, Technical Training 90%, Other 30%
	 */

	public double getPercent() {
		double percent = 0;

		switch (this.getProcess()) {
		case UNIVERSITYCOURSES:
			percent = 0.80;
			break;
		case SEMINARS:
			percent = 0.60;
			break;
		case CERTIFICATIONPREPARATIONCLASSES:
			percent = 0.75;
			break;
		case CERTIFICATION:
			percent = 1;
			break;
		case TECHNICALTRAINING:
			percent = 0.90;
			break;
		default:
			percent = 0.30;
			break;

		}

		return percent;
	}

	public double calculateAvailableReimbursement() {
		// The monetary amount available for an employee to reimburse is defined by
		// the following equation: AvailableReimbursement = TotalReimbursement ($1000) –
		// PendingReimbursements – AwardedReimbursements.
		double ar = this.totalReimbursement - this.pendingReimbursements - this.awardedReimbursements;
		if (ar < 0) {
			return 0;
			// Each employee is allowed to claim up to $1000 in tuition reimbursement a
			// year.
		} else if (ar > 1000.0) {
			return 1000;
		}
		return ar;
	}

	public void setAvailableReimbursementForFormula() {
		double ar = this.totalReimbursement - this.pendingReimbursements - this.awardedReimbursements;
		if (ar < 0) {
			ar = 0;
		}
		this.availableReimbursement = ar;
	}
	
	public double getAvailableReimbursement() {
		return this.availableReimbursement;
	}
	
	public void setAvailableReimbursement(double availableReimbursement) {
		this.availableReimbursement = availableReimbursement;
	}

	public void setPendingReimbursements(double pendingReimbursements) {
		if (pendingReimbursements < 0) {
			this.pendingReimbursements = 0;
		} else {
			double ar = getAvailableReimbursement();
			// If the projected reimbursement for an event exceeds the available
			// reimbursement amount,
			// it is adjusted to the amount available.
			if (ar < pendingReimbursements) {
				this.pendingReimbursements = ar;
			}
			this.pendingReimbursements = pendingReimbursements;
		}
	}

	public Process getProcess() {
		return this.process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public boolean isDirectSupervisorApproved() {
		return this.directSupervisorApproved;
	}

	public void setDirectSupervisorApproved(boolean directSupervisorApproved) {
		this.directSupervisorApproved = directSupervisorApproved;
	}

	public boolean isDepartmentHeadApproved() {
		return this.departmentHeadApproved;
	}

	public void setDepartmentHeadApproved(boolean departmentheadApproved) {
		this.departmentHeadApproved = departmentheadApproved;
	}

	public boolean isBenefitsCorrdinatorApproved() {
		return this.benefitsCorrdinatorApproved;
	}

	public void setBenefitsCorrdinatorApproved(boolean benefitscorrdinatorApproved) {
		this.benefitsCorrdinatorApproved = benefitscorrdinatorApproved;
	}


	public Date getDateDue() {
		return this.dateDue;
	}
	
	public LocalDate getLocalDateDue() {
		return this.dateDue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public String getDateDueCass() {
		if (this.getDateDue() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDateFormat.format(this.getDateDue());
		}
		return null;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}
	
	public void setDateDue(int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(this.dateReceived); // Using today's date
		c.add(Calendar.DATE, days); // Adding 5 days
		this.dateDue = c.getTime();
	}
	
	
	public void setLocalDateDue(LocalDate date) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		this.dateDue = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
	}
	
	public void setStringDateDue(String date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			this.dateDue = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	public Date getDateReceived() {
		return this.dateReceived;
	}
	
	public LocalDate getLocalDateReceived() {
		return this.dateReceived.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public String getDateReceivedCass() {
		if (this.getDateDue() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDateFormat.format(this.getDateReceived());
		}
		return null;
	}
	

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public void setLocalDateReceived(LocalDate date) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		this.dateReceived = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
	}
	
	public void setStringDateReceived(String date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			this.dateReceived = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public boolean isAmountChanged() {
		return this.isAmountChanged;
	}

	public void setAmountChanged(boolean isAmountChanged) {
		this.isAmountChanged = isAmountChanged;
	}

//	public Employee getEmployee() {
//		return employee;
//	}
//
//	public void setEmployee(Employee employee) {
//		this.employee = employee;
//	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}


	public ArrayList<Integer> getOtherId() {
		return otherId;
	}

	public void setOtherId(ArrayList<Integer> otherId) {
		this.otherId = otherId;
	}
	
	public void addOtherId(Employee e) {
		this.otherId.add(e.getId());
	}
	public void removeOtherId(Employee e) {
		for (int i = 0 ; i < this.otherId.size(); i++) {
			if (e.getId() == this.otherId.get(i)) {
				this.otherId.remove(i);
				break;
			}
		}
		
	}
	public void clearOtherId() {
		this.otherId = new ArrayList<Integer>();
	}

	public boolean isApprovalTimeLimit() {
		return approvalTimeLimit;
	}

	public void setApprovalTimeLimit(boolean approvalTimeLimit) {
		this.approvalTimeLimit = approvalTimeLimit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (approvalTimeLimit ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(availableReimbursement);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(awardedReimbursements);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (benefitsCorrdinatorApproved ? 1231 : 1237);
		result = prime * result + ((dateDue == null) ? 0 : dateDue.hashCode());
		result = prime * result + ((dateReceived == null) ? 0 : dateReceived.hashCode());
		result = prime * result + (departmentHeadApproved ? 1231 : 1237);
		result = prime * result + (directSupervisorApproved ? 1231 : 1237);
		result = prime * result + employeeId;
		result = prime * result + (formComplete ? 1231 : 1237);
		result = prime * result + ((gradeFile == null) ? 0 : gradeFile.hashCode());
		result = prime * result + (isAmountChanged ? 1231 : 1237);
		result = prime * result + ((otherId == null) ? 0 : otherId.hashCode());
		temp = Double.doubleToLongBits(pendingReimbursements);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((presentationFile == null) ? 0 : presentationFile.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		temp = Double.doubleToLongBits(totalReimbursement);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (urgent ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TuitionReimbursementForm other = (TuitionReimbursementForm) obj;
		if (approvalTimeLimit != other.approvalTimeLimit)
			return false;
		if (Double.doubleToLongBits(availableReimbursement) != Double.doubleToLongBits(other.availableReimbursement))
			return false;
		if (Double.doubleToLongBits(awardedReimbursements) != Double.doubleToLongBits(other.awardedReimbursements))
			return false;
		if (benefitsCorrdinatorApproved != other.benefitsCorrdinatorApproved)
			return false;
		if (dateDue == null) {
			if (other.dateDue != null)
				return false;
		} else if (!dateDue.equals(other.dateDue))
			return false;
		if (dateReceived == null) {
			if (other.dateReceived != null)
				return false;
		} else if (!dateReceived.equals(other.dateReceived))
			return false;
		if (departmentHeadApproved != other.departmentHeadApproved)
			return false;
		if (directSupervisorApproved != other.directSupervisorApproved)
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (formComplete != other.formComplete)
			return false;
		if (gradeFile == null) {
			if (other.gradeFile != null)
				return false;
		} else if (!gradeFile.equals(other.gradeFile))
			return false;
		if (isAmountChanged != other.isAmountChanged)
			return false;
		if (otherId == null) {
			if (other.otherId != null)
				return false;
		} else if (!otherId.equals(other.otherId))
			return false;
		if (Double.doubleToLongBits(pendingReimbursements) != Double.doubleToLongBits(other.pendingReimbursements))
			return false;
		if (presentationFile == null) {
			if (other.presentationFile != null)
				return false;
		} else if (!presentationFile.equals(other.presentationFile))
			return false;
		if (process != other.process)
			return false;
		if (Double.doubleToLongBits(totalReimbursement) != Double.doubleToLongBits(other.totalReimbursement))
			return false;
		if (urgent != other.urgent)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TuitionReimbursementForm [employeeId=" + employeeId + ", availableReimbursement="
				+ availableReimbursement + ", totalReimbursement=" + totalReimbursement + ", pendingReimbursements="
				+ pendingReimbursements + ", awardedReimbursements=" + awardedReimbursements + ", dateReceived="
				+ dateReceived + ", dateDue=" + dateDue + ", process=" + process + ", approvalTimeLimit="
				+ approvalTimeLimit + ", directSupervisorApproved=" + directSupervisorApproved
				+ ", departmentHeadApproved=" + departmentHeadApproved + ", benefitsCorrdinatorApproved="
				+ benefitsCorrdinatorApproved + ", formComplete=" + formComplete + ", otherId=" + otherId
				+ ", isAmountChanged=" + isAmountChanged + ", gradeFile=" + gradeFile + ", presentationFile="
				+ presentationFile + ", urgent=" + urgent + "]";
	}

	public boolean isFormComplete() {
		return formComplete;
	}

	public void setFormComplete(boolean formComplete) {
		this.formComplete = formComplete;
	}


	
	
}
