/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

	import org.junit.jupiter.api.Test;
	import org.springframework.core.style.ToStringCreator;
	import org.springframework.samples.petclinic.vet.Vet;
	import org.springframework.util.SerializationUtils;

	import javax.persistence.EntityManager;
	import java.time.LocalDate;

	import static org.assertj.core.api.Assertions.assertThat;
	import static org.mockito.Mockito.mock;
	import static org.mockito.Mockito.when;

/**
 * @author Dave Syer
 */
class OwnerTests {

	@Test
	void testSerialization() {
		Owner owner = new Owner();
		owner.setFirstName("Joe");
		owner.setLastName("Silva");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("123-456-7890");
		owner.setTelephoneTwo("123-456-7891");
		owner.setId(123);
		Owner other = (Owner) SerializationUtils.deserialize(SerializationUtils.serialize(owner));
		assertThat(other.getFirstName()).isEqualTo(owner.getFirstName());
		assertThat(other.getLastName()).isEqualTo(owner.getLastName());
		assertThat(other.getAddress()).isEqualTo(owner.getAddress());
		assertThat(other.getCity()).isEqualTo(owner.getCity());
		assertThat(other.getTelephone()).isEqualTo(owner.getTelephone());
		assertThat(other.getTelephoneTwo()).isEqualTo(owner.getTelephoneTwo());
		assertThat(other.getId()).isEqualTo(owner.getId());
	}

	@Test
	void toStringTest() {
		Owner owner = new Owner();
		owner.setFirstName("Joe");
		owner.setLastName("Silva");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("123-456-7890");
		owner.setTelephoneTwo("123-456-7891");
		owner.setId(123);

		Object exp = new ToStringCreator(owner).append("id", owner.getId()).append("new", owner.isNew())
			.append("lastName", owner.getLastName()).append("firstName", owner.getFirstName())
			.append("address", owner.getAddress()).append("city", owner.getCity()).append("telephone", owner.getTelephone())
			.append("telephoneTwo", owner.getTelephoneTwo());

		assertThat(owner.toString()).isEqualTo(exp.toString());
	}

	@Test
	void testAddVisit() {
		Owner owner = new Owner();
		owner.setFirstName("Joe");
		owner.setLastName("Silva");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("123-456-7890");
		owner.setTelephoneTwo("123-456-7891");
		owner.setId(123);

		Pet samplePet = new Pet();
		samplePet.setId(123);
		samplePet.setName("Fido");
		samplePet.setBirthDate(LocalDate.now());
		samplePet.setType(new PetType());


		Visit visit = new Visit();
		visit.setId(123);
		visit.setDate(LocalDate.now());
		visit.setDescription("description");

		Owner mockOwner = mock(Owner.class);
		when(mockOwner.addVisit(samplePet.getId(), visit)).thenReturn(owner);

		assertThat(mockOwner.addVisit(123, visit)).isEqualTo(owner);
	}
}
