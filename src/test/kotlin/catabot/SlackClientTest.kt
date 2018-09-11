package catabot

import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@RelaxedMockK
internal object SlackClientTest {

    private const val CHANNEL = "channel"
    private const val MESSAGE = "message"

    private val slackClient = SlackClient("token")
    private val mockedClient = mockk<Client>()

    @BeforeEach
    fun setUp() {
        FuelManager.instance.client = mockedClient
        every { mockedClient.executeRequest(any()).statusCode } returns 200
    }

    @Test
    fun sendToChannel() {
        // Given
        every { mockedClient.executeRequest(any()).data } returns slackSuccess()

        // Execute
        slackClient.sendToChannel(CHANNEL, MESSAGE)

        // Verify
        verify { mockedClient.executeRequest(any()) }
    }

    @Test
    fun sendToChannel_whenErrorFromSlack_shouldDoSomething() {
        // Given
        every { mockedClient.executeRequest(any()).data } returns slackError()

        // Execute
        slackClient.sendToChannel(CHANNEL, MESSAGE)

        // Verify
        verify { mockedClient.executeRequest(any()) }
    }

    internal fun slackSuccess(): ByteArray {
        return """
        {
            "ok": true,
            "channel": "000000000",
            "ts": "0000000000.000000",
            "message": {
                "text": "text",
                "username": "Slack API Tester",
                "bot_id": "9ABCDEF42",
                "type": "message",
                "subtype": "bot_message",
                "ts": "0000000000.000000"
            }
        }
        """.toByteArray(Charsets.UTF_8)
    }

    internal fun slackError(): ByteArray {
        return """
        {
            "ok": false,
            "message": {
                "text": "I NEED YOUR CLOTHES YOUR BOOTS AND YOUR MOTORCYCLE"
            }
        }
        """.toByteArray(Charsets.UTF_8)
    }
}
