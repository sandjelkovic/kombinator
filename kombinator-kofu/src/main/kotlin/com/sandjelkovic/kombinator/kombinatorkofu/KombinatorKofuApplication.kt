package com.sandjelkovic.kombinator.kombinatorkofu

import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.web.server

val app = application {
    server {
        router {
            GET("/") { ok().syncBody("Hello world!") }
        }
    }
}

fun main(){
    app.run()
}
