package com.supersonic.evercard.features.root.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.supersonic.evercard.features.cards_list.ui.CardListFragment

class MediaPagerAdapter(
    fragment: Fragment,
    private val fragments: MutableList<Fragment> = mutableListOf(),
    private val titles: MutableList<String> = mutableListOf()
) : FragmentStateAdapter(fragment) {

    init {
        // Инициализация с табами по умолчанию
        fragments.add(CardListFragment.newInstance())
        titles.add("Все карты")

        fragments.add(CardListFragment.newInstance())
        titles.add("Избранные")
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getTitle(position: Int): String {
        return titles[position]
    }

    fun addFolder(fragment: Fragment, title: String) {

        fragments.add(fragment)
        titles.add(title)
        notifyItemInserted(fragments.size - 1)
    }

    fun getTitles(): List<String> = titles.toList()

}
