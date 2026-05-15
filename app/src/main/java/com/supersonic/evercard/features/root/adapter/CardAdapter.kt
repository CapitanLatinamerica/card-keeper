package com.supersonic.evercard.features.root.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.supersonic.evercard.R
import com.supersonic.evercard.domain.model.LoyaltyCard

class CardAdapter(
    private var cards: List<LoyaltyCard>,
    private val onItemClick: (LoyaltyCard) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

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
        holder.title.text = card.shopName

        val backgroundColor = card.color ?: ContextCompat.getColor(
            holder.itemView.context,
            R.color.default_card_color
        )
        holder.cardView.setCardBackgroundColor(backgroundColor)

        holder.itemView.setOnClickListener {
            onItemClick(card)
        }
    }

    override fun getItemCount(): Int = if (cards.isEmpty()) 0 else Int.MAX_VALUE

    fun updateCards(newCards: List<LoyaltyCard>) {
        cards = newCards
        notifyDataSetChanged()
    }
}