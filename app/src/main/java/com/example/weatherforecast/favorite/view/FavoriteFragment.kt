package com.example.weatherforecast.favorite.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFavoriteBinding
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.favorite.viewmodel.FavActivityViewModel
import com.example.weatherforecast.favorite.viewmodel.FavActivityViewModelFactory
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModel
import com.example.weatherforecast.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherforecast.home.view.HomeFragment
import com.example.weatherforecast.map.view.MapsActivity
import com.example.weatherforecast.model.Favorite
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.network.WeatherClient
import com.example.weatherforecast.provider.Language.LanguageProvider
import com.example.weatherforecast.provider.unitsystem.UnitProvider


class FavoriteFragment : Fragment(), OnItemClickListener {

    private val binding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }
    private val mfactory by lazy { FavoriteViewModelFactory(Repository.getInstance(requireContext(), WeatherClient.getInstance()
    ,ConcreteLocalSource(requireContext())), UnitProvider.getInstance(requireContext()), LanguageProvider.getInstance(requireContext()))}
    private val favViewModel by lazy { ViewModelProvider(requireActivity(), mfactory)[FavoriteViewModel::class.java] }

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
    }

    override fun onClick(favorite: Favorite) {
        if(isOnline()){
            var intent = Intent(requireContext(), FavoriteActivity::class.java)
            intent.putExtra("favorite_obj", favorite)
            startActivity(intent)
        } else{
            Toast.makeText(requireContext(), " No Internet Connection ", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDeleteClick(favorite: Favorite) {
        favViewModel.deleteFavorite(favorite)
        Toast.makeText(context, "Deleted Place", Toast.LENGTH_SHORT).show()
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}