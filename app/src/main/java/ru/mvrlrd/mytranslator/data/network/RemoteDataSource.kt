package ru.mvrlrd.mytranslator.data.network


import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either

interface RemoteDataSource  {
    suspend fun getData(wordForTranslation: String): Either<Failure, ListSearchResult?>
}