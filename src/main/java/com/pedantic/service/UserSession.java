package com.pedantic.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateful;
//stateful keeps state and creates one seperate per session
@Stateful
//the container may passivate stateful beans until they are needed so they often implent Serializable
public class UserSession implements Serializable{
	public String getCurrentUserName() {
		return "";
	}
	
	@PostConstruct
	private void init() {
		
	}
	
	@PreDestroy
	private void destroy() {
		
	}
	@PrePassivate
	private void passivate() {
		
	}
	@PostActivate
	private void active() {
		
	}

}
