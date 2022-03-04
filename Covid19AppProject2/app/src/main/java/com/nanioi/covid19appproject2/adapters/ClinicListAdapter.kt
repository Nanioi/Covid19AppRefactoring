package com.nanioi.covid19appproject2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.adapters.ClinicViewPagerAdapter.Companion.differ
import com.nanioi.covid19appproject2.databinding.ItemClinicListBinding

class ClinicListAdapter : ListAdapter<ClinicLocationEntity, ClinicListAdapter.ViewHolder>(differ) {

    inner class ViewHolder(
        private val binding: ItemClinicListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ClinicLocationEntity) = with(binding) {
            name.text = item.clinic_name
            address.text = item.address
            weekdayTime.text = item.weekday_operating_time
            saturdayTime.text = item.saturday_operating_time
            holidayTime.text = item.holiday_operating_time
            telePhoneNum.text = item.telephone_num
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemClinicListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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