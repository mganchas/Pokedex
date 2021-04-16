package com.example.pokedex.domain.parsers

import android.net.Uri
import android.util.Log
import javax.inject.Inject

class UrlParserApi @Inject constructor() : IUrlParserApi
{
    companion object {
        private val TAG = UrlParserApi::class.java.simpleName
    }

    override fun getLastPath(uriRaw: String): String? {
        Log.d(TAG, "getLastPath() uriRaw: $uriRaw")
        val uri = Uri.parse(uriRaw)
        return getLastPath(uri)
    }

    override fun getLastPath(uri: Uri): String? {
        Log.d(TAG, "getLastPath() uriRaw: $uri")
        return uri.lastPathSegment
    }

    override fun getQueryParameterValue(uriRaw: String, queryParameterName: String) : String? {
        Log.d(TAG, "getQueryParameterValue() uriRaw: $uriRaw | queryParameterName: $queryParameterName")
        val uri = Uri.parse(uriRaw)
        return getQueryParameterValue(uri, queryParameterName)
    }

    override fun getQueryParameterValue(uri: Uri, queryParameterName: String) : String? {
        Log.d(TAG, "getQueryParameterValue() uri: $uri | queryParameterName: $queryParameterName")
        return uri.getQueryParameter(queryParameterName)
    }
}

