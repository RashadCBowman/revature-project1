package com.revature;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Employee;
import com.revature.beans.Employee.Event;
import com.revature.beans.TuitionReimbursementForm;
import com.revature.controllers.EmployeeController;
import com.revature.data.EmployeeDao;
import com.revature.data.TuitionReimbursementFormDao;
import com.revature.data.cass.EmployeeDaoCass;
import com.revature.data.cass.TuitionReimbursementFormDaoCass;
import com.revature.utils.CassandraUtil;

import io.javalin.Javalin;

public class Driver {

	
	
	
	public static void main(String[] args) {
		
		
		javalin();
		 //dbtest(); // created my keyspace
		//readEmployees();
		//readForms();
		//employeeKeyspace();
		//createEmployeeTable();
		//createFormTable();
		//addEmployeesToTable();
		//addFormToTable();
		//updateEmployee();
		//updateForm();
		System.out.println("Hello World");
	}
	
	
	
	public static void updateEmployee() {
		List<Employee> arr = readEmployees();
		arr.get(0).addMessage("This is Sparta!");
		EmployeeDao ed = new EmployeeDaoCass();
		ed.updateEmployee(arr.get(0));
	}
	
	public static void updateForm() {
		List<TuitionReimbursementForm> arr = readForms();
		arr.get(0).setDirectSupervisorApproved(true);
		arr.get(0).setDepartmentHeadApproved(true);
		TuitionReimbursementFormDao td = new TuitionReimbursementFormDaoCass();
		td.updateTuitionReimbursementForm(arr.get(0));
		
	}
	
	
	
