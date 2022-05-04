package org.springframework.samples.petclinic.vet;

public class DayForm {

	private String day;

	public DayForm() {
		super();
	}

	public DayForm(String day) {
		super();
		this.day = day;
	}

	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "DayForm [day=" + this.day + "]";
	}

}
