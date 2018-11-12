package com.sandjelkovic.kombinator.kombinatorkofu

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbc

val dataConfig = configuration {
	beans {
//		bean<CombinationRepository>()
//		bean<SlotRepository>()
//		bean<SlotEntryRepository>()
	}
	listener<ApplicationReadyEvent> {
	}
	r2dbc {

	}
}
