package com.inmoment.demo

/**
 * An interface for communicating with a dictionary-searching robot. This interface
 * gives no guarantees as to the location, speed, efficiency, or volatility of the physical
 * or digital robot it represents.
 *
 * @author kjensen / JuanFernandez
 */
interface Robot {

    /**
     * Instructs the robot to turn to the next page of the dictionary.
     * */
    fun moveToNextPage()

    /**
     * Instructs the robot to turn to the previous page of the dictionary.
     * */
    fun moveToPreviousPage()

    /**
     * Instructs the robot to move its camera to the next term on the current page of the
     * dictionary.
     * */
    fun moveToNextTerm()

    /**
     * Instructs the robot to move its camera to the previous term on the current page of the
     * dictionary.
     * */
    fun moveToPreviousTerm()

    /**
     * Instructs the robot to turn to the first page of the dictionary (pageIndex == 0).
     * */
    fun jumpToFirstPage()

    /**
     * Instructs the robot to turn to the last page of the dictionary (pageIndex == unknown).
     * */
    fun jumpToLastPage()

    /**
     * Instructs the robot to move its camera to the first term on the current page of the
     * dictionary (termIndex == 0).
     * */
    fun jumpToFirstTerm()

    /**
     * Instructs the robot to move its camera to the first last on the current page of the
     * dictionary (termIndex == unknown).
     * */
    fun jumpToLastTerm()

    /**
     * Instructs the robot to reset any counters that it may be keeping, such as the number
     * of pages or terms examined.
     * */
    fun resetCounters()

    /**
     * True if there are more pages after the current page of the dictionary.
     * */
    val hasMorePages: Boolean

    /**
     * True if there are more pages before the current page of the dictionary.
     * */
    val hasPreviousPages: Boolean

    /**
     * True if there are more terms after the current term on the current page of the dictionary.
     * */
    val hasMoreTerms: Boolean

    /**
     * True if there are more terms after the current term on the current page of the dictionary.
     * */
    val hasPreviousTerms: Boolean

    /**
     * The index of the current page of the dictionary (0-indexed).
     * */
    val currentPageIndex: Int

    /**
     * The index of the current term on the current page of the dictionary (0-indexed).
     * */
    val currentTermIndex: Int

    /**
     * The text of the current term on the current page of the dictionary, or null if the robot's
     * camera is not over any term.
     * */
    val currentTerm: String?

    /**
     * The text of the definition corresponding to the current term on the current page of the
     * dictionary, or null if the robot's camera is not over any term.
     * */
    val currentTermDefinition: String?

    /**
     * Instructs the robot to jump to specified Page index.
     * */
    fun jumpToSpecificPage(position: Int)

    /**
     * Instructs the robot to jump to specified Term index.
     */
    fun jumpToSpecificTerm(position: Int)

    /**
     * The number of pages for the Dictionary
     * */
    val dictionarySize: Int

    /**
     * The number of terms per page
     * */
    fun getPageSize(position: Int) : Int
}