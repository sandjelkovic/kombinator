package com.sandjelkovic.kombinator.kombinatorkofu

import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.web.jackson
import org.springframework.fu.kofu.web.server

val app = application {
    beans {
    }
    server {
        codecs {
            string()
            jackson()
        }
        router {
            GET("/") { ok().syncBody("Hello world!") }
        }
    }

    import(dataConfig)
}

fun main() {
    app.run()
}
