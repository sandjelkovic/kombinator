package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.config.ExampleDataRunner
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.test.InvalidTestDataException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CombinationsControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var combinationRepository: CombinationRepository

    @Autowired
    lateinit var exampleDataRunner: ExampleDataRunner

    @Before
    fun before() {
        // for now, rely on global test data
        exampleDataRunner.run()
    }

    @Test
    fun getCombination() {
        val computerCombination = combinationRepository.findByName("EPIC Computer").orElseThrow { InvalidTestDataException() }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/${computerCombination.uuid!!}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.uuid").exists())
                .andExpect(jsonPath("\$.uuid").value(computerCombination.uuid!!))
                .andExpect(jsonPath("\$.name").value(computerCombination.name))
    }

    @Test
    fun getCombinationNotFound() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/${UUID.randomUUID()}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }

    @Test
    fun getCombinationInvalidRequestPath() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/invalidUUid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getCombinationEmptyRequestPath() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/%20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

}
