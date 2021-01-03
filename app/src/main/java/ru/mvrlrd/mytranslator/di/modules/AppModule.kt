package ru.mvrlrd.mytranslator.di.modules

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.vm.TranslationViewModel
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase

val appModule = module {
    single { NetworkAvailabilityHandler(androidContext()) }
}

val appModule2 = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppSearchingHistoryDataBase::class.java,
            "history_database"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppSearchingHistoryDataBase>().historyDao() }
    viewModel { TranslationViewModel(get(), get()) }


}