package com.inmoment.demo.presenter

/**
 * Created by manolofernandez on 3/29/18.
 */

interface MainActivityPresenterBSearch {
    fun findDefinitionBinarySearch(word: String): String?
    fun notifyUIProgress(isWorking: Boolean)
    fun showAlertError()
    fun showWordFound(word: String, definition: String)
}
