package io.pivotal.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
@Controller
class App {

    @Value("\${spring.security.user.name}")
    private val username: String? = null

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("username", username)
        return "index"
    }
}

fun main(args: Array<String>) {
	runApplication<App>(*args)
}
