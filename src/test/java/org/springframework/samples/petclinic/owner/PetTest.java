package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;
import static org.assertj.core.api.Assertions.assertThat;

public class PetTest {

	@Test
	void testSerialization() {
		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Doguinho");

		Pet pet = new Pet();
		pet.setName("Auau");
		pet.setBirthDate(LocalDate.of(2020, 10, 19));
		pet.setType(petType);
		pet.setId(123);
		Pet other = (Pet) SerializationUtils.deserialize(SerializationUtils.serialize(pet));
		assertThat(other.getName()).isEqualTo(pet.getName());
		assertThat(other.getBirthDate()).isEqualTo(pet.getBirthDate());
		assertThat(other.getType().getId()).isEqualTo(pet.getType().getId());
		assertThat(other.getId()).isEqualTo(pet.getId());
	}

    @Test
    void testToString() {
        Pet pet = new Pet();
        pet.setName("Auau");
        pet.setBirthDate(LocalDate.of(2020, 10, 19));
        pet.setType(new PetType());
        pet.setId(123);
        assertThat(pet.toString()).isEqualTo("Auau");
    }
}
