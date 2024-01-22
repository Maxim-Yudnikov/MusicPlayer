package com.maxim.musicplayer.trackMore.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maxim.musicplayer.core.presentation.ShowError
import com.maxim.musicplayer.core.sl.ProvideViewModel
import com.maxim.musicplayer.databinding.BottomFragmentMoreBinding

class MoreBottomFragment : BottomSheetDialogFragment(), ShowError {
    private var _binding: BottomFragmentMoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomFragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (requireActivity() as ProvideViewModel).viewModel(MoreViewModel::class.java)

        binding.favoriteButton.setOnClickListener {
            binding.favoriteButton.isEnabled = false
            viewModel.saveToFavorites(this)
            if (viewModel.fromFavorite())
                dismiss()
        }

        binding.playNextButton.setOnClickListener {
            viewModel.playNext()
            dismiss()
        }

        binding.detailsButton.setOnClickListener {
            viewModel.details()
        }

        viewModel.observe(this) {
            it.show(
                binding.artImageView,
                binding.titleTextView,
                binding.descriptionTextView,
                binding.favoriteButton
            )
        }

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}