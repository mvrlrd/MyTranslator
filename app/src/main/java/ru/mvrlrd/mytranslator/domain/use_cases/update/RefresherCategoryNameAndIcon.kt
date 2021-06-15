package ru.mvrlrd.mytranslator.domain.use_cases.update

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RefresherCategoryNameAndIcon (private val localIRepository: ILocalRepository) :
    UseCase<Int, Array<String>>() {

    override suspend fun run(params: Array<String>): Either<Failure, Int> {
        val cardId = params[0].toLong()
        val newName = params[1]
        val newIcon = params[2]
        return localIRepository.updateCategory(cardId, newName, newIcon)
    }
}