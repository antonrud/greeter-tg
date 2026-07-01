import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel
import dev.inmo.kslog.common.defaultMessageFormatter
import dev.inmo.kslog.common.e
import dev.inmo.kslog.common.i
import dev.inmo.kslog.common.setDefaultKSLog
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onChatMemberJoined
import dev.inmo.tgbotapi.types.LinkPreviewOptions
import dev.inmo.tgbotapi.utils.PreviewFeature


private val log = KSLog("greeter-tg")

fun loadGreeting(): String =
    object {}.javaClass.getResourceAsStream("/greeting.txt")!!.bufferedReader().readText().trim()

@OptIn(PreviewFeature::class)
suspend fun main(args: Array<String>) {
    val greeting = loadGreeting()
    val token = args.first()
    val isDebug = args.any { it == "debug" }

    if (isDebug) {
        setDefaultKSLog(
            KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
                println(defaultMessageFormatter(level, tag, message, throwable))
            }
        )
    }

    val bot = telegramBot(token)

    log.i { "Application started" }

    bot.buildBehaviourWithLongPolling {
        onChatMemberJoined {
            runCatching {

                val name = it.member.username?.username ?: it.member.firstName
                log.i { "New member joined chat ${it.chat.id}: $name" }

                send(
                    chatId = it.chat.id,
                    text = greeting.replace("{username}", name),
                    linkPreviewOptions = LinkPreviewOptions.Disabled
                )
            }.onFailure { ex ->
                log.e(ex) {
                    "Failed to greet new member in chat ${it.chat.id}"
                }
            }
        }
    }.join()
}
