package com.nanioi.covid19appproject2.data

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml( name = "response")
data class InfectionStatus (
    @Element
    val header: Header,
    @Element
    val body: Body,
)
@Xml(name = "header")
data class Header(
    @PropertyElement
    val resultCode: Int,
    @PropertyElement
    val resultMsg: String,
)
@Xml(name = "body")
data class Body(
    @Element
    val items: Items,
    @PropertyElement
    val numOfRows: Int,
    @PropertyElement
    val pageNo: Int,
    @PropertyElement
    val totalCount: Int,
)

@Xml
data class Items(
    @Element(name = "item")
    val item: List<Item>
)

@Xml
data class Item(
    @PropertyElement(name="decideCnt") var decideCnt: String?,
    @PropertyElement(name="deathCnt") var deathCnt : String?,
    @PropertyElement(name="stateDt") var stateDt : String?,
    @PropertyElement(name="stateTime") var stateTime : String?
){
    constructor() : this(null,null,null,null)
}