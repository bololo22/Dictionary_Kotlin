package com.inmoment.demo

import android.content.Context

/**
 * An in-memory implementation of [Robot]. This class loads a dictionary of terms from a string
 * array packaged with the Android application, and adds artificial wait times to simulate the
 * activity of a physical robot.
 *
 * @author kjensen / JuanFernandez
 */
class RobotImpl(private val context: Context, private val ui: RobotUserInterface) : Robot {

    companion object {
        const val PAGE_SIZE = 100
    }
    override var currentPageIndex: Int = -1
    override var currentTermIndex: Int = -1
    private var pagesExamined: Int = 0
    private var termsExamined: Int = 0

    
    private val dictionary: List<List<Pair<String, String>>> by lazy {
        ui.updateBusyText(context.getString(R.string.loading_dictionary_into_robot))
        context.resources.getStringArray(R.array.dictionary)
                .map { it.split("|") }
                .filter { it.size >= 2 }
                .map { Pair(it[0].trim(), it[1].trim()) }
                .chunked(PAGE_SIZE)
    }

    override fun moveToNextPage() {
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, currentTermIndex, currentPageIndex + 1))
        Thread.sleep(100)
        currentPageIndex++
        pagesExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun moveToPreviousPage() {
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, currentTermIndex, currentPageIndex - 1))
        Thread.sleep(100)
        currentPageIndex--
        pagesExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun moveToNextTerm() {
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, currentTermIndex + 1, currentPageIndex))
        Thread.sleep(10)
        currentTermIndex++
        termsExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun moveToPreviousTerm() {
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, currentTermIndex - 1, currentPageIndex))
        Thread.sleep(10)
        currentTermIndex--
        termsExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun jumpToFirstPage() {
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, currentTermIndex, 0))
        Thread.sleep(1000)
        currentPageIndex = 0
        pagesExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun jumpToLastPage() {
        val newPageIndex = dictionary.lastIndex
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, currentTermIndex, newPageIndex))
        Thread.sleep(1000)
        currentPageIndex = newPageIndex
        pagesExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun jumpToFirstTerm() {
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, 0, currentPageIndex))
        Thread.sleep(500)
        currentTermIndex = 0
        termsExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun jumpToLastTerm() {
        val newTermIndex = dictionary.getOrNull(currentPageIndex)?.lastIndex ?: -1
        ui.updateBusyText(context.getString(R.string.moving_to_term_on_page, newTermIndex, currentPageIndex))
        Thread.sleep(500)
        currentTermIndex = newTermIndex
        termsExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun resetCounters() {
        pagesExamined = 0
        termsExamined = 0
    }

    override val currentTerm: String?
        get() = dictionary.getOrNull(currentPageIndex)?.getOrNull(currentTermIndex)?.first

    override val currentTermDefinition: String?
        get() = dictionary.getOrNull(currentPageIndex)?.getOrNull(currentTermIndex)?.second

    override val hasMorePages: Boolean
        get() = dictionary.indices.contains(currentPageIndex + 1)

    override val hasMoreTerms: Boolean
        get() = dictionary.getOrNull(currentPageIndex)?.indices?.contains(currentTermIndex + 1) ?: false

    override val hasPreviousPages: Boolean
        get() = dictionary.indices.contains(currentPageIndex - 1)

    override val hasPreviousTerms: Boolean
        get() = dictionary.getOrNull(currentPageIndex)?.indices?.contains(currentTermIndex - 1) ?: false

    override fun jumpToSpecificPage(position: Int){
        currentPageIndex = position
        pagesExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override fun jumpToSpecificTerm(position: Int){
        currentTermIndex = position;
        pagesExamined++
        ui.updateUI(currentPageIndex, currentTermIndex, pagesExamined, termsExamined, currentTerm)
    }

    override val dictionarySize: Int
        get() = dictionary.size - 1

    override fun getPageSize(position: Int): Int {
        return dictionary[position].size
    }
}