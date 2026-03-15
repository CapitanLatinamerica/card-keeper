package com.supersonic.evercard.features.root.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.supersonic.evercard.features.cards_list.ui.CardListFragment

class MediaPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CardListFragment.newInstance()
            1 -> CardListFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
