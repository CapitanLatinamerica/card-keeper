package com.supersonic.evercard.features.cards_list.ui

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.supersonic.evercard.features.cards_list.ui.adapters.CardListAdapter

class CardListFragment : Fragment(){

    private lateinit var adapter: CardListAdapter
    private lateinit var navController: NavController

    companion object {
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }
}