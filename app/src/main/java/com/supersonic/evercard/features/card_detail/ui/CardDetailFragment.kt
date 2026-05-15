package com.supersonic.evercard.features.card_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.supersonic.evercard.R

class CardDetailFragment : Fragment() {

    private var cardId: String? = null
    private var cardName: String? = null
    private var cardNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardId = it.getString("card_id")
            cardName = it.getString("card_name")
            cardNumber = it.getString("card_number")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvCardId = view.findViewById<TextView>(R.id.tvCardId)
        val tvCardName = view.findViewById<TextView>(R.id.tvCardName)
        val tvCardNumber = view.findViewById<TextView>(R.id.tvCardNumber)

        tvCardId.text = "ID: $cardId"
        tvCardName.text = "Название: $cardName"
        tvCardNumber.text = "Номер: $cardNumber"
    }

    companion object {
        fun newInstance(cardId: String, cardName: String, cardNumber: String): CardDetailFragment {
            val fragment = CardDetailFragment()
            val args = Bundle()
            args.putString("card_id", cardId)
            args.putString("card_name", cardName)
            args.putString("card_number", cardNumber)
            fragment.arguments = args
            return fragment
        }
    }
}