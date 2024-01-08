package com.maxim.musicplayer.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.maxim.musicplayer.core.sl.ProvideViewModel

abstract class BaseFragment<B: ViewBinding, V: ViewModel>: Fragment() {
    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected lateinit var viewModel: V
    protected abstract fun viewModelClass(): Class<V>
    protected abstract fun bind(inflater: LayoutInflater, container: ViewGroup?): B
    protected var onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bind(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        viewModel = (activity as ProvideViewModel).viewModel(viewModelClass())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        _binding = null
    }
}