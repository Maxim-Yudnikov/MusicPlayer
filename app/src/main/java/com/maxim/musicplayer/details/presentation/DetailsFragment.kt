package com.maxim.musicplayer.details.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.maxim.musicplayer.core.sl.ProvideViewModel
import com.maxim.musicplayer.databinding.DialogFragmentDetailsBinding

class DetailsFragment: DialogFragment() {
    private var _binding: DialogFragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailsViewModel


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = (requireActivity() as ProvideViewModel).viewModel(DetailsViewModel::class.java)
        _binding = DialogFragmentDetailsBinding.inflate(layoutInflater)

        viewModel.observe(this) {
            it.show(
                binding.artImageView,
                binding.titleTextView,
                binding.filePathTextView,
                binding.formatTextView,
                binding.bitrateTextView,
                binding.samplingRateTextView,
                binding.sizeTextView,
                binding.lengthTextView,
                binding.albumTextView,
                binding.artistTextView
            )
        }

        viewModel.init(savedInstanceState == null)

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.goBack()
    }
}