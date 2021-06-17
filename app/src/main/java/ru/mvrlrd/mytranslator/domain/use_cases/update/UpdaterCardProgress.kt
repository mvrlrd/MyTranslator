package ru.mvrlrd.mytranslator.domain.use_cases.update

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class UpdaterCardProgress (private val searchResultRepository: ILocalRepository) :
    UseCase<Int, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Int> {
        val cardId = params[0]
        val newProgress = params[1].toInt()
        return searchResultRepository.updateCardProgress(cardId, newProgress)
    }
}