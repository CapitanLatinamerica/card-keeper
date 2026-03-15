package com.supersonic.evercard.features.root.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.supersonic.evercard.R
import com.supersonic.evercard.features.root.data.DiscountCard

class CardAdapter(private val cards: List<DiscountCard>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    // Огромное число для имитации бесконечности
    private val infiniteCount = Int.MAX_VALUE

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: androidx.cardview.widget.CardView = view.findViewById(R.id.cardContainer)
        val title: android.widget.TextView = view.findViewById(R.id.cardTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val realPosition = position % cards.size
        val card = cards[realPosition]

        holder.title.text = card.name
        holder.cardView.setCardBackgroundColor(card.color)

        // Здесь позже добавим отображение логотипа и штрих-кода
    }

    override fun getItemCount(): Int = if (cards.isEmpty()) 0 else infiniteCount
}
