package com.nanioi.covid19appproject2.data

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml( name = "response")
data class ClinicResponse (
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
    @PropertyElement(name="addr") var addr: String?,
    @PropertyElement(name="pcrPsblYn") var pcrPsblYn : String?,
    @PropertyElement(name="ratPsblYn") var ratPsblYn : String?,
    @PropertyElement(name="recuClCd") var recuClCd : String?,
    @PropertyElement(name="sidoCdNm") var sidoCdNm: String?,
    @PropertyElement(name="sgguCdNm") var sgguCdNm : String?,
    @PropertyElement(name="XPos") var XPos : String?,
    @PropertyElement(name="YPos") var YPos : String?,
    @PropertyElement(name="yadmNm") var yadmNm : String?,
    @PropertyElement(name="telno") var telno : String?
){
    constructor() : this(null,null,null,null,null,null,null,null,null,null)
}