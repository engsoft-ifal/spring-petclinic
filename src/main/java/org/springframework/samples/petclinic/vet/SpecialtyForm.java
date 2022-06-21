package org.springframework.samples.petclinic.vet;

public class SpecialtyForm {

	private String specialty;

	public SpecialtyForm() {
		super();
	}

	public SpecialtyForm(String spec) {
		super();
		specialty = spec;
	}

	public String getSpecialty() {
		return this.specialty;
	}

	public void setSpecialty(String spec) {
		this.specialty = spec;
	}

	@Override
	public String toString() {
		return "SpecialtyForm [specialty=" + this.specialty + "]";
	}

}
