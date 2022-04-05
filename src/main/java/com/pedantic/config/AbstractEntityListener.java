package com.pedantic.config;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.pedantic.entities.AbstractEntity;
//entity listener
public class AbstractEntityListener {
	@PrePersist
	public void setCreatedOn(AbstractEntity abstractEntity) {
		abstractEntity.setCreatedOn(LocalDateTime.now());
	}
	@PreUpdate
	public void setUpdatedOn(AbstractEntity abstractEntity) {
		abstractEntity.setUpdatedOn(LocalDateTime.now());
	}
	

}
