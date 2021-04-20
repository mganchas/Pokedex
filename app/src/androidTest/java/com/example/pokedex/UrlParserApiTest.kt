package com.example.pokedex

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pokedex.domain.parsers.UrlParserApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UrlParserApiTest {
    private val api = UrlParserApi()
    private val dummyUrl = "www.url.pt/"
    private val dummyLastPath = "2"
    private val dummyQueryParameterName = "id"
    private val dummyQueryParameterValue = "5"

    @Test
    fun getLastPath_receives_empty_string_returns_null() {
        Assert.assertNull(api.getLastPath(""))
    }

    @Test
    fun getLastPath_receives_valid_url_string_returns_last_path_value() {
        Assert.assertEquals(dummyLastPath, api.getLastPath("$dummyUrl/$dummyLastPath"))
    }

    @Test
    fun getLastPath_receives_empty_uri_returns_null() {
        Assert.assertNull(api.getLastPath(Uri.EMPTY))
    }

    @Test
    fun getLastPath_receives_valid_uri_returns_last_path_value() {
        val uri = Uri.parse("$dummyUrl/$dummyLastPath")
        Assert.assertEquals(dummyLastPath, api.getLastPath(uri))
    }

    @Test
    fun getQueryParameterValue_receives_empty_string_returns_null() {
        Assert.assertNull(api.getQueryParameterValue("", ""))
    }
}