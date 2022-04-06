package com.example.weatherforecast.favorite.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.databinding.FragmentFavoriteBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModel
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherforecast.map.view.MapsActivity
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient


class FavoriteFragment : Fragment(), OnItemClickListener {

    private val binding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }
    private val factory by lazy { FavoriteViewModelFactory(Repository.getInstance(
        requireContext(), WeatherClient.getInstance(), ConcreteLocalSource(requireContext())
    )) }
    private val favViewModel by lazy { ViewModelProvider(requireActivity(), factory)[FavoriteViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpRecyclerView()

        observeFavorites()

        binding.floatBtn.setOnClickListener {

            val intent = Intent(requireContext(), MapsActivity::class.java)
            intent.putExtra("map_request", "fav")
            startActivity(intent)

        }


        return binding.root
    }

    private fun setUpRecyclerView() = binding.apply {
        favRecycler.layoutManager = LinearLayoutManager(requireContext())
        favRecycler.adapter = FavAdapter(requireContext(),this@FavoriteFragment)
    }

    private fun observeFavorites(){
        favViewModel.getFavorites().observe(viewLifecycleOwner) { fillFavData(it) }

    }
    private fun fillFavData(favorites: List<Favorite>) = binding.apply {
        (favRecycler.adapter as FavAdapter).setData(favorites)
        Log.i("TAG", "fillFavData: ${favorites.size}")
    }

    override fun onClick(favorite: Favorite) {
        //start activity like home and display data by lat&lng
        Log.i("TAG", "onClick: ${favorite.addressLine}")
    }

}