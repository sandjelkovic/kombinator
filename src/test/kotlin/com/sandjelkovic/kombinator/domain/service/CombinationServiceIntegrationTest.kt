package com.sandjelkovic.kombinator.domain.service

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class CombinationServiceIntegrationTest {
    @Autowired
    lateinit var service: CombinationService
    @Autowired
    lateinit var combinationRepository: CombinationRepository
    @Autowired
    lateinit var entityManager: EntityManager

    private val invalidId = 100000L

    @Before
    fun before() {
        combinationRepository.deleteAll()
        refreshJPAContext()
    }

    @After
    fun after() {
        combinationRepository.deleteAll()
        refreshJPAContext()
    }

    @Test
    fun getByInternalIdSuccess() {
        val uuid = UUID.randomUUID().toString()
        val savedCombination = combinationRepository.save(Combination(uuid = uuid)).copy()

        val optional = service.getCombinationByInternalId(savedCombination.id!!)

        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(true))

        // Hibernate and it's proxies...
        val retrievedCombination = optional.get().copy()

        assertThat(retrievedCombination.uuid, notNullValue())
        assertThat(retrievedCombination.uuid, equalTo(uuid))
        assertThat(retrievedCombination, samePropertyValuesAs(savedCombination))
    }

    @Test
    fun getByInternalIdNonExisting() {
        val optional = service.getCombinationByInternalId(invalidId)

        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(false))
    }

    @Test
    fun findAllCombinations() {
        val savedCombinations = (100..105)
                .map { Combination(id = it.toLong()) }
                .map { combinationRepository.save(it) }

        refreshJPAContext()

        val combinations = service.findAllCombinations()

        assertThat(combinations, notNullValue())
        assertThat(combinations, not(empty()))
        assertThat(combinations, hasSize(savedCombinations.size))

        val retrievedIds = combinations.map { it.id }
        val savedIdsArray = savedCombinations.map { it.id }.toTypedArray()

        assertThat(retrievedIds, containsInAnyOrder(*savedIdsArray))
    }

    @Test
    fun findByUUIDSuccess() {
        val uuid = UUID.randomUUID().toString()
        val savedCombination = combinationRepository.save(Combination(uuid = uuid)).copy()

        val optional = service.findByUUID(uuid.toString())

        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(true))

        // Hibernate and it's proxies...
        val retrievedCombination = optional.get().copy()

        assertThat(retrievedCombination.uuid, notNullValue())
        assertThat(retrievedCombination.uuid, equalTo(uuid))
        assertThat(retrievedCombination, samePropertyValuesAs(savedCombination))
    }

    @Test
    fun findByUUIDNonExisting() {
        val optional = service.findByUUID(UUID.randomUUID().toString())

        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(false))
    }


    @Test
    fun shouldCreateCombination() {
        val uuid = UUID.randomUUID().toString()
        combinationRepository.deleteAll()

        val savedCombination = service.createCombination(Combination(name = "Super name")).copy()

        Assertions.assertThat(savedCombination).isNotNull()
        Assertions.assertThat(savedCombination.name).isEqualTo("Super name")
        Assertions.assertThat(savedCombination.id).isGreaterThan(0)
        Assertions.assertThat(savedCombination.uuid).isNotBlank()
    }

    fun refreshJPAContext() {
        // Thanks Hibernate and JPA... Great cache you have there, if there would only be a way to turn it off for testing!
        entityManager.flush()
        entityManager.clear()
    }
}
