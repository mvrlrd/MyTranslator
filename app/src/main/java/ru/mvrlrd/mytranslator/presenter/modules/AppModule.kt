package ru.mvrlrd.mytranslator.presenter.modules

import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter
import ru.mvrlrd.mytranslator.view.fragments.SearchingDialogFragment
import ru.mvrlrd.mytranslator.view.fragments.TranslationFragment
import ru.mvrlrd.mytranslator.view.fragments.translation.OnSwipeListener

val appModule = module {
//    single { RecipesAdapter() }
//    factory { TranslationAdapter(TranslationFragment() as OnSwipeListener)}
//    factory { TranslationAdapter()}
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
    viewModel { MainViewModel(get(), get()) }
    single { SearchingDialogFragment() }

}