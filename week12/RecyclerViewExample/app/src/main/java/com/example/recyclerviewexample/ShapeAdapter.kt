package com.example.recyclerviewexample

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewexample.databinding.ItemShapeBinding
import com.example.recyclerviewexample.item.ShapeItem
import kotlin.random.Random

class ShapeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ShapeItem>()
    var count = 1

    inner class ShapeViewHolder(private val binding: ItemShapeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShapeItem) {
            with(binding.tvCount) {
                text = item.count.toString()
                backgroundTintList = ColorStateList.valueOf(item.color)

                when (item.type) {
                    ShapeItem.Type.CIRCLE -> setBackgroundResource(R.drawable.bg_circle)
                    ShapeItem.Type.SQUARE -> setBackgroundResource(R.drawable.bg_square)
                    ShapeItem.Type.TRIANGLE -> setBackgroundResource(R.drawable.bg_triangle)
                    ShapeItem.Type.STAR -> setBackgroundResource(R.drawable.bg_star)
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ShapeViewHolder(
            ItemShapeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShapeViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    private fun notifyItem(newItems: MutableList<ShapeItem>) {
        update(newItems)
        items.clear()
        items.addAll(newItems)
    }

    fun addItem() {
        val newItems = createNewItems()

        repeat(count) {
            val randomIndex = (Random.nextDouble() * items.size).toInt()
            newItems.add(randomIndex, ShapeItem(count = (items.size + 1)))
        }

        notifyItem(newItems)
    }

    fun removeItem() {
        val newItems = createNewItems()

        if (items.size >= count)
            repeat(count) {
                val randomIndex = (Random.nextDouble() * newItems.size).toInt()
                newItems.removeAt(randomIndex)
            }

        notifyItem(newItems)
    }

    fun shuffleItem() {
        val newItems = createNewItems()
        newItems.shuffle()

        notifyItem(newItems)
    }

    private fun createNewItems(): MutableList<ShapeItem> {
        val newItems = mutableListOf<ShapeItem>()
        newItems.addAll(items)
        return newItems
    }

    fun incrementCount() = if (count < items.size) count++ else null
    fun decrementCount() = if (count > 1) count-- else null

    private fun update(newItems: MutableList<ShapeItem>) {
        val diffUtilCallback = DiffUtilCallback(items, newItems)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class DiffUtilCallback(
        private var oldItems: MutableList<ShapeItem>,
        private var newItems: MutableList<ShapeItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].hashCode() == newItems[newItemPosition].hashCode()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
    }

}