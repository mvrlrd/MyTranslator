package ru.mvrlrd.mytranslator.presenter.modules

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.room.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.room.HistoryDao
import ru.mvrlrd.mytranslator.view.SearchingDialogFragment

//import ru.mvrlrd.mytranslator.room.AppSearchingHistoryDataBase
//import ru.mvrlrd.mytranslator.room.HistoryDao


val appModule = module{
    single { ApiHelper() }
    single { Room.databaseBuilder(androidContext(), AppSearchingHistoryDataBase::class.java, "history_database").fallbackToDestructiveMigration().build() }
    single <HistoryDao> { get<AppSearchingHistoryDataBase>().historyDao() }
    viewModel { MainViewModel(get(),get())}
    single{SearchingDialogFragment()}
}