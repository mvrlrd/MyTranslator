package ru.mvrlrd.mytranslator.data.data.network


import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.response.ListSearchResult
import ru.mvrlrd.mytranslator.functional.Either

interface DataSource  {
    suspend fun getData(wordForTranslation: String): Either<Failure, ListSearchResult?>
}