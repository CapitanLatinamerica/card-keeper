package com.supersonic.evercard.features.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.supersonic.evercard.R
import com.supersonic.evercard.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : Fragment() {

    private val viewModel: SplashFragmentViewModel by viewModel()
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Наблюдаем за готовностью к переходу
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isReady.collect { isReady ->
                if (isReady) {
                    findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                }
            }
        }

        // Наблюдаем за состоянием загрузки (пока не используем, но можно показать прогресс)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                // Показать/скрыть прогресс-бар
                // binding.progressBar.isVisible = isLoading
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}