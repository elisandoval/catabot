package catabot

import io.micronaut.context.annotation.Value
import io.micronaut.function.FunctionBean
import mu.KotlinLogging
import java.util.function.Supplier

@FunctionBean("listOpenPullRequests")
class ListOpenPullRequestsFunction(val fetcher: PullRequestFetcher,
                                   val messageFormatter: PullRequestMessageFormatter,
                                   val slackClient: SlackClient,
                                   @Value("\${slack.channel}") val channel: String) : Supplier<Unit> {

    private val log = KotlinLogging.logger {}

    override fun get() {
        val pullRequests = fetcher.fetch()
        val formattedMessage = messageFormatter.format(pullRequests)
        log.info("{}", formattedMessage)
        slackClient.sendToChannel(channel, formattedMessage)
    }
}