	public static List<Employee> readEmployees() {
		List<Employee> arr= new ArrayList<Employee>();
		StringBuilder sb = new StringBuilder("Select * from employee;");
		ResultSet rs = CassandraUtil.getInstance().getSession().execute(sb.toString());
		rs.forEach(e -> {
			Employee employee = new Employee();
			employee.setId(e.getInt("id"));
			employee.setName(e.getString("name"));
			employee.setPass(e.getString("pass"));
			employee.setEmail(e.getString("email"));
			employee.setRole(Employee.Role.valueOf(e.getString("role")));
			employee.setLocalDate(e.getLocalDate("date"));
			employee.setLocalTime(e.getLocalTime("time"));
			employee.setLocation(e.getString("location"));
			employee.setDescription(e.getString("description"));
			employee.setCost(e.getDouble("cost"));
			employee.setGradingFormat((char)e.getInt("gradingFormat"));
			employee.setTypeOfEvent(Employee.Event.valueOf(e.getString("typeOfEvent")));
			employee.setDepartmentHead(e.getBoolean("departmentHead"));
			employee.setApprovedForm((ArrayList<Integer>) e.getList("approvedForm", Integer.class));
			employee.setMessages((ArrayList<String>) e.getList("messages", String.class));
			System.out.println(employee);
			arr.add(employee);
		});
		
		return arr;
	}
	
	
	public static List<TuitionReimbursementForm> readForms() {
		List<TuitionReimbursementForm> arr= new ArrayList<TuitionReimbursementForm>();
		StringBuilder sb = new StringBuilder("Select * from form;");
		//System.out.println(arr.get(0)); 
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
			System.out.println(form);
			arr.add(form);
		});
		
		return arr;
	}
	
	
	public static void addEmployeesToTable() {
		//PlayerDao textDao = new PlayerDaoFile();
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee(1,"Bob","Bob","bobbyboy@yahoo.com","Baltimore", "Seniour Year", 300.00, 'b',Event.SEMINARS));
		employees.add(new Employee(2,"Ted","Ted","teddytoon@yahoo.com","Flordia", "Seniour Year", 200.00, 'a',Event.CERTIFICATION));
		employees.add(new Employee(3,"Tim","Tim","timmytuesday@yahoo.com","NoWhere", "Seniour Year", 400.00, 'c',Event.OTHER));
		employees.get(0).addMessage("Hello World");
		employees.get(0).addMessage("How are you");
		employees.get(0).addFormId(1);
		employees.get(0).addFormId(7);
		
		
		employees.forEach((s) -> {
			System.out.println(s);
		});
		
		//System.out.println(employees.get(0));
		//System.out.println(employees.get(0).getMessages());
		
		for (Employee e : employees) {
			StringBuilder sb = new StringBuilder("INSERT INTO employee (id, name, pass, email, role, date, time, location, description, cost, gradingFormat, typeOfEvent, departmentHead, approvedForm, messages) VALUES (")
					.append(e.getId() + ",'")
					.append(e.getName() + "','")
					.append(e.getPass() + "','")
					.append(e.getEmail() + "','")
					.append(e.getRole() + "','")
					.append(e.getDateCass() + "','")
					.append(e.getTime() + "','")
					.append(e.getLocation() + "','")
					.append(e.getDescription() + "',")
					.append(e.getCost() + ",")
					.append(e.getGradingFormatAsInt() + ",'")
					.append(e.getTypeOfEvent() + "',")
					.append(e.isDepartmentHead() + ",")
					.append(e.getApprovedForm() + ",")
					.append(e.getMessagesCass()).append(");");
			SimpleStatement s = new SimpleStatementBuilder(sb.toString())
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			CassandraUtil.getInstance().getSession().execute(s);
		}
	}
	
	public static void addFormToTable() {
		//PlayerDao textDao = new PlayerDaoFile();
		List<TuitionReimbursementForm> form = new ArrayList<TuitionReimbursementForm>();
		form.add(new TuitionReimbursementForm());

		
		
		form.forEach((s) -> {
			System.out.println(s);
		});
		
		//System.out.println(employees.get(0));
		//System.out.println(employees.get(0).getMessages());
		
		for (TuitionReimbursementForm t : form) {
			StringBuilder sb = new StringBuilder("INSERT INTO form (employeeId, availableReimbursement, totalReimbursement, pendingReimbursements, awardedReimbursements, dateReceived, dateDue, process, approvalTimeLimit, directSupervisorApproved, departmentHeadApproved, benefitsCorrdinatorApproved, formComplete, otherId, isAmountChanged, gradeFile, presentationFile urgent) VALUES (")
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
		}
	}
	
	
	
	public static void dbtest() {
		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append("Employee with replication = {")
				.append("'class':'SimpleStrategy','replication_factor':1};");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void employeeKeyspace() {
		DriverConfigLoader loader = DriverConfigLoader.fromClasspath("application.conf");
		try(CqlSession session = CqlSession.builder()
				.withConfigLoader(loader)
				.build()) {
			StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
					.append("project1 with replication = {")
					.append("'class':'SimpleStrategy','replication_factor':1};");
			session.execute(sb.toString());
		}
		
		
		
	}
	public static void createEmployeeTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("Employee (")
				.append("id int,")
				.append("name text PRIMARY KEY,")
				.append("pass text,")
				.append("email text,")
				.append("role text,")
				.append("date date,")
				.append("time time,")
				.append("location text,")
				.append("description text,")
				.append("cost double,")
				.append("gradingFormat int,")
				.append("typeOfEvent text,")
				.append("departmentHead boolean,")
				.append("approvedForm list<int>,")
				.append("messages list<text>);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	
	public static void createFormTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("Form (")
				.append("employeeId int PRIMARY KEY,")
				.append("availableReimbursement double,")
				.append("totalReimbursement double,")
				.append("pendingReimbursements double,")
				.append("awardedReimbursements double,")
				.append("dateReceived date,")
				.append("process text,")
				.append("dateDue date,")
				.append("approvalTimeLimit boolean,")
				.append("directSupervisorApproved boolean,")
				.append("departmentHeadApproved boolean,")
				.append("benefitsCorrdinatorApproved boolean,")
				.append("isFormComplete boolean,")
				.append("otherId list<int>,")
				.append("isAmountChanged boolean,")
				.append("gradeFile text,")
				.append("presentationFile text,")
				.append("urgent boolean);");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	
	
	public static void javalin() {
		Javalin app = Javalin.create().start(8080);

		
		// Employee Controller
		// All 
		app.put("/employee", EmployeeController::register);
		app.post("/employee/:name/:pass", EmployeeController::login);
		app.delete("/employee", EmployeeController::logout);
		app.put("/employee/form", EmployeeController::form);
		app.get("/employee/form", EmployeeController::viewform);
		app.delete("/employee/form", EmployeeController::cancel);
//		app.post("/employee/form", EmployeeController::view);
		app.post("/employee/:name/form", EmployeeController::upload);
		app.get("/employee/:name/form", EmployeeController::download);
		
		// Employee Only
//		app.put("/employee/form", EmployeeController::cancel);
//		app.post("/employee", EmployeeController::login);
//		app.delete("/employee", EmployeeController::logout);
//		
//		//ADMIN
		app.put("/employee/alt", EmployeeController::alt);
		app.post("/employee/:id", EmployeeController::approve);
		app.get("/employee/:id", EmployeeController::request);
		app.delete("/employee/:id", EmployeeController::deny);
		app.post("/employee/:id/:change", EmployeeController::change);
		app.get("/employee", EmployeeController::getEmployees);
//		app.put("/employee/approve", EmployeeController::cancel);

		

	}
}
