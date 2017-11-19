package com.sandjelkovic.kombinator.config

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.model.SlotEntry
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotEntryRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import org.springframework.boot.CommandLineRunner
import java.math.BigDecimal
import java.util.*

/**
 * @author sandjelkovic
 * @date 19.11.17.
 */

class ExampleDataRunner(val combinationRepository: CombinationRepository,
                        val slotRepository: SlotRepository,
                        val slotEntryRepository: SlotEntryRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (!isExampleDataAlreadyCreated(combinationRepository)) {

            val computer = combinationRepository.save(Combination(uuid = UUID.randomUUID(), name = "EPIC Computer", slots = listOf()))

            combinationRepository.saveAll(mutableListOf(
                    computer,
                    Combination(uuid = UUID.randomUUID(), name = "Shopping priority", slots = listOf()),
                    Combination(uuid = UUID.randomUUID(), name = "Summer travel priorities", slots = listOf()),
                    Combination(uuid = UUID.randomUUID(), name = "Living room furniture", slots = listOf()),
                    Combination(uuid = UUID.randomUUID(), name = "Random list of something", slots = listOf()
                    )))

            val cpuSlot = slotRepository.save(Slot(name = "CPU", configuration = computer))
            val gpuSlot = slotRepository.save(Slot(name = "GPU", configuration = computer))

            slotEntryRepository.save(SlotEntry(name = "Intel i5-4000", data = "Middle range CPU", url = "https://localhost/buyMeAn-i5", position = 0, value = BigDecimal.valueOf(200), slot = cpuSlot, selected = false))
            slotEntryRepository.save(SlotEntry(name = "Intel i7-4000", data = "High range CPU", url = "https://localhost/buyMeAn-i7", position = 2, value = BigDecimal.valueOf(400), slot = cpuSlot, selected = false))
            slotEntryRepository.save(SlotEntry(name = "AMD Ryzen 7", data = "AMD is back with new High End CPU", url = "https://localhost/getMeARyzen7", position = 1, value = BigDecimal.valueOf(350), slot = cpuSlot, selected = true))

            slotEntryRepository.save(SlotEntry(name = "RX390", data = "AMD GPU", url = "https://localhost/rx390", position = 0, value = BigDecimal.valueOf(400), slot = gpuSlot, selected = false))
            slotEntryRepository.save(SlotEntry(name = "Gigabyte GTX 1070", data = "1070. 3 Coolers!!!", url = "https://localhost/gigabyte1070", position = 1, value = BigDecimal.valueOf(500), slot = gpuSlot, selected = true))
            slotEntryRepository.save(SlotEntry(name = "Asus GTX 1070", data = "1070. Water cooling", url = "https://localhost/asus1070", position = 2, value = BigDecimal.valueOf(550), slot = gpuSlot, selected = false))
        }
    }

    private fun isExampleDataAlreadyCreated(combinationRepository: CombinationRepository) =
            combinationRepository
                    .findAll()
                    .mapNotNull { it.name }
                    .contains("EPIC Computer")

}
