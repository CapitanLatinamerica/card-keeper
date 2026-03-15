package com.supersonic.evercard.features.cards_list.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.supersonic.evercard.databinding.FragmentCardListBinding
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.supersonic.evercard.features.root.adapter.CardAdapter
import com.supersonic.evercard.features.root.data.DiscountCard
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class CardListFragment : Fragment() {

    private val viewModel: CardListViewModel by viewModel()
    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myCards = listOf(
            DiscountCard("1", "Пятёрочка", Color.parseColor("#E31E24"), null, "123456"),
            DiscountCard("2", "Перекрёсток", Color.parseColor("#00704A"), null, "789012"),
            DiscountCard("3", "Магнит", Color.parseColor("#FF0000"), null, "345678"),
            DiscountCard("4", "Лента", Color.parseColor("#0039A6"), null, "901234")
        )
        val adapter = CardAdapter(myCards)
        binding.cardsViewPager.adapter = adapter

        // Устанавливаем начальную позицию в "середину" бесконечного списка.
        // Выбираем число кратное размеру списка, чтобы открылась именно первая карта
        val middlePosition = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % myCards.size)
        binding.cardsViewPager.setCurrentItem(middlePosition, false)

        // 2. Настройка карусели
        binding.cardsViewPager.offscreenPageLimit = 3
        val transformer = CompositePageTransformer().apply {
            // Используем только положительный отступ или 0
            addTransformer(MarginPageTransformer(20))

            addTransformer { page, position ->
                val r = 1 - Math.abs(position)

                // 1. Масштаб (чтобы боковые карты были чуть уже центральной)
                page.scaleX = 0.85f + r * 0.15f

                // 2. Эффект наслоения (сближаем карты вручную)
                // Чем больше число (например, -150), тем сильнее карты будут заезжать друг под друга
                // position * page.height / 3 — это примерный расчет для нахлеста
                page.translationY = -150 * position

                // 3. Прозрачность
                page.alpha = 0.5f + r * 0.5f
            }
        }
        binding.cardsViewPager.setPageTransformer(transformer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = CardListFragment()
    }
}
