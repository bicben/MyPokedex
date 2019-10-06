package com.zaripov.mypokedex.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.zaripov.mypokedex.R
import com.zaripov.mypokedex.presentation.view.PokeDetailsView
import com.zaripov.mypokedex.presentation.presenter.PokeDetailsPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.zaripov.mypokedex.databinding.FragmentPokeDetailsBinding
import com.zaripov.mypokedex.model.Pokemon

class PokeDetailsFragment : MvpAppCompatFragment(), PokeDetailsView {

    companion object {
        const val TAG = "PokeDetailsFragment"

        fun newInstance(): PokeDetailsFragment {
            val fragment: PokeDetailsFragment = PokeDetailsFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPokeDetailsPresenter: PokeDetailsPresenter
    lateinit var binding: FragmentPokeDetailsBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokeDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        savedInstanceState ?: run {
            val entry = PokeDetailsFragmentArgs.fromBundle(arguments!!).pokemonEntry
            Log.i(TAG, "entry received: $entry")
            mPokeDetailsPresenter.loadPokemon(entry)
        }

        return binding.root
    }

    override fun displayError(throwable: Throwable) {
        alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.pokedex))
            .setMessage(throwable.toString())
            .setOnCancelListener {mPokeDetailsPresenter.cancelError()}
            .show()
    }

    override fun cancelError() {
        if(::alertDialog.isInitialized && alertDialog.isShowing){
            alertDialog.hide()
        }
    }

    override fun bindPokemon(pokemon: Pokemon) {
        Log.i(TAG, "Binded pokemon: $pokemon")
        binding.pokemon = pokemon
    }
}
