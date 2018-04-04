package com.inmoment.demo

/**
 * A utility interface for updating the UI with metrics held by the [Robot].
 *
 * @author kjensen
 */
interface RobotUserInterface {

    fun updateUI(pageIndex: Int, termIndex: Int, pagesExamined: Int, termsExamined: Int, currentTerm: String? = null)
    fun updateBusyText(busyText: String?)

}