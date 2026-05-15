package com.supersonic.evercard.features.root.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.supersonic.evercard.features.cards_list.ui.CardListFragment

/**
 * Адаптер для ViewPager2 с поддержкой динамического добавления табов (папок)
 */
class MediaPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    // Списки фрагментов и заголовков
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    init {
        // Табы по умолчанию
        addDefaultTab("Все карты")
        addDefaultTab("Избранные")
    }

    private fun addDefaultTab(title: String) {
        fragments.add(CardListFragment.newInstance())
        titles.add(title)
    }

    /**
     * Добавляет новую папку (таб) в конец списка
     */
    fun addFolder(folderName: String) {
        fragments.add(CardListFragment.newInstance())
        titles.add(folderName)
        notifyItemInserted(fragments.size - 1)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getTitle(position: Int): String = titles[position]

    fun getTitles(): List<String> = titles.toList()
}