package com.sandjelkovic.kombinator.domain.service

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.persistence.EntityManager
import javax.transaction.Transactional

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
    lateinit var combinationRepository : CombinationRepository
    @Autowired
    lateinit var entityManager : EntityManager

    private val invalidId = 100000L

    @Test
    fun getByInternalIdSuccess() {
        val uuid = UUID.randomUUID()
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

    fun refreshJPAContext() {
        // Thanks Hibernate and JPA... Great cache you have there, if there would only be a way to turn it off for testing!
        entityManager.flush()
        entityManager.clear()
    }
}
