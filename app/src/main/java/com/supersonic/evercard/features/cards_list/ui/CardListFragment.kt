package com.supersonic.evercard.features.cards_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.supersonic.evercard.databinding.FragmentCardListBinding
import com.supersonic.evercard.features.root.adapter.CardAdapter
import com.supersonic.evercard.features.root.adapter.CarouselLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardListFragment : Fragment() {

    private val viewModel: CardListViewModel by viewModel()
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
        adapter = CardAdapter(emptyList())
        binding.cardsRecyclerView.adapter = adapter

        // Подписка на данные из ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cards.collectLatest { cards ->
                updateAdapter(cards)
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

    private fun updateAdapter(cards: List<com.supersonic.evercard.features.root.data.DiscountCard>) {
        adapter.updateCards(cards)

        // Устанавливаем начальную позицию в "середину" для бесконечной прокрутки
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