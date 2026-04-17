package com.supersonic.evercard.features.settings.ui

import android.os.Bundle
import android.util.Log
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

        // Наполняем кнопки графикой и текстом
        setupModeButtons()

        // Наблюдаем за текущей темой
        lifecycleScope.launch {
            viewModel.currentTheme.collect { theme ->
                updateThemeSelection(theme)
            }
        }

        // Наблюдаем за режимом отображения
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

    private fun setupModeButtons() {
        // Текст уже есть в layout, устанавливаем только обработчики
        val listCardView = binding.listModeButton.root
        val carouselCardView = binding.carouselModeButton.root

        listCardView.setOnClickListener {
            viewModel.setDisplayMode("list")
            updateModeSelection("list")
        }

        carouselCardView.setOnClickListener {
            viewModel.setDisplayMode("carousel")
            updateModeSelection("carousel")
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
        // Скрываем галочки у обеих кнопок
        binding.listModeButton.checkIcon.visibility = View.GONE
        binding.carouselModeButton.checkIcon.visibility = View.GONE

        // Показываем галочку у выбранной
        when (mode) {
            "list" -> binding.listModeButton.checkIcon.visibility = View.VISIBLE
            "carousel" -> binding.carouselModeButton.checkIcon.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}