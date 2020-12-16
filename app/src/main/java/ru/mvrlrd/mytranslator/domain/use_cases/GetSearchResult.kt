package ru.mvrlrd.mytranslator.domain.use_cases

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.response.SearchResultResponse
import ru.mvrlrd.mytranslator.domain.Repository
import ru.mvrlrd.mytranslator.functional.Either

class GetSearchResult (private val searchResultRepository: Repository):
    UseCase<SearchResultResponse?, String>() {

    override suspend fun run(wordForTranslation: String): Either<Failure, SearchResultResponse?> {
        return searchResultRepository.getSearchResult(wordForTranslation)
    }
}

