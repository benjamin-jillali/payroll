package com.pedantic.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.SessionScoped;

import com.pedantic.entities.Employee;

//Application state is simply a session scoped cdi bean with email
//we use it to store the currently logged in users email in the session scope
@SessionScoped
public class ApplicationState implements Serializable{
	
	private String email;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	

}
