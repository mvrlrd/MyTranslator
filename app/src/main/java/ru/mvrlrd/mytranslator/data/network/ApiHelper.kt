package ru.mvrlrd.mytranslator.data.network

import retrofit2.Response
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either

class ApiHelper(
    private val apiService: ISkyengApiService,
    private val networkHandler: NetworkAvailabilityHandler
) :
    RemoteDataSource {

    override suspend fun getData(wordForTranslation: String): Either<Failure, ListSearchResult?> =
        requestData { apiService.search(wordForTranslation) }

    private fun <T> responseHandle(response: Response<T>): Either<Failure, T?> =
        when (response.isSuccessful) {
            true -> Either.Right(response.body())
            false -> Either.Left(Failure.ServerError)
        }

    private suspend fun <T> requestData(request: suspend () -> Response<T>) =
        when (networkHandler.isConnected()) {
            true -> {
                try {
                    responseHandle(request())
                } catch (exception: Throwable) {
                    Either.Left(Failure.ServerError)
                }
            }
            false -> Either.Left(Failure.NetworkConnection)
        }
}