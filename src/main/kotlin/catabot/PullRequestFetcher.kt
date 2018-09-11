package catabot

import io.micronaut.context.annotation.Value
import org.kohsuke.github.GHIssueState
import org.kohsuke.github.GHPullRequest
import org.kohsuke.github.GitHub
import java.net.URL
import java.util.*
import javax.inject.Singleton

@Singleton
class PullRequestFetcher(@Value("\${github.token}") val token: String,
                         @Value("\${github.organization}") val organization: String,
                         @Value("\${github.repositories}") val repositories: List<String>,
                         @Value("\${github.users}") val users: List<String>) {

    fun fetch(): List<PullRequest> {

        val github = GitHub.connectUsingOAuth(token)

        val ghPullRequests = mutableListOf<GHPullRequest>()
        for (repository in repositories) {
            val ghs = github.getOrganization(organization)
                    .getRepository(repository)
                    .getPullRequests(GHIssueState.OPEN)
            ghPullRequests.addAll(ghs)
        }

        return ghPullRequests
                .filter { it.user.login in users }
                .map {
                    PullRequest(
                            name = it.user.name,
                            userLogin = it.user.login,
                            link = it.htmlUrl,
                            title = it.title,
                            labels = it.labels.map { it.name },
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt)
                }
    }
}

data class PullRequest(val name: String,
                       val userLogin: String,
                       val link: URL,
                       val title: String,
                       val labels: List<String>,
                       val createdAt: Date,
                       val updatedAt: Date)
