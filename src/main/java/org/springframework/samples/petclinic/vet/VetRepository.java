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
package org.springframework.samples.petclinic.vet;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Repository class for <code>Vet</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface VetRepository extends Repository<Vet, Integer> {

	/**
	 * Retrieve all <code>Vet</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
	@Transactional(readOnly = true)
	@Cacheable("vets")
	Collection<Vet> findAll() throws DataAccessException;

	/**
	 * Retrieve all <code>Vet</code>s from data store in Pages
	 * @param pageable
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	@Cacheable("vets")
	Page<Vet> findAll(Pageable pageable) throws DataAccessException;

	;

	@Query("SELECT specialty FROM Specialty specialty")
	@Transactional(readOnly = true)
	List<Specialty> findVetSpecialties();

	/**
	 * Save an {@link Vet} to the data store, either inserting or updating it.
	 * @param vet the {@link Vet} to save
	 */
	void save(Vet vet);

	/**
	 * Retrieve an {@link Vet} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Vet} if found
	 */
	@Query("SELECT vet FROM Vet vet WHERE vet.id =:id")
	@Transactional(readOnly = true)
	Vet findById(@Param("id") Integer id);

	@Query("SELECT spec FROM Specialty spec WHERE spec.name =:name")
	@Transactional(readOnly = true)
	Specialty findSpecialtyByName(@Param("name") String name);

	@Query("SELECT spec FROM Specialty spec WHERE spec.id =:id")
	@Transactional(readOnly = true)
	Specialty findSpecialtyById(@Param("id") Integer id);

}
