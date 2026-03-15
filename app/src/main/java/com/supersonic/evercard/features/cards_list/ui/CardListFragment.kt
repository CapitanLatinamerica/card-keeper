package com.supersonic.evercard.features.cards_list.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.supersonic.evercard.databinding.FragmentCardListBinding
import com.supersonic.evercard.features.root.adapter.CardAdapter
import com.supersonic.evercard.features.root.data.DiscountCard
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        binding.cardsViewPager.setPageTransformer { page, position ->
            val absPos = Math.abs(position)
            val r = 1 - Math.min(1f, absPos) // Ограничиваем, чтобы r не уходил в минус

            // Если r=1 (центр), то scaleX = 1.0 (полная ширина)
            // Если r=0 (сосед), то scaleX = 0.8 (сужена)
            page.scaleX = 0.8f + (r * 0.2f)

            // Высота: схлопываем боковые до состояния "полоски"
            val minScaleY = 0.25f
            page.scaleY = minScaleY + (r * (1f - minScaleY))

            // Наслоение: чем сильнее схлопнута карта по Y, тем ближе её надо подтянуть
            // Попробуйте увеличить 0.65f до 0.75f, если дыры между картами всё еще большие
            page.translationY = -page.height * 0.75f * position

            page.alpha = 0.6f + (r * 0.4f)

            // Важно для перекрытия: центральная карта выше всех
            page.translationZ = if (absPos < 0.5) 10f else 0f
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
