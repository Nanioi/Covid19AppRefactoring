package com.nanioi.covid19appproject2.Model.data.regionInfo


import com.google.gson.annotations.SerializedName

data class RegionInfoResponse(
    @SerializedName("documents")
    val documents: List<RegionInfo>?,
    @SerializedName("meta")
    val meta: Meta?
)