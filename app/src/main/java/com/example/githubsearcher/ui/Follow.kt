package com.example.githubsearcher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearcher.databinding.FragmentFollowBinding
import com.example.githubsearcher.viewModel.FollowerViewModel
import com.example.githubsearcher.viewModel.FollowingViewModel
import kotlin.properties.Delegates

class Follow : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private var position by Delegates.notNull<Int>()
    private val followingViewModel by activityViewModels<FollowingViewModel>()
    private val followerViewModel by activityViewModels<FollowerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
        var username = ""
        arguments?.let {
            position = it.getInt(POSITION)
            username = it.getString(USERNAME)!!
        }
        if (position == 1) {
            subscribe1()
        } else {
            subscribe2()
        }
    }

    private fun subscribe1() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHero.layoutManager = layoutManager
        val fragmentAdapter = ApiAdapter()
        binding.rvHero.adapter = fragmentAdapter
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvHero.addItemDecoration(itemDecoration)

        followingViewModel.apiData.observe(viewLifecycleOwner) {
            fragmentAdapter.submitList(it)
        }
        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        followingViewModel.showError.observe(viewLifecycleOwner) {
            binding.sectionLabel.isVisible = it
        }
        followingViewModel.showFailed.observe(viewLifecycleOwner) {
            binding.sectionLabelFailed.isVisible = it
        }
    }

    private fun subscribe2() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHero.layoutManager = layoutManager
        val fragmentAdapter = ApiAdapter()
        binding.rvHero.adapter = fragmentAdapter
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvHero.addItemDecoration(itemDecoration)

        followerViewModel.apiData.observe(viewLifecycleOwner) {
            fragmentAdapter.submitList(it)
        }
        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        followerViewModel.showError.observe(viewLifecycleOwner) {
            binding.difollow.isVisible = it
        }
        followerViewModel.showFailed.observe(viewLifecycleOwner) {
            binding.sectionLabelFailed.isVisible = it
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.memuat.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.memuat.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val POSITION = "position"
        const val USERNAME = "username"
    }
}