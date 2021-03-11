package ru.mvrlrd.mytranslator.di.modules

import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.ui.fragments.translation.TranslationViewModel
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogFragment
import ru.mvrlrd.mytranslator.ui.fragments.favorites.FavoritesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogViewModel

val appModule = module {
    single { NetworkAvailabilityHandler(androidContext()) }
    single { DbHelper(get()) }
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
    single { TranslationViewModel(get(), get(), get()) }
    single { TagDialogViewModel(get()) }
    single { SearchResultIRepository(get(),get()) }
    viewModel {
        FavoritesViewModel(
            get(), get(), get()
        )
    }
    single { androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    single { TagDialogFragment() }

}