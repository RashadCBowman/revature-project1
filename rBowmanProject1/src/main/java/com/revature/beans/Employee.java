package com.revature.beans;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Employee {
	
	//S KEY
	private int id;
	//NATURAL KEYS
	private String name;
	private String pass;
	private String email;
	private Role role;
	
	/*
	 * This form must collect (required) basic employee information; date time
	 * location, description, cost, grading format, and type of event; work-related justification
	 */
	private Date date; //date
	private Time time; //time
	private String location; //location
	private String description; //description
	private double cost; //cost
	private char gradingFormat; //grading format
	private Event typeOfEvent; //type of event;
	//private String workRelatedJustification;
	
	
	private TuitionReimbursementForm tuitionReimbursementForm;
	private boolean departmentHead;
	private ArrayList<Integer> approvedForm;
	private ArrayList<String> messages;
	
	public enum Role{
		EMPLOYEE, DIRECTSUPERVISOR, DEPARTMENTHEAD, BENEFITSCORRDINATOR
	}
	public enum Event {
		UNIVERSITYCOURSES, SEMINARS, CERTIFICATIONPREPARATIONCLASSES, CERTIFICATION, TECHNICALTRAINING, OTHER
	}

	public Employee() {
		super();
		this.setId(0);
		this.setName("");
		this.setPass("");
		this.setEmail("");
		this.setRole(Role.EMPLOYEE);
		this.setDate(new Date());
		this.setTime(new Time(0));
		this.setLocation("");
		this.setDescription("");
		this.setCost(0.0);
		this.setGradingFormat('F');
		this.setTypeOfEvent(Event.OTHER);
		this.setTuitionReimbursementForm(null);
		this.setDepartmentHead(false);
		this.setApprovedForm(new ArrayList<Integer>());
		this.setMessages(new ArrayList<String>());
	}
	//Shortcut for regular employees
	public Employee (int id, String name, String pass, String email, String location, String description, double cost, char gradingFormat, Event typeOfEvent) {
		this();
		this.setId(id);
		this.setName(name);
		this.setPass(pass);
		this.setEmail(email);
		this.setLocation(location);
		this.setDescription(description);
		this.setCost(cost);
		this.setGradingFormat(gradingFormat);
		this.setTypeOfEvent(typeOfEvent);
	}
	//Shortcut for higher ups
	public Employee (int id, String name, String pass, String email, Role role, String location, String description, boolean departmentHead) {
		this();
		this.setId(id);
		this.setName(name);
		this.setPass(pass);
		this.setEmail(email);
		this.setRole(role);
		this.setLocation(location);
		this.setDescription(description);
		this.setDepartmentHead(departmentHead);
	}
	
	public Employee(int id, String name, String pass, String email, Role role, Date date, Time time, String location,
			String description, double cost, char gradingFormat, Event typeOfEvent,
			TuitionReimbursementForm tuitionReimbursementForm, boolean departmentHead) {
		this();
		this.setId(id);
		this.setName(name);
		this.setPass(pass);
		this.setEmail(email);
		this.setRole(role);
		this.setDate(date);
		this.setTime(time);
		this.setLocation(location);
		this.setDescription(description);
		this.setCost(cost);
		this.setGradingFormat(gradingFormat);
		this.setTypeOfEvent(typeOfEvent);
		this.setTuitionReimbursementForm(tuitionReimbursementForm);
		this.setDepartmentHead(departmentHead);
	}
	
	public Employee(Employee e) {
		this(e.getId(),e.getName(),e.getPass(),e.getEmail(),e.getRole(),
				e.getDate(),e.getTime(),e.getLocation(),e.getDescription(),
				e.getCost(),e.getGradingFormat(),e.getTypeOfEvent(),e.getTuitionReimbursementForm(),
				e.isDepartmentHead());
	}
	
	
	

	public Role getRole() {
		return this.role;  
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isDepartmentHead() {
		return this.departmentHead;
	}

	public void setDepartmentHead(boolean departmentHead) {
		if (!this.getRole().equals(Role.EMPLOYEE)) {
			this.departmentHead = departmentHead;
		} else {
			this.departmentHead = false;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (id >= 0) {
			this.id = id;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDate() {
		return this.date;
	}
	
	public String getDateCass() {
		if (this.getDate() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDateFormat.format(this.getDate());
		}
		return null;
	}
	
	public LocalDate getLocalDate() {
		return this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setLocalDate(LocalDate date) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		this.date = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
	}
	
	public void setStringDate(String date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			this.date = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
	public void setLocalTime(LocalTime time) {
		this.time = Time.valueOf(time);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		if (cost >= 0) {
			this.cost = cost;
		}
	}

	public char getGradingFormat() {
		return gradingFormat;
	}
	
	public int getGradingFormatAsInt() {
		return gradingFormat;
	}
	
	public void setGradingFormat(char gradingFormat) {
		Character.toUpperCase(gradingFormat);
		if (gradingFormat=='A'||gradingFormat=='B'||gradingFormat=='C'||gradingFormat=='D'||gradingFormat=='F') {
			this.gradingFormat = gradingFormat;
		}
		if ((gradingFormat >= 65 && gradingFormat <= 68) || gradingFormat == 70) {
			this.gradingFormat = gradingFormat;
		}
		if ((gradingFormat >= 97 && gradingFormat <= 100) || gradingFormat == 102) {
			this.gradingFormat = gradingFormat;
		}
	}

	public Event getTypeOfEvent() {
		return typeOfEvent;
	}

	public void setTypeOfEvent(Event typeOfEvent) {
		this.typeOfEvent = typeOfEvent;
	}

	public TuitionReimbursementForm getTuitionReimbursementForm() {
		return tuitionReimbursementForm;
	}

	public void setTuitionReimbursementForm(TuitionReimbursementForm tuitionReimbursementForm) {
		this.tuitionReimbursementForm = tuitionReimbursementForm;
	}

	public ArrayList<Integer> getApprovedForm() {
		return approvedForm;
	}

	public void setApprovedForm(ArrayList<Integer> approvedForm) {
		this.approvedForm = approvedForm;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}
	public String getMessagesCass() {
		if (this.messages.size() > 0) {
			StringBuilder sb = new StringBuilder("['");
			for (int i = 0 ; i < this.messages.size() ; i++) {
				if (i == this.messages.size()-1) {
					sb.append(this.messages.get(i) + "']");
				} else {
					sb.append(this.messages.get(i) + "','");
				}
			}
			return sb.toString();
		}
		return null;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	public void removeMessage(int id) {
		if (id >= 0 && id < this.messages.size()) {
			this.messages.remove(id);
		}
	}
	public void clearMessages() {
		this.messages.clear();
	}
	
	public void addFormId(int id) {
		this.approvedForm.add(id);
	}
	public void removeFormId(int id) {
		if (id >= 0 && id < this.approvedForm.size()) {
			this.approvedForm.remove(id);
		}
	}
	public void clearFormId() {
		this.approvedForm.clear();
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approvedForm == null) ? 0 : approvedForm.hashCode());
		long temp;
		temp = Double.doubleToLongBits(cost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (departmentHead ? 1231 : 1237);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + gradingFormat;
		result = prime * result + id;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((messages == null) ? 0 : messages.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((tuitionReimbursementForm == null) ? 0 : tuitionReimbursementForm.hashCode());
		result = prime * result + ((typeOfEvent == null) ? 0 : typeOfEvent.hashCode());
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
		Employee other = (Employee) obj;
		if (approvedForm == null) {
			if (other.approvedForm != null)
				return false;
		} else if (!approvedForm.equals(other.approvedForm))
			return false;
		if (Double.doubleToLongBits(cost) != Double.doubleToLongBits(other.cost))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (departmentHead != other.departmentHead)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (gradingFormat != other.gradingFormat)
			return false;
		if (id != other.id)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (messages == null) {
			if (other.messages != null)
				return false;
		} else if (!messages.equals(other.messages))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		if (role != other.role)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (tuitionReimbursementForm == null) {
			if (other.tuitionReimbursementForm != null)
				return false;
		} else if (!tuitionReimbursementForm.equals(other.tuitionReimbursementForm))
			return false;
		if (typeOfEvent != other.typeOfEvent)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", pass=" + pass + ", email=" + email + ", role=" + role
				+ ", date=" + date + ", time=" + time + ", location=" + location + ", description=" + description
				+ ", cost=" + cost + ", gradingFormat=" + gradingFormat + ", typeOfEvent=" + typeOfEvent
				+ ", tuitionReimbursementForm=" + tuitionReimbursementForm + ", departmentHead=" + departmentHead
				+ ", approvedForm=" + approvedForm + ", messages=" + messages + "]";
	}
	
	
}
