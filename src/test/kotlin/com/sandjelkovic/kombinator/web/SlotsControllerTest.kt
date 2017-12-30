package com.sandjelkovic.kombinator.web

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

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
                MockMvcRequestBuilders.get("/combinations/${combination.uuid}/slots")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.slotList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.slotList").isArray)
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.slotList").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$..slotList.length()", jsonListSizeMatcher(2)))
    }
}
