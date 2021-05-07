package com.revature.controllers;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.Employee;
import com.revature.beans.Employee.Role;
import com.revature.beans.TuitionReimbursementForm;
import com.revature.data.cass.EmployeeDaoCass;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.TuitionReimbursementFormService;
import com.revature.services.TuitionReimbursementFormServiceImpl;
import com.revature.utils.S3Util;

import io.javalin.http.Context;

public class EmployeeController {
	private static EmployeeService es = new EmployeeServiceImpl();
	private static TuitionReimbursementFormService ts = new TuitionReimbursementFormServiceImpl();
	private static final Logger log = LogManager.getLogger(EmployeeController.class);
	
	
	public static void register(Context ctx) {
		log.trace("register");
		Employee e = new Employee();
		log.trace("name");
		e.setName(ctx.formParam("name"));
		log.trace("pass");
		e.setPass(ctx.formParam("pass"));
		log.trace("email");
		e.setEmail(ctx.formParam("email"));
		log.trace("location");
		e.setLocation(ctx.formParam("location"));
		log.trace("description");
		e.setDescription(ctx.formParam("description"));
		log.trace("cost");
		e.setCost(Double.parseDouble(ctx.formParam("cost")));
		log.trace("grading Format skip");
		//e.setGradingFormat((char)Integer.parseInt(ctx.formParam("gradingFormat")));
		log.trace("typeofEvent");
		e.setTypeOfEvent(Employee.Event.valueOf(ctx.formParam("typeOfEvent")));
		
		
		boolean added = es.addEmployee(e);
		if (added) {
			ctx.json(e);
			ctx.status(200);
		} else {
			ctx.status(409);
		}
	}
	
	public static void alt(Context ctx) {
		log.trace("register");
		Employee e = ctx.bodyAsClass(Employee.class);
		
		
		
		boolean added = es.addEmployee(e);
		if (added) {
			ctx.json(e);
			ctx.status(200);
		} else {
			ctx.status(409);
		}
	}
	

	public static void login(Context ctx) {
		log.trace("login");
		if(ctx.sessionAttribute("Employee") != null) {
			ctx.status(204);
			return;
		}
		String name = ctx.pathParam("name");
		String pass = ctx.pathParam("pass");
		Employee e = es.getEmployeeByNameAndPass(name, pass);
		log.trace(e);
		if (e == null) {
			ctx.status(401);
		} else {
			//ts.autoApproved();
			ctx.sessionAttribute("Employee", e);
			ctx.json(e);
			ctx.status(200);
		}
	}
	
	public static void logout(Context ctx) {
		log.trace("logout");
		ctx.req.getSession().invalidate();
		ctx.status(200);
	}
	
	
	public static void getEmployees(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		if(e == null || e.getRole().equals(Role.EMPLOYEE)) {
			ctx.status(403);
			return;
		}
		List<Employee> employees = es.getEmployees();
		ctx.json(employees);
		ctx.status(200);
	}
	
	
	public static void form(Context ctx) {
		log.trace("form");
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		log.trace(e);
		log.trace(e.getRole());
		if(e == null || !e.getRole().equals(Role.EMPLOYEE)) {
			ctx.status(403);
			return;
		}
		
		
		
		TuitionReimbursementForm t = new TuitionReimbursementForm();
		t.setPendingReimbursements(Double.parseDouble(ctx.formParam("pendingReimbursements")));
		t.setAwardedReimbursements(Double.parseDouble(ctx.formParam("awardedReimbursements")));
		t = ts.addForm(e, t);
		boolean added = t== null ? false : true;
		if (added) {
			ctx.json(t);
			ctx.status(200);
		} else {
			ctx.status(409);
		}
	}
	
	public static void viewform(Context ctx) {
		log.trace("form");
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		log.trace(e);
		log.trace(e.getRole());
		if(e != null || e.getRole().equals(Role.EMPLOYEE)) {
			
			TuitionReimbursementForm t = new EmployeeDaoCass().getEmployeeForm(e);
			log.trace(t);
			if (t != null) {
				ctx.json(t);
				ctx.status(200);
			} else {
				ctx.status(409);
			}
		}
	}
	
	
	public static void cancel(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		if(e == null || !e.getRole().equals(Role.EMPLOYEE)) {
			ctx.status(403);
			return;
		}
				
		if(es.cancelRequest(e)){
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	}
	
	
	public static void approve(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		if(e == null || e.getRole().equals(Role.EMPLOYEE)) {
			ctx.status(403);
			return;
		}
		int id = Integer.parseInt(ctx.pathParam("id"));
		
		if(es.approveTuitionReimbursement(e, id)){
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	}
	
	public static void deny(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		if(e == null || e.getRole().equals(Role.EMPLOYEE)) {
			ctx.status(403);
			return;
		}
		int id = Integer.parseInt(ctx.pathParam("id"));
		String reason = ctx.formParam("reason");
		
		
		if(es.denyTuitionReimbursement(e, id, reason)){
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	}
	
	public static void request(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		if(e == null || e.getRole().equals(Role.EMPLOYEE)) {
			ctx.status(403);
			return;
		}
		int id = Integer.parseInt(ctx.pathParam("id"));
		
		
		if(es.requestAdditionalInformation(e, id)){
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	}
	
	
	public static void change(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		if(e == null || !e.getRole().equals(Role.BENEFITSCORRDINATOR)) {
			ctx.status(403);
			return;
		}
		int id = Integer.parseInt(ctx.pathParam("id"));
		double change = Double.parseDouble(ctx.pathParam("change"));
		
		
		if(es.changeReimbursementAmount(e, id, change)){
			ctx.status(200);
		} else {
			ctx.status(401);
		}
	}
	
	public static void view(Context ctx) {
		Employee e = (Employee) ctx.sessionAttribute("Employee");
		TuitionReimbursementForm t = null;
		if(e != null) {
			if (e.getRole().equals(Role.EMPLOYEE)) {
			//	t = es.viewGrade(e);
			} else {
				int id = Integer.parseInt(ctx.pathParam("id"));
			//	t = es.viewGrade(e, id);
			}
			
			if (t == null) {
				ctx.status(401);
			} else {
				ctx.json(t);
				ctx.status(200);
			}
		} else {
			ctx.status(403);
			return;
		}
	}
	
	
	public static void upload(Context ctx) {
		String name = new StringBuilder(ctx.pathParam("name")).toString();//.append(".png").toString();
		if (fileTypes(name)) {
			byte[] bytes = ctx.bodyAsBytes();
			try {
				S3Util.getInstance().uploadToBucket(name, bytes);
			} catch(Exception e) {
				ctx.status(500);
			}
			ctx.status(204);
		} else {
			ctx.status(403);
		}
		
	}
	
	public static void download(Context ctx) {
		String name = new StringBuilder(ctx.pathParam("name")).append(".png").toString();
		try {
			InputStream s = S3Util.getInstance().getObject(name);
			ctx.result(s);
		} catch(Exception e) {
			ctx.status(500);
		}
	}
	
	
	
	public static boolean fileTypes(String str) {
		List<String> types = Arrays.asList("pdf", "png", "jpeg", "txt", "doc");
		for (String t : types) {
			if (t.equals(str.substring(str.length()-3, str.length()))){
				return true;
				
			}
		}
		return false;
	}
	
}
