package ru.mvrlrd.mytranslator.presenter.modules

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.room.AppSearchingHistoryDataBase

//import ru.mvrlrd.mytranslator.room.AppSearchingHistoryDataBase
//import ru.mvrlrd.mytranslator.room.HistoryDao


val appModule = module{
    single { ApiHelper() }
    viewModel { MainViewModel(get())}
    single { Room.databaseBuilder(androidContext(), AppSearchingHistoryDataBase::class.java, "history_database").build() }
    single { get<AppSearchingHistoryDataBase>().historyDao() }
}