package com.example.pokedex.domain.parsers

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokedex.domain.parsers.UrlParserApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UrlParserApiTest
{
    private val api = UrlParserApi()
    private val dummyUrl = "www.url.pt"
    private val dummyLastPath = "2"
    private val dummyQueryParameterName = "id"
    private val otherDummyQueryParameterName = "age"
    private val dummyQueryParameterValue = "5"

    @Test
    fun getLastPath_receivesEmptyString_returnsNull() {
        Assert.assertNull(api.getLastPath(""))
    }

    @Test
    fun getLastPath_receivesValidUrlString_returnsLastPathValue() {
        Assert.assertEquals(dummyLastPath, api.getLastPath("$dummyUrl/$dummyLastPath"))
    }

    @Test
    fun getLastPath_receivesEmptyUri_returnsNull() {
        Assert.assertNull(api.getLastPath(Uri.EMPTY))
    }

    @Test
    fun getLastPath_receivesValidUri_returnsLastPathValue() {
        val uri = Uri.parse("$dummyUrl/$dummyLastPath")
        Assert.assertEquals(dummyLastPath, api.getLastPath(uri))
    }

    @Test
    fun getQueryParameterValue_receivesEmptyString_returnsNull() {
        Assert.assertNull(api.getQueryParameterValue("", ""))
    }

    @Test
    fun getQueryParameterValue_receivesValidUrlStringAndQueryParameter_returnsMatchingQueryParameterValue() {
        val url = "$dummyUrl?$dummyQueryParameterName=$dummyQueryParameterValue"
        Assert.assertEquals(dummyQueryParameterValue, api.getQueryParameterValue(url, dummyQueryParameterName))
    }

    @Test
    fun getQueryParameterValue_receivesValidUrlStringButInvalidQueryParameter_returnsNull() {
        val url = "$dummyUrl?$otherDummyQueryParameterName=$dummyQueryParameterValue"
        Assert.assertNull(api.getQueryParameterValue(url, dummyQueryParameterName))
    }

    @Test
    fun getQueryParameterValue_receivesEmptyUri_returnsNull() {
        Assert.assertNull(api.getQueryParameterValue(Uri.EMPTY, otherDummyQueryParameterName))
    }

    @Test
    fun getQueryParameterValue_receivesValidUri_returnsMatchingQueryParameterValue() {
        val uri = Uri.parse("$dummyUrl?$otherDummyQueryParameterName=$dummyQueryParameterValue")
        Assert.assertEquals(dummyQueryParameterValue, api.getQueryParameterValue(uri, otherDummyQueryParameterName))
    }

    @Test
    fun getQueryParameterValue_receivesValidUriButInvalidQueryParameter_returnsNull() {
        val uri = Uri.parse("$dummyUrl?$otherDummyQueryParameterName=$dummyQueryParameterValue")
        Assert.assertNull(api.getQueryParameterValue(uri, dummyQueryParameterName))
    }
}