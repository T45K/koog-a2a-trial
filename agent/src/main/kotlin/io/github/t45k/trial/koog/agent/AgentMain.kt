package io.github.t45k.trial.koog.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.singleRunStrategy
import ai.koog.agents.core.tools.Tool
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

suspend fun main() {
    val geminiApiKey = System.getenv("GEMINI_API_KEY")
    val aiAgent: AIAgent<String, String> = AIAgent(
        simpleGoogleAIExecutor(geminiApiKey),
        GoogleModels.Gemini2_5Flash,
        singleRunStrategy(),
        ToolRegistry {
            tool(
                GoogleSearchTool(
                    apiKey = System.getenv("GOOGLE_CUSTOM_SEARCH_API_KEY"),
                    cx = System.getenv("CX"),
                )
            )
        },
    )

    val result = aiAgent.run(
        """
            Kotlinのバックエンドエンジニアを募集している企業の求人のURLを教えて。年収700万以上で。100件くらい検索して、良さそうなものを上位10件で選んで
        """.trimIndent()
    )
    println(result)
}

@Serializable
data class GoogleSearchArgs(val query: String, val num: Int = 5)

class GoogleSearchTool(
    private val apiEndpoint: String = "https://www.googleapis.com/customsearch/v1",
    private val apiKey: String,
    private val cx: String
) : Tool<GoogleSearchArgs, String>() {
    override val name: String = "googleSearch"
    override val description: String = "Execute web search and return a summary of top results. { query: string, num?: number }"

    override val argsSerializer: KSerializer<GoogleSearchArgs> = GoogleSearchArgs.serializer()
    override val resultSerializer: KSerializer<String> = String.serializer()

    private val client = HttpClient(CIO)

    override suspend fun execute(args: GoogleSearchArgs): String {
        val response: HttpResponse = client.get(apiEndpoint) {
            parameter("key", apiKey)
            parameter("cx", cx)
            parameter("q", args.query)
            parameter("num", args.num)
        }
        return response.bodyAsText().also { println(it) }
    }
}


/*
A2Aプロトコルを紹介する簡単な題材を作っています。
以下の要求を満たすように、プログラムを修正してください
修正対象はServerMainとClientMain

## ServerMain
- ServerMainは2つのSkillを持つ
  - 1. Greetings: 挨拶が入力されたとき、適切な挨拶を返すだけのSkill
  - 2. 天気検索: 天気を検索して返すSkill
- Serverへの入力が挨拶だった場合、Greetings Skillを実行する
- Greetings Skillでは、AiAgentにそのまま入力を渡して、結果を返す。
- これはeventProcessor.sendMessageをそのまま作る
- AiAgentの実装はAgentMainを参照する
- Serverへの入力が天気に関する話題なら、天気検索Skillを実行する
- これはeventProcessor.sendTaskEventで処理する
- 天気検索では、日付と地域名がない場合、ユーザーにそれを含めるように返答する
- 含まれていた場合、検索前に日付と地域名で検索します、という返答を返してから、検索する
- 検索にはGoogleSearchToolを用いる
- https://weathernews.jpを使うように指定する
- Serverへの入力がいずれかに当てはまらない場合、どちらかを指定するように返答する

## ClientMain
以下のリクエストを送る
- 最初に挨拶をドイツ語で送る。レスポンスを確認し、Greetings Skillが利用され、ドイツ語であいさつが返ってきたことを確認する
- 「今日の天気は何ですか」と送る。天気検索Skillが利用されて、地域を指定するように言われることを確認する
- 「今日の大阪の天気は何ですか」と送る。天気検索Skillが利用されて、天気が返ってきたことを確認する
- 今日の晩御飯の献立について訊く。あいさつか天気について訊くように言われたことを確認する
 */
