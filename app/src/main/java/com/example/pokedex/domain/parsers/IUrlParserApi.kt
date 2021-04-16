package com.example.pokedex.domain.parsers

import android.net.Uri

interface IUrlParserApi {
    fun getLastPath(uriRaw : String) : String?
    fun getLastPath(uri : Uri) : String?
    fun getQueryParameterValue(uriRaw : String, queryParameterName : String) : String?
    fun getQueryParameterValue(uri : Uri, queryParameterName : String) : String?
}