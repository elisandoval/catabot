package catabot

import com.github.kittinunf.fuel.httpPost
import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Singleton
class SlackClient(@Value("\${slack.token}") val token: String) {

    fun sendToChannel(channel: String, message: String) {
        "https://slack.com/api/chat.postMessage"
                .httpPost(parameters = listOf(
                        "token" to token,
                        "channel" to channel,
                        "text" to message)).response()
    }
}
