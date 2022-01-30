package otus.homework.flowcats

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class CatsRepository(
    private val catsService: CatsService,
    private val refreshIntervalMs: Long = 5000
) {

    fun listenForCatFacts() = flow {
        while (true) {
            AppResult.createFromSuspend {
                getCatFacts()
            }.let { catFactResult ->
                emit(catFactResult)
            }
        }
    }

    private suspend fun getCatFacts(): Fact {
        delay(refreshIntervalMs)
        // добавляем фейковую ошибку
        val hasError = Random.nextInt(2) % 2 == 0
        if(hasError) throw Throwable("Get cats fact error")
        return catsService.getCatFact()
    }
}