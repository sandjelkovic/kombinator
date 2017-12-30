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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CombinationsControllerTest : ControllerTest() {

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
        val combination = combinationRepository.findByName(super.fullCombinationName).orElseThrow { InvalidTestDataException() }

        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/${combination.uuid!!}")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("\$.uuid").exists())
                .andExpect(jsonPath("\$.uuid").value(combination.uuid!!))
                .andExpect(jsonPath("\$.name").value(combination.name))
    }

    @Test
    fun getCombinationNotFound() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/${UUID.randomUUID()}")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun getCombinationInvalidRequestPath() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/invalidUUid")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getCombinationEmptyRequestPath() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations/%20")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getCombinations() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.combinationList").exists())
                .andExpect(jsonPath("$._embedded.combinationList").isArray)
                .andExpect(jsonPath("$._embedded.combinationList").isNotEmpty)
                .andExpect(jsonPath("$..combinationList.length()", jsonListSizeMatcher(5)))
    }

    @Test
    fun getCombinationsEmpty() {
        combinationRepository.deleteAll()
        val contentAsString = mockMvc.perform(
                MockMvcRequestBuilders.get("/combinations")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isEmpty)
    }

}
