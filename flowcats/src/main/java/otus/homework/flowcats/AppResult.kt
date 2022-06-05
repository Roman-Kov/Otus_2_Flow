package otus.homework.flowcats

import android.util.Log
import java.util.concurrent.CancellationException

sealed class AppResult<out T : Any> {

    object Loading : AppResult<Nothing>()
    object Empty : AppResult<Nothing>()
    data class Failure(val throwable: Throwable) : AppResult<Nothing>()
    data class Success<out T : Any>(val data: T) : AppResult<T>()

    companion object {

        suspend fun <T : Any> createFromSuspend(call: suspend () -> T?): AppResult<T> = try {
            call()?.let {
                Success(it)
            } ?: Empty
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e("Error", e.message ?: "error")
            Failure(e)
        }
    }
}

inline fun <T : Any> AppResult<T>.onSuccess(block: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) block(this.data)
    return this
}

inline fun <T : Any> AppResult<T>.onFailure(block: (Throwable) -> Unit): AppResult<T> {
    if (this is AppResult.Failure) block(this.throwable)
    return this
}