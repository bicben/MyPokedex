package com.zaripov.mypokedex.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.zaripov.mypokedex.R
import com.zaripov.mypokedex.adapter.PokeAdapter
import com.zaripov.mypokedex.adapter.PokeListClickListener
import com.zaripov.mypokedex.databinding.FragmentPokeListBinding
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.presentation.presenter.PokeListPresenter
import com.zaripov.mypokedex.presentation.view.PokeListView
import com.zaripov.mypokedex.utils.applySchedulers
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PokeListFragment : MvpAppCompatFragment(), PokeListView, PokeListClickListener {
    @InjectPresenter
    lateinit var mPokeListPresenter: PokeListPresenter

    private lateinit var binding: FragmentPokeListBinding
    private lateinit var listAdapter: PokeAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var searchView: SearchView
    private val disposables = CompositeDisposable()
    private var isSearchViewEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        listAdapter = PokeAdapter(this)
        binding = FragmentPokeListBinding.inflate(inflater, container, false)
        binding.rvPokeList.adapter = listAdapter

        return binding.root
    }

    override fun onStartLoading() {
        Timber.i("on start loading")
        binding.rvPokeList.visibility = View.GONE
        binding.probarPokeList.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
    }

    override fun onFinishLoading(emptyList: Boolean) {
        Timber.i("on finish loading")
        binding.rvPokeList.visibility = View.VISIBLE
        binding.probarPokeList.visibility = View.GONE
        if (emptyList) {
            binding.emptyView.visibility = View.VISIBLE
        }
    }

    override fun showError(error: String) {
        onFinishLoading(false)

        alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.pokedex))
            .setMessage(error)
            .setOnCancelListener { mPokeListPresenter.cancelError() }
            .show()
    }

    override fun searchFieldEnabled(enabled: Boolean) {
        searchView.inputType = when (enabled) {
            true -> InputType.TYPE_CLASS_TEXT
            false -> InputType.TYPE_NULL
        }
        isSearchViewEnabled = enabled
    }

    override fun setEntries(entries: PagedList<PokeListEntry>) {
        listAdapter.submitList(entries)
    }

    override fun cancelError() {
        if (::alertDialog.isInitialized && alertDialog.isShowing) {
            alertDialog.hide()
        }
    }

    override fun onClick(entry: Int) {
        Timber.i("clicked on entry: $entry")
        this.findNavController().navigate(
            PokeListFragmentDirections.actionPokeListFragmentToPokeDetailsFragment(entry)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.poke_list_menu, menu)
        searchView = menu.findItem(R.id.search_bar)?.actionView as SearchView
        searchFieldEnabled(false)

        disposables.add(
            searchView.queryTextChanges()
                .skipInitialValue()
                .skip(1)
                .map { it.toString() }
                .map { it.trim() }
                .debounce(250, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap {
                    activity?.runOnUiThread { onStartLoading() }
                    mPokeListPresenter.getEntryList(it)
                }
                .filter { isSearchViewEnabled }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listAdapter.submitList(it)
                    onFinishLoading(it.isEmpty())
                }, {
                    Timber.e(it.toString())
                })
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nuke_pokemons_menu_button -> onNuking(getString(R.string.pokemon)) { mPokeListPresenter.nukePokeDatabase() }
            R.id.nuke_entries_menu_button -> onNuking(getString(R.string.entry)) { mPokeListPresenter.nukeEntryDatabase() }
        }

        return false
    }

    private fun onNuking(type: String, func: () -> Unit) {
        alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.confirmation))
            .setMessage("Are you sure you want to wipe the $type database?")
            .setPositiveButton(getString(R.string.yes)) { dialog, _ -> func(); dialog.dismiss() }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}