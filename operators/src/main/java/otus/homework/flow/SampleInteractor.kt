package otus.homework.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class SampleInteractor(
    private val sampleRepository: SampleRepository
) {

    /**
     * Реализуйте функцию task1 которая последовательно:
     * 1) возводит числа в 5ую степень
     * 2) убирает чила <= 20
     * 3) убирает четные числа
     * 4) добавляет постфикс "won"
     * 5) берет 3 первых числа
     * 6) возвращает результат
     */
    fun task1(): Flow<String> =
        sampleRepository.produceNumbers()
            .map { number ->
                val multiplier = 5
                number * multiplier
            }.filter { number ->
                number > 20 && number % 2 != 0
            }.map { number ->
                "$number won"
            }.take(3)

    /**
     * Классическая задача FizzBuzz с небольшим изменением.
     * Если входное число делится на 3 - эмитим само число и после него эмитим строку Fizz
     * Если входное число делится на 5 - эмитим само число и после него эмитим строку Buzz
     * Если входное число делится на 15 - эмитим само число и после него эмитим строку FizzBuzz
     * Если число не делится на 3,5,15 - эмитим само число
     */
    fun task2(): Flow<String> =
        sampleRepository.produceNumbers().transform { number ->
            when {
                number % 15 == 0 -> {
                    emit(number.toString())
                    emit("FizzBuzz")
                }
                number % 5 == 0 -> {
                    emit(number.toString())
                    emit("Buzz")
                }
                number % 3 == 0 -> {
                    emit(number.toString())
                    emit("Fizz")
                }
                else -> emit(number.toString())
            }
        }

    /**
     * Реализуйте функцию task3, которая объединяет эмиты из двух flow и возвращает кортеж Pair<String,String>(f1,f2),
     * где f1 айтем из первого флоу, f2 айтем из второго флоу.
     * Если айтемы в одно из флоу кончились то результирующий флоу также должен закончится
     */
    fun task3(): Flow<Pair<String, String>> {
        val colors = sampleRepository.produceColors()
        val forms = sampleRepository.produceForms()
        return colors.zip(forms) { color, form ->
            Pair(color, form)
        }
    }

    /**
     * Реализайте функцию task4, которая обрабатывает IllegalArgumentException и в качестве фоллбека
     * эмитит число -1.
     * Если тип эксепшена != IllegalArgumentException, пробросьте его дальше
     * При любом исходе, будь то выброс исключения или успешная отработка функции вызовите метод dotsRepository.completed()
     */
    fun task4(): Flow<Int> =
        sampleRepository.produceNumbers()
            .catch { e ->
                if (e is IllegalArgumentException) emit(-1)
                else throw e
            }.onCompletion {
                sampleRepository.completed()
            }
}