package ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog


import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.network.ApiHelper

import ru.mvrlrd.mytranslator.domain.use_cases.tags.TagsLoader
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "AddNewCategoryViewModel"

class AddNewCategoryViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val categoriesLoader: TagsLoader = TagsLoader(searchResultRepository)

}
