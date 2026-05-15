package com.supersonic.evercard.features.root.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.supersonic.evercard.R
import com.supersonic.evercard.databinding.FragmentMainBinding
import com.supersonic.evercard.features.cards_list.ui.CardListFragment
import com.supersonic.evercard.features.root.adapter.MediaPagerAdapter
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

        // Восстанавливаем сохранённую позицию таба
        savedInstanceState?.getInt("current_tab")?.let {
            viewModel.setCurrentTab(it)
        }

        pagerAdapter = MediaPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
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
            tab.text = pagerAdapter.getTitle(position)
        }.attach()

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        binding.btnAddCard.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addEditCardFragment)
        }

        binding.btnAddFolder.setOnClickListener {
            showCreateFolderDialog()
        }

        // Сохраняем позицию таба при переключении
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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

    private fun showCreateFolderDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_create_folder, null)
        val editText = dialogView.findViewById<TextInputEditText>(R.id.folderNameInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Создать папку")
            .setView(dialogView)
            .setPositiveButton("Создать") { _, _ ->
                val folderName = editText.text.toString().trim()
                if (folderName.isNotEmpty()) {
                    createNewTab(folderName)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun createNewTab(folderName: String) {
        Log.d("MainFragment", "createNewTab called with name: $folderName")
        val currentCount = pagerAdapter.itemCount
        Log.d("MainFragment", "Current count before add: $currentCount")

        pagerAdapter.addFolder(CardListFragment.newInstance(), folderName)

        Log.d("MainFragment", "Count after add: ${pagerAdapter.itemCount}")
        Log.d("MainFragment", "Titles: ${pagerAdapter.getTitles()}")

        binding.viewPager.setCurrentItem(currentCount, true)
    }

    private fun saveFolders() {
        val prefs = requireContext().getSharedPreferences("folders", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("folder_list", pagerAdapter.getTitles().toSet()).apply()
    }

    private fun loadFolders() {
        val prefs = requireContext().getSharedPreferences("folders", Context.MODE_PRIVATE)
        val savedTitles = prefs.getStringSet("folder_list", emptySet()) ?: emptySet()
        savedTitles.forEach { title ->
            pagerAdapter.addFragment(CardListFragment.newInstance(), title)
        }
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

}