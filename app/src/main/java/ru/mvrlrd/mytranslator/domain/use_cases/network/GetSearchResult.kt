package ru.mvrlrd.mytranslator.domain.use_cases.network

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class GetSearchResult(private val searchResultIRepository: IRepository) :
    UseCase<ListSearchResult?, String>() {

    override suspend fun run(params: String): Either<Failure, ListSearchResult?> {
        return searchResultIRepository.getSearchResult(params)
    }
}

