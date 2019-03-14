package com.inmoment.demo.presenter

import android.content.Context

import com.inmoment.demo.view.MainActivityView
import com.inmoment.demo.R
import com.inmoment.demo.Robot

/**
 * Created by manolofernandez on 3/29/18.
 */

class MainActivityPresenterBSearchImpl(private val robot: Robot, private val mainActivityView: MainActivityView, private val context: Context) : MainActivityPresenterBSearch {

    override fun notifyUIProgress(isWorking: Boolean) {
        mainActivityView.showProgress(isWorking)
    }

    override fun showAlertError() {
        mainActivityView.showAlertError()
    }

    override fun showWordFound(word: String, definition: String) {
        mainActivityView.showWordFound(word, definition)
    }

    override fun findDefinitionBinarySearch(word: String): String? {
        robot.jumpToFirstPage()
        val left = robot.currentPageIndex
        val right = robot.dictionarySize

        findDefinitionBinarySearchInDictionary(left, right, word)

        robot.jumpToFirstTerm()
        val up = robot.currentTermIndex
        val down = robot.getPageSize(robot.currentPageIndex)
        return findDefinitionBinarySearchInPage(up, down, word)
    }

    private fun findDefinitionBinarySearchInDictionary(left: Int, right: Int, word: String) {
        if (right >= left) {
            val mid = left + (right - left) / 2

            robot.jumpToSpecificPage(mid)
            robot.jumpToFirstTerm()

            if (word >= robot.currentTerm!!.toLowerCase()) {
                robot.jumpToLastTerm()
                if (word <= robot.currentTerm!!.toLowerCase()) {
                    return
                }
            }

            if (word < robot.currentTerm!!.toLowerCase()) {
                findDefinitionBinarySearchInDictionary(left, mid - 1, word)
            }

            if (word > robot.currentTerm!!.toLowerCase()) {
                findDefinitionBinarySearchInDictionary(mid + 1, right, word)
            }
        }
    }

    private fun findDefinitionBinarySearchInPage(up: Int, down: Int, word: String): String? {
        if (down >= up) {
            val mid = up + (down - up) / 2

            robot.jumpToSpecificTerm(mid)

            if (word.compareTo(robot.currentTerm!!.toLowerCase()) == 0) {
                val definition = robot.currentTermDefinition
                if (robot.hasMoreTerms) {
                    robot.moveToNextTerm()
                }
                return if (robot.currentTerm!!.equals(context.resources.getString(R.string.term_usage), ignoreCase = true)) {
                    definition + "\n " + robot.currentTerm + " " + robot.currentTermDefinition
                } else {
                    definition
                }
            }

            if (word < robot.currentTerm!!.toLowerCase()) {
                return findDefinitionBinarySearchInPage(up, mid - 1, word)
            }

            if (word > robot.currentTerm!!.toLowerCase()) {
                return findDefinitionBinarySearchInPage(mid + 1, down, word)
            }
        }
        return context.resources.getString(R.string.error)
    }

    companion object {
        private val TAG = MainActivityPresenterBSearchImpl::class.java.name
    }
}
