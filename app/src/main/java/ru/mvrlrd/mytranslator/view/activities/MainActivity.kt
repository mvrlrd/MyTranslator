package ru.mvrlrd.mytranslator.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.translation_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.ui.recycler.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter
import ru.mvrlrd.mytranslator.view.fragments.TranslationFragment

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()
    var translationFragment: Fragment = TranslationFragment()
    lateinit var callback : ItemTouchHelper.Callback
    lateinit var _adapter : TranslationAdapter
//    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavController()
        getSupportActionBar()?.hide();

        _adapter = TranslationAdapter()

//        supportFragmentManager.beginTransaction()
//            .add(R.id.trFragmentContainerView, translationFragment)
//            .commit()

        mainViewModel.liveTranslations.observe(this, Observer { meanings ->
            handleTranslationList(meanings as MutableList<MeaningModelForRecycler>)
        })




    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    fun onClickGo(view: View) {
        mainViewModel.loadData(searchedWord_TextView.text.toString())
    }

    private fun handleTranslationList(list: MutableList<MeaningModelForRecycler>) {

        translation_recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = _adapter.apply { collection = list  }

        }
        callback = SimpleItemTouchHelperCallback(translation_recyclerview.adapter as TranslationAdapter)
        ItemTouchHelper(callback).attachToRecyclerView(translation_recyclerview)

    }



    private fun initNavController() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
                as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_translator, R.id.navigation_favorites)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


}
