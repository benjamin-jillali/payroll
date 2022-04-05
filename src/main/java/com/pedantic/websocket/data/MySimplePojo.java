package com.pedantic.websocket.data;
//jee websocket api allow us to send different representations of a data type
//here we want to send this pojo as a json representative over the websocket protocol
public class MySimplePojo {
	private String name;
	private String email;
	private String comment;
	public MySimplePojo(){
		
	}
	public MySimplePojo(String name, String email, String comment) {
		super();
		this.name = name;
		this.email = email;
		this.comment = comment;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
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
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return String.format("User %s with email %s says %s", getName(), getEmail(), getComment());
	}
	

}
