package com.inmoment.demo.view

/**
 * Created by manolofernandez on 3/27/18.
 */

interface MainActivityView {
    fun showWordFound(term: String, definition: String)
    fun showProgress(isWorking: Boolean)
    fun showAlertError()
}
