package ru.mvrlrd.mytranslator.domain.use_cases.loaders

import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.domain.ILocalRepository

class GetterAllCatsFlow(val localIRepository: ILocalRepository) {
    fun getAllCatsFlow() =
        localIRepository.getAllCatsFlow()
}