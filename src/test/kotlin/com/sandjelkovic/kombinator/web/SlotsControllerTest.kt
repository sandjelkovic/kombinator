package com.sandjelkovic.kombinator.web

import assertk.assert
import assertk.assertions.isNullOrEmpty
import com.sandjelkovic.kombinator.config.ExampleDataRunner
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SlotsControllerTest : ControllerTest() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var combinationRepository: CombinationRepository

    @Autowired
    lateinit var slotRepository: SlotRepository

    @Autowired
    lateinit var exampleDataRunner: ExampleDataRunner

    @Before
    fun before() {
        // for now, rely on global test data
        exampleDataRunner.run()
    }

    @Test
    fun getSlots() {
        val combination = combinationRepository.findByName(super.fullCombinationName).orElseThrow { InvalidTestDataException() }

        mockMvc.perform(
                get("/combinations/${combination.uuid}/slots")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.slotList").exists())
                .andExpect(jsonPath("$._embedded.slotList").isArray)
                .andExpect(jsonPath("$._embedded.slotList").isNotEmpty)
                .andExpect(jsonPath("$..slotList.length()", jsonListSizeMatcher(2)))
    }

    @Test
    fun getSlotsEmpty() {
        val combination = combinationRepository.findByName("Living room furniture").orElseThrow { InvalidTestDataException() }

        mockMvc.perform(
                get("/combinations/${combination.uuid}/slots")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$._embedded").doesNotExist())
                .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun getSlotsNotExistingCombination() {
        val uuid = UUID.randomUUID()

        mockMvc.perform(
                get("/combinations/$uuid/slots")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
                .andExpect { assert { it.response.contentAsString }.returnedValue { isNullOrEmpty() } }
    }

    @Test
    fun `should createSlots and bind it to the Combination`() {

    }
//
//    @Test
//    fun `should return 404 for non existing Combination`() {
//        val uuid = UUID.randomUUID()
//
//        mockMvc.perform(
//                post("/combinations/$uuid")
//                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound)
//    }
}
