package com.supersonic.evercard.features.root.ui

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.supersonic.evercard.R
import com.supersonic.evercard.databinding.FragmentMainBinding
import com.supersonic.evercard.features.root.adapter.MediaPagerAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val sharedViewModel: MainSharedViewModel by activityViewModels()
    private val viewModel: MainFragmentViewModel by viewModel()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MediaPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        // Восстанавливаем сохранённую позицию таба
        savedInstanceState?.getInt("current_tab")?.let {
            viewModel.setCurrentTab(it)
        }

        pagerAdapter = MediaPagerAdapter(this)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.offscreenPageLimit = 3

        // Отступы под системные бары
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.all_cards)
                1 -> getString(R.string.favorite_cards)
                2 -> getString(R.string.new_folder)
                else -> ""
            }

        }.attach()

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.btnAddCard.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addEditCardFragment)
        }

        // Сохраняем позицию таба при переключении
        viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setCurrentTab(position)
            }
        })
        setupSearch()
    }

    private fun setupSearch() {
        val editText = binding.findEditText
        val clearIcon = binding.clearTextIcon

        fun updateClearButtonVisibility() {
            clearIcon.isVisible = editText.text?.isNotEmpty() == true
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                Log.d("SearchDebug", "Query changed: '$query'")
                sharedViewModel.updateSearchQuery(query)
                updateClearButtonVisibility()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearIcon.setOnClickListener {
            editText.text?.clear()
            sharedViewModel.clearSearch()
            updateClearButtonVisibility()
        }

        updateClearButtonVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.currentTab.value?.let {
            outState.putInt("current_tab", it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

}