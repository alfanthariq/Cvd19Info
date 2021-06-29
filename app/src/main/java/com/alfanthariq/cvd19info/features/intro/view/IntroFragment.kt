package com.alfanthariq.cvd19info.features.intro.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alfanthariq.cvd19info.R
import com.alfanthariq.cvd19info.base.BaseFragment
import com.alfanthariq.cvd19info.databinding.FragmentIntroBinding
import com.alfanthariq.cvd19info.di.ViewModelFactory
import com.alfanthariq.cvd19info.features.intro.viewmodel.IntroViewModel

// TODO: Rename parameter arguments, choose names that match
private const val VIEW_TYPE = "view_type"

class IntroFragment : BaseFragment<IntroViewModel>() {
    private val binding by viewBinding(FragmentIntroBinding::inflate)

    override var viewModel: IntroViewModel
        get() = ViewModelProvider(this, ViewModelFactory{IntroViewModel()})[IntroViewModel::class.java]
        set(value) {}

    private var viewType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewType = it.getInt(VIEW_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (viewType) {
            1 -> {
                binding.apply {
                    imgCenter.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.wearmask))
                    txtTitle.text = getString(R.string.wearmask)
                }

            }
            2 -> {
                binding.apply {
                    imgCenter.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.washhand))
                    txtTitle.text = getString(R.string.handwash)
                }
            }
            3 -> {
                binding.apply {
                    imgCenter.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pydistancing))
                    txtTitle.text = getString(R.string.distanc)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(viewType: Int) =
            IntroFragment().apply {
                arguments = Bundle().apply {
                    putInt(VIEW_TYPE, viewType)
                }
            }
    }
}