package io.rybalkinsd.kotlinbootcamp.practice.server

import io.rybalkinsd.kotlinbootcamp.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.Queue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

@Controller
@RequestMapping("/chat")
class ChatController {
    val log = logger()
    val messages: Queue<String> = ConcurrentLinkedQueue()
    val usersOnline: MutableMap<String, String> = ConcurrentHashMap()

    @RequestMapping(
        path = ["/login"],
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun login(@RequestParam("name") name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        usersOnline.contains(name) -> ResponseEntity.badRequest().body("Already logged in")
        else -> {
            usersOnline[name] = name
            messages += "[$name] logged in".also { log.info(it) }
            ResponseEntity.ok().build()
        }
    }

    /**
     *
     * Well formatted sorted list of online users
     *
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
        path = ["online"],
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun online(): ResponseEntity<String> =
            ResponseEntity(usersOnline.values.toString(), HttpStatus.OK)
    
    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = ["logout"],
            method = [RequestMethod.DELETE],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun logout(@RequestParam("name") name: String): ResponseEntity<String> = when {
        name.isEmpty() -> ResponseEntity.badRequest().body("Name is too short")
        name.length > 20 -> ResponseEntity.badRequest().body("Name is too long")
        !usersOnline.contains(name) -> ResponseEntity.badRequest().body("No such user")
        else -> {
            usersOnline.remove(name)
            messages += "[$name] logged out".also { log.info(it) }
            ResponseEntity.ok().build()
        }
    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = ["/say"],
            method = [RequestMethod.POST],
            consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun say(@RequestParam("name") name: String, @RequestParam("msg") msg: String): ResponseEntity<String> = when {
        !usersOnline.contains(name) -> ResponseEntity.badRequest().body("User is not logged in")
        else -> {
            messages += "[$name] $msg".also { log.info(it) }
            ResponseEntity.ok().build()
        }
    }

    /**
     * curl -i localhost:8080/chat/history
     */
    @RequestMapping(
            path = ["history"],
            method = [RequestMethod.GET],
            produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun history(): ResponseEntity<String> =
            ResponseEntity(messages.toString(), HttpStatus.OK)
}
