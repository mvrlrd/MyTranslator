package ru.mvrlrd.mytranslator.domain.use_cases

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.response.SearchResult
import ru.mvrlrd.mytranslator.domain.Repository
import ru.mvrlrd.mytranslator.functional.Either

class GetSearchResult (private val searchResultRepository: Repository):
    UseCase<SearchResult?, String>() {

    override suspend fun run(wordForTranslation: String): Either<Failure, SearchResult?> {
        return searchResultRepository.getSearchResult(wordForTranslation)
    }
}

