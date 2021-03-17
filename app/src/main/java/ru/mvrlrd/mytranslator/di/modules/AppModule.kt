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
import ru.mvrlrd.mytranslator.ui.fragments.categories.CategoriesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.AddNewCategoryViewModel
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.AddingCategoryFragment
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.categories.recycler.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogFragment
import ru.mvrlrd.mytranslator.ui.fragments.favorites.FavoritesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogViewModel

val appModule = module {
    single { NetworkAvailabilityHandler(androidContext()) }
    single { DbHelper(get()) }
}

val appModule2 = module {
    factory { (listener: IconsAdapter.IconAdapterListener)-> IconsAdapter(listener) }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppSearchingHistoryDataBase::class.java,
            "history_database"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppSearchingHistoryDataBase>().historyDao() }
    single { TranslationViewModel(get(), get()) }
    single { TagDialogViewModel(get(), get()) }
    single { SearchResultIRepository(get(),get()) }
    viewModel {
        FavoritesViewModel(
            get(), get()
        )
    }
    single{
        CategoriesAdapter()
    }
//    single { IconsAdapter(get()) }

    single { AddingCategoryFragment() }
    viewModel { AddNewCategoryViewModel(get(),get()) }

    viewModel { CategoriesViewModel(get(),get()) }


    single { androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    single { TagDialogFragment() }

}