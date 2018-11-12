package com.sandjelkovic.kombinator.domain.service

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.test.isNone
import com.sandjelkovic.kombinator.test.isRight
import com.sandjelkovic.kombinator.test.isSome
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import strikt.api.expectThat
import strikt.assertions.*
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

    companion object {
        private const val invalidId = 100000L
    }

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
        val randomUUID = UUID.randomUUID().toString()
        val savedCombination = combinationRepository.save(Combination(uuid = randomUUID)).copy()

        val option = service.getCombinationByInternalId(savedCombination.id!!)

        expectThat(option).isSome {
            get { uuid }.isNotNull().isEqualTo(randomUUID)
            // Hibernate and it's proxies...
            get { copy() }.propertiesAreEqualTo(savedCombination)
        }
    }

    @Test
    fun getByInternalIdNonExisting() {
        val option = service.getCombinationByInternalId(invalidId)

        expectThat(option).isNone()
    }

    @Test
    fun findAllCombinations() {
        val savedCombinations = (100..105)
            .map { Combination(id = it.toLong()) }
            .map { combinationRepository.save(it) }

        refreshJPAContext()

        val combinations = service.findAllCombinations()

        expectThat(combinations)
            .isNotEmpty()
            .hasSize(savedCombinations.size)

        val retrievedIds = combinations.map { it.id!! }
        val savedIds = savedCombinations.map { it.id!! }

        expectThat(retrievedIds).containsExactly(savedIds)
    }

    @Test
    fun findByUUIDSuccess() {
        val randomUUID = UUID.randomUUID().toString()
        val savedCombination = combinationRepository.save(Combination(uuid = randomUUID)).copy()

        val option = service.findByUUID(randomUUID)

        expectThat(option).isSome {
            get { uuid }.isNotNull().isEqualTo(randomUUID)
            // Hibernate and it's proxies...
            get { copy() }.propertiesAreEqualTo(savedCombination)
        }
    }

    @Test
    fun findByUUIDNonExisting() {
        val option = service.findByUUID(UUID.randomUUID().toString())

        expectThat(option).isNone()
    }


    @Test
    fun shouldCreateCombination() {
        combinationRepository.deleteAll()

        val either = service.createCombination(Combination(name = "Super name"))

        // Strikt
        expectThat(either).isRight {
            get { name }.isEqualTo("Super name")
            get { id }.isNotNull().isGreaterThan(0L)
            get { uuid }.isNotNull().isNotBlank()
        }
    }

    fun refreshJPAContext() {
        // Thanks Hibernate and JPA... Great cache you have there, if there would only be a way to turn it off for testing!
        entityManager.flush()
        entityManager.clear()
    }
}
