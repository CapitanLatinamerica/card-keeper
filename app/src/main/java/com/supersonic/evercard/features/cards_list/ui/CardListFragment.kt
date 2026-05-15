package com.supersonic.evercard.features.cards_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.supersonic.evercard.R
import androidx.navigation.fragment.findNavController
import com.supersonic.evercard.databinding.FragmentCardListBinding
import com.supersonic.evercard.features.root.adapter.CardAdapter
import com.supersonic.evercard.features.root.adapter.CarouselLayoutManager
import com.supersonic.evercard.features.root.data.DiscountCard
import com.supersonic.evercard.features.root.ui.MainSharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardListFragment : Fragment() {

    private val viewModel: CardListViewModel by viewModel()
    private val sharedViewModel: MainSharedViewModel by activityViewModels()
    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CardAdapter
    private lateinit var layoutManager: CarouselLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка кастомного LayoutManager для карусели
        layoutManager = CarouselLayoutManager(requireContext())
        binding.cardsRecyclerView.layoutManager = layoutManager

        // Адаптер с пустым списком
        adapter = CardAdapter(emptyList()) { }
        binding.cardsRecyclerView.adapter = adapter

        // Подписка на данные из ViewModel с учётом поискового запроса
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.allCards,
                    sharedViewModel.searchQuery
                ) { allCards, query ->
                    if (query.isBlank()) {
                        allCards
                    } else {
                        allCards.filter { card ->
                            card.name.contains(query, ignoreCase = true)
                        }
                    }
                }.collect { filteredCards ->
                    updateAdapter(filteredCards)
                }
            }
        }

        // Подписка на состояние загрузки
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }

        // Подписка на ошибки
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                binding.errorText.isVisible = error != null
                binding.errorText.text = error
            }
        }
    }

    private fun updateAdapter(cards: List<DiscountCard>) {
        adapter = CardAdapter(cards) { clickedCard ->
            // Переход на детальный экран с передачей ID карты
            val bundle = Bundle().apply {
                putString("card_id", clickedCard.id)
                putString("card_name", clickedCard.name)
                putString("card_number", clickedCard.barcode)
            }
            findNavController().navigate(R.id.action_cardListFragment_to_cardDetailFragment, bundle)
        }
        binding.cardsRecyclerView.adapter = adapter

        if (cards.isNotEmpty() && binding.cardsRecyclerView.adapter == adapter) {
            val middlePosition = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % cards.size)
            binding.cardsRecyclerView.scrollToPosition(middlePosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = CardListFragment()
    }
}