package com.revature.data.cass;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Employee;
import com.revature.beans.TuitionReimbursementForm;
import com.revature.data.TuitionReimbursementFormDao;
import com.revature.utils.CassandraUtil;

public class TuitionReimbursementFormDaoCass implements TuitionReimbursementFormDao{
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static final Logger log = LogManager.getLogger(TuitionReimbursementFormDaoCass.class);
	
	
	@Override
	public List<TuitionReimbursementForm> getTuitionReimbursementForms() {
		log.trace("Getting Form!");
		List<TuitionReimbursementForm> arr= new ArrayList<TuitionReimbursementForm>();
		StringBuilder sb = new StringBuilder("Select * from form;"); 
		ResultSet rs = CassandraUtil.getInstance().getSession().execute(sb.toString());
		rs.forEach(e -> {
			TuitionReimbursementForm form = new TuitionReimbursementForm();
			form.setEmployeeId(e.getInt("employeeId"));
			form.setAvailableReimbursement(e.getDouble("availableReimbursement"));
			form.setTotalReimbursement(e.getDouble("totalReimbursement"));
			form.setPendingReimbursements(e.getDouble("pendingReimbursements"));
			form.setAwardedReimbursements(e.getDouble("awardedReimbursements"));
			form.setLocalDateReceived(e.getLocalDate("dateReceived"));
			form.setLocalDateDue(e.getLocalDate("dateDue"));
			form.setProcess(TuitionReimbursementForm.Process.valueOf(e.getString("process")));
			form.setApprovalTimeLimit(e.getBoolean("approvalTimeLimit"));
			form.setDirectSupervisorApproved(e.getBoolean("directSupervisorApproved"));
			form.setDepartmentHeadApproved(e.getBoolean("departmentHeadApproved"));
			form.setBenefitsCorrdinatorApproved(e.getBoolean("benefitsCorrdinatorApproved"));
			form.setFormComplete(e.getBoolean("isFormComplete"));
			form.setOtherId((ArrayList<Integer>) e.getList("otherId", Integer.class));
			form.setAmountChanged(e.getBoolean("isAmountChanged"));
			form.setGradeFile(e.getString("gradeFile"));
			form.setPresentationFile(e.getString("presentationFile"));
			form.setUrgent(e.getBoolean("urgent"));
			log.trace(form);
			arr.add(form);
		});
		log.trace("Returning array");
		return arr;
	}




	@Override
	public TuitionReimbursementForm getTuitionReimbursementForm() {

		return null;
	}

	public TuitionReimbursementForm addForm(Employee e, TuitionReimbursementForm t) {
		log.trace("addForm");
		t.setEmployeeId(e.getId());
		StringBuilder sb = new StringBuilder("INSERT INTO form (employeeId, availableReimbursement, totalReimbursement, pendingReimbursements, awardedReimbursements, dateReceived, dateDue, process, approvalTimeLimit, directSupervisorApproved, departmentHeadApproved, benefitsCorrdinatorApproved, isFormComplete, otherId, isAmountChanged, gradeFile, presentationFile, urgent) VALUES (")
				.append(t.getEmployeeId() + ",")
				.append(t.getAvailableReimbursement() + ",")
				.append(t.getTotalReimbursement() + ",")
				.append(t.getPendingReimbursements() + ",")
				.append(t.getAwardedReimbursements() + ",'")
				.append(t.getDateReceivedCass() + "','")
				.append(t.getDateDueCass() + "','")
				.append(t.getProcess() + "',")
				.append(t.isApprovalTimeLimit() + ",")
				.append(t.isDirectSupervisorApproved() + ",")
				.append(t.isDepartmentHeadApproved() + ",")
				.append(t.isBenefitsCorrdinatorApproved() + ",")
				.append(t.isFormComplete() + ",")
				.append(t.getOtherId() + ",")
				.append(t.isAmountChanged() + ",'")
				.append(t.getGradeFile() + "','")
				.append(t.getPresentationFile() + "',")
				.append(t.isUrgent()).append(");");
		SimpleStatement s = new SimpleStatementBuilder(sb.toString())
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		CassandraUtil.getInstance().getSession().execute(s);
		log.trace(t);
		return t;
	}



	@Override
	public void updateTuitionReimbursementForm(TuitionReimbursementForm t) {
		log.trace("UpdateForm!");
		String query = "update form set availableReimbursement = ?,"
				+ " totalReimbursement = ?,"
				+ " pendingReimbursements = ?,"
				+ " awardedReimbursements = ?,"
				+ " dateReceived = ?,"
				+ " dateDue = ?,"
				+ " process = ?,"
				+ " approvalTimeLimit = ?,"
				+ " directSupervisorApproved = ?,"
				+ " departmentHeadApproved = ?,"
				+ " benefitsCorrdinatorApproved = ?,"
				+ " isFormComplete = ?,"
				+ " otherId = ?,"
				+ " isAmountChanged = ?,"
				+ " urgent= ?"
				+ " where employeeId = ?";
		log.trace("SimpleStatement!");
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		log.trace("BoundStatement!");
		BoundStatement bound = session.prepare(s).bind(t.getAvailableReimbursement(),
				t.getTotalReimbursement(),
				t.getPendingReimbursements(),
				t.getAwardedReimbursements(),
				t.getLocalDateReceived(),
				t.getLocalDateDue(),
				t.getProcess().toString(),
				t.isApprovalTimeLimit(),
				t.isDirectSupervisorApproved(),
				t.isDepartmentHeadApproved(),
				t.isBenefitsCorrdinatorApproved(),
				t.isFormComplete(),
				t.getOtherId(),
				t.isAmountChanged(),
				t.isUrgent(),
				t.getEmployeeId());
		log.trace("Execute bound!");
		session.execute(bound);
		log.trace("Method Complete!");
		
	}
	
	public List<Employee> getEmployeesRelatedToForm(TuitionReimbursementForm t){
		List<Employee> employees = new EmployeeDaoCass().getEmployees();
		List<Employee> arr = new ArrayList<Employee>();
			for (int id : t.getOtherId()) {
				for (Employee e : employees) {
					if (e.getId() == id) {
						arr.add(e);
					}
				}
			}
		return arr;
	}
}
