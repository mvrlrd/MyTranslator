package ru.mvrlrd.mytranslator.domain.use_cases.update

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class UpdaterAllCategoriesToUnselect(private val localIRepository: ILocalRepository) :
    UseCase<Int, Unit>() {

    override suspend fun run(params:Unit): Either<Failure, Int> {
        return localIRepository.unselectAllCategories(unselected = false , selected = true)
    }
}