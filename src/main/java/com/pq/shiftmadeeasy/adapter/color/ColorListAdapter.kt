package com.pq.shiftmadeeasy.adapter.color

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pq.shiftmadeeasy.R
import kotlinx.android.synthetic.main.color_layout.view.*

class ColorListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var colorList: List<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.color_layout, parent, false),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ColorViewHolder -> {
                holder.bind(colorList[position])
            }
        }
    }

    override fun getItemCount(): Int = colorList.size

    fun submitList(list: ArrayList<Int>) {
        colorList = list
        notifyDataSetChanged()
    }

    class ColorViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val image = itemView.colorId?.background?.mutate() as? GradientDrawable

        fun bind(color: Int) {
            image?.setColor(color)
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, color)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Int)
    }
}