package com.pedantic.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
//jpa entity
@Entity
//named query selects user based on thier email
@NamedQuery(name = ApplicationUser.FIND_USER_BY_CREDENTIALS, query = "select u from ApplicationUser u where u.email = :email")
public class ApplicationUser extends AbstractEntity{
//public class ApplicationUser{
	public static final String FIND_USER_BY_CREDENTIALS = "User.findUserByCredentials";
//	@SequenceGenerator(name="User_seq", sequenceName = "User_Sequence")
//	@GeneratedValue(generator = "User_seq")
//	@Id
//	required for sequence to work something like the following is needed in the database
//	CREATE SEQUENCE Emp_Seq
//	MINVALUE 1
//	START WITH 1
//	INCREMENT BY 50
//	private Long id;
//	the @FormParam injects the form input from the html form
	@NotEmpty(message ="Email must be set")
	@Email(message = "The email in the form user@domain.com")
	@FormParam("email")
	private String email;
	@Size(min = 8)
	//@Pattern(regexp = "", message = "password must be in the form....")
	@FormParam("password")
    private String password;

    public Long getId() {
		return id;
	}
    
    private String salt;

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public void setId(Long id) {
		this.id = id;
	}
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
