package com.example.news

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
data class News(
    @Element(name = "channel")
    val channel: RssChannel,
)

@Xml(name = "channel")
data class RssChannel(
    @Element(name = "item")
    val items: List<NewsItem>? = null,
)

@Xml(name = "item")
data class NewsItem(
    @PropertyElement(name = "title")
    val title: String? = null,

    @PropertyElement(name = "guid")
    val link: String? = null,
)