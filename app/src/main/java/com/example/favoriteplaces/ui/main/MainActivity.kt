package com.example.favoriteplaces.ui.main

import android.os.Bundle
import android.view.Menu
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.ViewAnimator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.favoriteplaces.R
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewAnimator: ViewAnimator by lazy {
        findViewById(R.id.view_animator)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progress_bar)
    }

    private val adapter = PlacePredictionAdapter()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        initRecyclerView()
        viewModel.events.observe(this) { event ->
            when (event) {
                is PlacesSearchEventLoading -> {
                    progressBar.isIndeterminate = true
                }
                is PlacesSearchEventError -> {
                    progressBar.isIndeterminate = false
                    viewAnimator.displayedChild = 0
                }
                is PlacesSearchEventFound -> {
                    progressBar.isIndeterminate = false
                    adapter.setPredictions(event.places)
                    viewAnimator.displayedChild = if (event.places.isEmpty()) 0 else 1
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            queryHint = getString(R.string.search_a_place)
            isIconifiedByDefault = false
            isFocusable = true
            isIconified = false
            requestFocusFromTouch()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.onSearchQueryChanged(newText)
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = linearLayoutManager
            adapter = this@MainActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    linearLayoutManager.orientation
                )
            )
        }
    }
}