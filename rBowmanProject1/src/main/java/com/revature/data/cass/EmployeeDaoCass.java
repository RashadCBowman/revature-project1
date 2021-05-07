package com.revature.data.cass;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.beans.Employee;
import com.revature.beans.Employee.Role;
import com.revature.beans.TuitionReimbursementForm;
import com.revature.data.EmployeeDao;
import com.revature.data.TuitionReimbursementFormDao;
import com.revature.utils.CassandraUtil;

import jdk.internal.net.http.common.Log;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeeDaoCass implements EmployeeDao{
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private static final Logger log = LogManager.getLogger(EmployeeDaoCass.class);

	@Override
	public List<Employee> getEmployees() {
		List<Employee> arr = new ArrayList<Employee>();
		String query = "select * from employee";
		ResultSet rs = session.execute(query);
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
	
	public void addEmployee(Employee e) {
		List<Employee> arr = getEmployees();
		e.setId(arr.size());
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
	


	@Override
	public void updateEmployee(Employee e) {
		log.trace("UpdateEmployee!");
		String query = "update employee set role = ?,"
				+ " gradingFormat = ?,"
				+ " approvedForm = ?,"
				+ " messages= ?"
				+ " where name = ?";
		log.trace("SimpleStatement!");
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		log.trace("BoundStatement!");
		BoundStatement bound = session.prepare(s).bind(e.getRole().toString(),
				e.getGradingFormatAsInt(),
				e.getApprovedForm(),
				e.getMessages(),
				e.getName());
		log.trace("Execute bound!");
		session.execute(bound);
		log.trace("Method Complete!");
	}
	


	@Override
	public Employee getEmployeeById(int id) {
		List<Employee> employee = getEmployees();
			for (Employee e : employee) {
				if (e.getId() == id) {
					return e;
				}
			}
		return null;
	}

	@Override
	public Employee getEmployeeByName(String name) {
		List<Employee> employees =  getEmployees();
		List<Employee> arr = getEmployees().stream()
				.filter(t -> t.getName().equals(name))
				.collect(Collectors.toList());
		return arr.get(0);
	}
	
	@Override
	public Employee getEmployeeByNameAndPass(String name, String pass) {
		log.trace("name is " + name + " pass is "+ pass + "!");
		List<Employee> employees =  getEmployees();
		List<Employee> arr = getEmployees().stream()
				.filter(t -> t.getName().equals(name) && t.getPass().equals(pass))
				.collect(Collectors.toList());
		log.trace("Employee is " + arr.get(0));
		return arr.get(0);
	}
	
	
	public List<Employee> getEmployeesByRole(Role role){
		List<Employee> employees =  getEmployees();
		List<Employee> arr = getEmployees().stream()
				.filter(t -> t.getRole().equals(role))
				.collect(Collectors.toList()); 
//		employees.forEach(e ->{
//			if (e.getRole().equals(role)) {
//				arr.add(e);
//			}
//		});
		return arr;
	}

	@Override
	public TuitionReimbursementForm getEmployeeForm(Employee e) {
		TuitionReimbursementFormDao td = new TuitionReimbursementFormDaoCass();
		List<TuitionReimbursementForm> trf = td.getTuitionReimbursementForms();
		for (TuitionReimbursementForm arr : trf) {
			if (e.getId() == arr.getEmployeeId()) {
				return arr;
			}
		}
		return null;
	}
}
