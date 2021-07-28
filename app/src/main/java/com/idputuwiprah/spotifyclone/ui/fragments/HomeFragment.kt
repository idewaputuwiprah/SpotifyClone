package com.idputuwiprah.spotifyclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.idputuwiprah.spotifyclone.R
import com.idputuwiprah.spotifyclone.adapters.SongAdapter
import com.idputuwiprah.spotifyclone.databinding.FragmentHomeBinding
import com.idputuwiprah.spotifyclone.other.Status
import com.idputuwiprah.spotifyclone.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    @Inject
    lateinit var songAdapter: SongAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()

        songAdapter.setOnItemClickListener { song->
            mainViewModel.playOrToggleSong(song)
        }
    }

    private fun setupRecyclerView() = fragmentHomeBinding.rvAllSongs.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result->
            when(result.status) {
                Status.SUCCESS -> {
                    fragmentHomeBinding.allSongsProgressBar.isVisible = false
                    result.data?.let { songs->
                        songAdapter.songs = songs
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> fragmentHomeBinding.allSongsProgressBar.isVisible = true
            }
        }
    }
}