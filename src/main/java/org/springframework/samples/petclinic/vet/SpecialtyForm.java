package org.springframework.samples.petclinic.vet;

public class SpecialtyForm {

	private Specialty specialty;

	public SpecialtyForm() {
		super();
	}

	public SpecialtyForm(Specialty spec) {
		super();
		specialty = spec;
	}

	public Specialty getSpecialty() {
		return specialty;
	}

	public void setSpeciality(Specialty spec) {
		this.specialty = spec;
	}

	@Override
	public String toString() {
		return "EmployeeForm [id=" + specialty.getId().toString() + ", name=" + specialty.getName() + "]";
	}

}