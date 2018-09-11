package catabot

import javax.inject.Singleton

@Singleton
class PullRequestMessageFormatter {

    fun format(pullRequests: List<PullRequest>): String {
        return pullRequests.joinToString("\n") { "- *${it.name}* => ${it.title}: `${it.link}`" }
    }
}
