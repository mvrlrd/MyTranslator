package ru.mvrlrd.mytranslator.domain.use_cases.update

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class UpdaterCategoryIsChecked(private val localIRepository: ILocalRepository) :
    UseCase<Int, Array<String>>() {

    override suspend fun run(params: Array<String>): Either<Failure, Int> {
        val cardId = params[0].toLong()
        val newProgress = params[1]
        return localIRepository.updateCategoryIsChecked(cardId, newProgress=="true")
    }
}