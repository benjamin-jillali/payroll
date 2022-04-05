package com.pedantic.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	
	/*
	 * iprimary int, byte, short, long and char
	 * warpper classes of primitives: Byte, Integer, Short, Long and Character
	 * lava.lang.String, large numeric type java.math.BigInteger also 
	 * java.util.date and java.sql.date
	 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //if db supports primary key generation allows db to generate key natively aka autoNumber
//   @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String userEmail;

    @Version
    protected Long version;
    
    protected LocalDateTime createdOn;
    protected LocalDateTime updatedOn;

    public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

//	  uses property access
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
