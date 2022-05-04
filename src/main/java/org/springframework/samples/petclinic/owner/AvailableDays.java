package org.springframework.samples.petclinic.owner;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.model.BaseEntity;

@Entity
@Table(name = "available_days")
public class AvailableDays extends BaseEntity {

	@NotEmpty
	private String name;

	public String getName() {
		return this.name;
	}

	public void setDate(String name) {
		this.name = name;
	}

}
