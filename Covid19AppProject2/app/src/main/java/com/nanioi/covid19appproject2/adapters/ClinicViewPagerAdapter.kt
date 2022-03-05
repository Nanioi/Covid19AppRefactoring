package com.nanioi.covid19appproject2.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.databinding.ItemClinicViewpagerBinding

class ClinicViewPagerAdapter(val itemClicked: (ClinicLocationEntity) -> Unit) :
    ListAdapter<ClinicLocationEntity, ClinicViewPagerAdapter.ItemViewHoler>(differ) {

    inner class ItemViewHoler(
        private val binding: ItemClinicViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ClinicLocationEntity) = with(binding) {
            name.text = item.clinic_name
            address.text = item.address
            telePhoneNum.text = item.telephone_num

            binding.root.setOnClickListener {
                itemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHoler {
        return ItemViewHoler(
            ItemClinicViewpagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHoler, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<ClinicLocationEntity>() {
            override fun areItemsTheSame(
                oldItem: ClinicLocationEntity,
                newItem: ClinicLocationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ClinicLocationEntity,
                newItem: ClinicLocationEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}