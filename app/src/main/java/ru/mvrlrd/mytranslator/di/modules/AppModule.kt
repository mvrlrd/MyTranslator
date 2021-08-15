package ru.mvrlrd.mytranslator.di.modules

import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.ui.fragments.categories.CategoriesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewCategoryDialog
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordViewModel
import ru.mvrlrd.mytranslator.ui.fragments.learning.LearningViewModel
import ru.mvrlrd.mytranslator.ui.fragments.words.CardsOfCategoryViewModel
import ru.mvrlrd.mytranslator.ui.old.old.favorites.FavoritesViewModel

val appSources = module {
    single { NetworkAvailabilityHandler(androidContext()) }
    single <LocalDataSource> { DbHelper(get()) }
//    single { DbHelper(get()) }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppSearchingHistoryDataBase::class.java,
            "all_db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppSearchingHistoryDataBase>().allDatabasesDao() }
    single { LocalIRepository(get()) }
}

val appViewModules = module {
    viewModel { FavoritesViewModel(get()) }
    viewModel { CategoriesViewModel(get()) }
    viewModel { LearningViewModel(get()) }
    viewModel { CardsOfCategoryViewModel(get()) }
    viewModel { NewWordViewModel(get(),get()) }
}

val appFragments = module {
    single { NewCategoryDialog() }
    single { NewWordDialog() }
    factory { (listener: IconsAdapter.IconAdapterListener) -> IconsAdapter(listener) }
}

val appToolModule = module {
    single { androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
}