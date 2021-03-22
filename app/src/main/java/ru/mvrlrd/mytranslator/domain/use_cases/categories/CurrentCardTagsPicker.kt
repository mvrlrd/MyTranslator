package ru.mvrlrd.mytranslator.domain.use_cases.categories

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class CurrentCardTagsPicker(private val searchResultRepository: IRepository) :
    UseCase<CardWithTag, Long>() {

    override suspend fun run(params: Long): Either<Failure, CardWithTag> {
        return searchResultRepository.getTagsOfCurrentCard(params)
    }
}