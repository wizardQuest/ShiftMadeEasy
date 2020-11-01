package com.pq.shiftmadeeasy.adapter.professionselector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.resources.ProfessionsStructure
import kotlinx.android.synthetic.main.item_profession.view.*

class ProfessionSelectorAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var professionList: List<ProfessionsStructure> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProfessionSelectorViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profession, parent, false),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfessionSelectorViewHolder -> {
                holder.bind(professionList[position])
            }
        }
    }

    override fun getItemCount(): Int = professionList.size

    fun submitList(professionList: List<ProfessionsStructure>) {
        this.professionList = professionList
    }

    inner class ProfessionSelectorViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val professionIcon = itemView.professionImageId
        private val professionName: TextView = itemView.professionNameId

        fun bind(profession: ProfessionsStructure) {
            professionIcon?.setImageResource(profession.professionIcon)
            professionName?.text = profession.professionName
            itemView?.setOnClickListener {
                interaction?.onItemSelected(position = adapterPosition)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int)
    }
}