package com.zaripov.mypokedex.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
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
import java.util.concurrent.TimeUnit

class PokeListFragment : MvpAppCompatFragment(), PokeListView, PokeListClickListener {
    companion object {

        const val TAG = "PokeListFragment"
        fun newInstance(): PokeListFragment {
            val fragment: PokeListFragment = PokeListFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPokeListPresenter: PokeListPresenter

    private lateinit var binding: FragmentPokeListBinding
    private lateinit var listAdapter: PokeAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var searchView: SearchView
    private val disposables = CompositeDisposable()

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
        Log.i(TAG, "on start loading")
        binding.rvPokeList.visibility = View.GONE
        binding.probarPokeList.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
    }

    override fun onFinishLoading(emptyList: Boolean) {
        Log.i(TAG, "on finish loading")
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
        searchView.inputType = when(enabled){
            true -> InputType.TYPE_CLASS_TEXT
            false -> InputType.TYPE_NULL
        }
    }

    override fun setEntries(entries: List<PokeListEntry>) {
//        disposables.add(listAdapter.submitEntries(entries)
//            .applySchedulers()
//            .subscribe ({
//                onFinishLoading(entries.isEmpty())
//            },{
//                showError(it.toString())
//            })
//        )
        listAdapter.submitList(entries)
        onFinishLoading(entries.isEmpty())
    }

    override fun cancelError() {
        if (::alertDialog.isInitialized && alertDialog.isShowing) {
            alertDialog.hide()
        }
    }

    override fun onClick(entry: Int) {
        Log.i(TAG, "clicked on entry: $entry")
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
            Flowable.create(FlowableOnSubscribe<String> { subscriber ->
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        subscriber.onNext(query!!)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        subscriber.onNext(newText!!)
                        return false
                    }
                })
            }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .map { text -> text.trim() }
                .debounce(200, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mPokeListPresenter.queryEntries(it)
                }, {
                    Log.e(TAG, it.toString())
                })
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nuke_pokemons_menu_button -> mPokeListPresenter.nukePokeDatabase()
            R.id.nuke_entries_menu_button -> mPokeListPresenter.nukeEntryDatabase()
        }

        return false
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}