package com.supersonic.evercard.features.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.supersonic.evercard.R
import com.supersonic.evercard.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Кнопка назад
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Наблюдаем за текущей темой
        lifecycleScope.launch {
            viewModel.currentTheme.collect { theme ->
                updateThemeSelection(theme)
            }
        }

        // Наблюдаем за режимом отображения (для будущих кнопок)
        lifecycleScope.launch {
            viewModel.displayMode.collect { mode ->
                updateModeSelection(mode)
            }
        }

        // Обработчик выбора темы
        binding.themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val theme = when (checkedId) {
                R.id.radioLight -> "light"
                R.id.radioDark -> "dark"
                R.id.radioCarnival -> "carnival"
                else -> "light"
            }
            viewModel.setTheme(theme)
            requireActivity().recreate() // Пересоздаём Activity для применения темы
        }
    }

    private fun updateThemeSelection(theme: String) {
        when (theme) {
            "light" -> binding.radioLight.isChecked = true
            "dark" -> binding.radioDark.isChecked = true
            "carnival" -> binding.radioCarnival.isChecked = true
        }
    }

    private fun updateModeSelection(mode: String) {
        // TODO: Обновить UI кнопок "Список" и "Карусель"
        // binding.listModeButton.isSelected = mode == "list"
        // binding.carouselModeButton.isSelected = mode == "carousel"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}