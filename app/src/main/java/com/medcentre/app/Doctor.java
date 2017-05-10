package com.medcentre.app;

public class Doctor {
	private int dId;
	private String dFirstName;
	private String dLastName;
	private String dPhoneNumber;
	private String dEmail;
	private String dArea;

	/*

		id = id;
		firstName = firstName;
		lastName = lastName;
		phoneNumber = phoneNumber;
		email = email;
		area = area;
	 */

	public Doctor(int id, String firstName, String lastName, String phoneNumber, String email, String area) {
        dId = id;
        dFirstName = firstName;
        dLastName = lastName;
        dPhoneNumber = phoneNumber;
        dEmail = email;
        dArea = area;
	}

	public Doctor(String firstName, String lastName, String phoneNumber, String email, String area) {
		this(-1, firstName, lastName, phoneNumber, email, area);
	}

	public int getId() {
		return dId;
	}
	public String getFirstName() {
		return dFirstName;
	}
	public String getLastName() {
		return dLastName;
	}
	public String getPhoneNumber() {
		return dPhoneNumber;
	}
	public String getEmail() {
		return dEmail;
	}
	public String getArea() {
		return dArea;
	}

	public void setFirstName(String firstName) {
		dFirstName = firstName;
	}
	public void setLastName(String lastName) {
		dLastName = lastName;
	}
	public void setPhoneNumber(String phoneNumber) {
		dPhoneNumber = phoneNumber;
	}
	public void setEmail(String email) {
		dEmail = email;
	}
	public void setArea(String area) {
		dArea = area;
	}
}