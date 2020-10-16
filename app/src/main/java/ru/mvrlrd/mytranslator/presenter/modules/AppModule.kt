package ru.mvrlrd.mytranslator.presenter.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper
import ru.mvrlrd.mytranslator.presenter.MainViewModel



val appModule = module{
    single { ApiHelper() }
    viewModel { MainViewModel(get())}
}