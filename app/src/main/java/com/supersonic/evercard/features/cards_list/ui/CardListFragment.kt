package com.supersonic.evercard.features.cards_list.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.supersonic.evercard.databinding.FragmentCardListBinding
import com.supersonic.evercard.features.root.adapter.CardAdapter
import com.supersonic.evercard.features.root.data.DiscountCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardListFragment : Fragment() {

    private val viewModel: CardListViewModel by viewModel()
    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CardAdapter



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

        // Создаём адаптер (пока без данных)
        adapter = CardAdapter(emptyList())
        binding.cardsViewPager.adapter = adapter

        // Настройка карусели
        binding.cardsViewPager.offscreenPageLimit = 3
        binding.cardsViewPager.setPageTransformer { page, position ->
            val absPos = Math.abs(position)
            val r = 1 - Math.min(1f, absPos)

            page.scaleX = 0.8f + (r * 0.2f)
            val minScaleY = 0.25f
            page.scaleY = minScaleY + (r * (1f - minScaleY))
            page.translationY = -page.height * 0.75f * position
            page.alpha = 0.6f + (r * 0.4f)
            page.translationZ = if (absPos < 0.5) 10f else 0f
        }

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
                if (error != null) {
                    binding.errorText.text = error
                    binding.errorText.isVisible = true
                } else {
                    binding.errorText.isVisible = false
                }
            }
        }
    }

    private fun updateAdapter(cards: List<com.supersonic.evercard.features.root.data.DiscountCard>) {
        adapter = CardAdapter(cards)
        binding.cardsViewPager.adapter = adapter

        // Устанавливаем начальную позицию в "середину" бесконечного списка
        if (cards.isNotEmpty()) {
            val middlePosition = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % cards.size)
            binding.cardsViewPager.setCurrentItem(middlePosition, false)
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
