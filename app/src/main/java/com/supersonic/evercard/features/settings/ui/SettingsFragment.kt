package com.supersonic.evercard.features.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.supersonic.evercard.R
import androidx.navigation.fragment.findNavController
import com.supersonic.evercard.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

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

        // Кнопка назад на тулбаре
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Загружаем сохранённую тему и отмечаем выбранную радио-кнопку
        loadSavedTheme()

        // Обработчик выбора темы
        binding.themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLight -> {
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    saveTheme("light")
                }
                R.id.radioDark -> {
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    saveTheme("dark")
                }
                R.id.radioCarnival -> {
                    // TODO: кастомная тема (пока используем тёмную)
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    saveTheme("carnival")
                }
            }
            // Пересоздаём Activity для применения темы
            requireActivity().recreate()
        }
    }

    private fun setTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun saveTheme(theme: String) {
        val prefs = requireContext().getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("app_theme", theme).apply()
    }

    private fun loadSavedTheme() {
        val prefs = requireContext().getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        val savedTheme = prefs.getString("app_theme", "light")

        when (savedTheme) {
            "light" -> binding.radioLight.isChecked = true
            "dark" -> binding.radioDark.isChecked = true
            "carnival" -> binding.radioCarnival.isChecked = true
            else -> binding.radioLight.isChecked = true
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