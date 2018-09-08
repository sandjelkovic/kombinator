package com.sandjelkovic.kombinator.domain.service

import arrow.core.Either
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
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

        val option = service.getCombinationByInternalId(savedCombination.id!!)

        assertThat(option).isNotNull
        assertThat(option.isDefined()).isTrue()

        // Hibernate and it's proxies...
        val retrievedCombination = option.get().copy()

        assertThat(retrievedCombination.uuid)
                .isNotNull()
                .isEqualTo(uuid)

        assertThat(retrievedCombination).isEqualToComparingFieldByFieldRecursively(savedCombination)
    }

    @Test
    fun getByInternalIdNonExisting() {
        val option = service.getCombinationByInternalId(invalidId)

        assertThat(option).isNotNull
        assertThat(option.isEmpty()).isTrue()

    }

    @Test
    fun findAllCombinations() {
        val savedCombinations = (100..105)
                .map { Combination(id = it.toLong()) }
                .map { combinationRepository.save(it) }

        refreshJPAContext()

        val combinations = service.findAllCombinations()

        assertThat(combinations)
                .isNotNull
                .isNotEmpty
                .hasSize(savedCombinations.size)

        val retrievedIds = combinations.map { it.id }
        val savedIdsArray = savedCombinations.map { it.id }.toTypedArray()

        assertThat(retrievedIds).contains(*savedIdsArray)
    }

    @Test
    fun findByUUIDSuccess() {
        val uuid = UUID.randomUUID().toString()
        val savedCombination = combinationRepository.save(Combination(uuid = uuid)).copy()

        val option = service.findByUUID(uuid)

        assertThat(option).isNotNull
        assertThat(option.isDefined()).isTrue()

        // Hibernate and it's proxies...
        val retrievedCombination = option.get().copy()

        assertThat(retrievedCombination.uuid)
                .isNotNull()
                .isEqualTo(uuid)

        assertThat(retrievedCombination).isEqualToComparingFieldByFieldRecursively(savedCombination)
    }

    @Test
    fun findByUUIDNonExisting() {
        val option = service.findByUUID(UUID.randomUUID().toString())

        assertThat(option).isNotNull
        assertThat(option.isEmpty()).isTrue()

    }


    @Test
    fun shouldCreateCombination() {
        val uuid = UUID.randomUUID().toString()
        combinationRepository.deleteAll()

        val either = service.createCombination(Combination(name = "Super name"))

        assertThat(either).isNotNull()
        assertThat(either is Either.Right).isTrue()
        val rightEither = either as Either.Right
        assertThat(rightEither.b.name).isEqualTo("Super name")
        assertThat(rightEither.b.id).isGreaterThan(0)
        assertThat(rightEither.b.uuid).isNotBlank()
    }

    fun refreshJPAContext() {
        // Thanks Hibernate and JPA... Great cache you have there, if there would only be a way to turn it off for testing!
        entityManager.flush()
        entityManager.clear()
    }
}
