package ru.mvrlrd.mytranslator.domain

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.response.SearchResultResponse
import ru.mvrlrd.mytranslator.functional.Either

interface Repository {
    suspend fun getSearchResult(
        wordForTranslation: String
    ): Either<Failure, SearchResultResponse?>


}