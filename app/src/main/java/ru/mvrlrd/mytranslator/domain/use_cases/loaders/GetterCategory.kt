package ru.mvrlrd.mytranslator.domain.use_cases.loaders

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class GetterCategory(private val local: ILocalRepository) {

    fun run(params: Long):Flow<Category> {
        return local.getCategory(params)
    }
}