package com.inmoment.demo.view

import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader;
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.inmoment.demo.R
import com.inmoment.demo.Robot
import com.inmoment.demo.RobotImpl
import com.inmoment.demo.RobotUserInterface
import com.inmoment.demo.presenter.MainActivityPresenterBSearchImpl
import com.inmoment.demo.presenter.MainActiviyPresenterImpl

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

/**
 * The application's main activity.
 *
 * @author kjensen
 * */
class MainActivity : AppCompatActivity(), RobotUserInterface, MainActivityView, LoaderManager.LoaderCallbacks<String>{

    private val SEARCH_ASYNC_TASK_LOADER = 22

    private var isCancelled: Boolean = false

    private lateinit var robot: Robot

    private lateinit var mainActivityPresenter : MainActiviyPresenterImpl
    private lateinit var mainActivityBSearchPresenter : MainActivityPresenterBSearchImpl


    //<editor-fold defaultstate="collapsed" desc="Lifecycle Methods...">
    /**
     * @author kjensen
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        this.robot = RobotImpl(this, this)

        this.mainActivityPresenter = MainActiviyPresenterImpl(this.robot, this, this)
        this.mainActivityBSearchPresenter = MainActivityPresenterBSearchImpl(this.robot, this, this)

        this.setBusy(false)

        search_term_edit_text.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                findDefinition(search_term_edit_text)
                true
            } else {
                false
            }
        };

        getSupportLoaderManager().initLoader(SEARCH_ASYNC_TASK_LOADER, null, this);

    }
    //</editor-fold>

    /**
     * This method instructs the robot to find the definition for a given search term.
     *
     * This method should:
     * - Retrieve a search term term from the [searchTermEditText] text field.
     * - Leverage [robot] to find the definition for the given search term.
     * - Update the [definitionTextView] text view with the term's definition once it is found.
     * - Frequently check the status of [isCancelled] and return immediately if it is 'true'.
     * - Gracefully handle any errors or bad user input; feel free to use dialogs or get creative.
     *
     * You will receive bonus points for any of the following:
     * - Your algorithm is particularly efficient.
     * - You modify the UI in some way to make it more visually appealing.
     * - You leverage some feature of Kotlin that is not available to Java.
     * - You do something extra creative.
     *
     * @author JuanFernandez
     * */
    private fun findDefinition() {

        val wordToFind = search_term_edit_text.text.toString().decapitalize()
        val bundle = Bundle()
        bundle.putString(getString(R.string.bundle_word_to_find), wordToFind)

        val loaderManager = supportLoaderManager
        val searchLoader = loaderManager.getLoader<String>(SEARCH_ASYNC_TASK_LOADER)
        if (searchLoader == null ) {
            loaderManager.initLoader<String>(SEARCH_ASYNC_TASK_LOADER, bundle, this).forceLoad()
        } else {
            loaderManager.restartLoader<String>(SEARCH_ASYNC_TASK_LOADER, bundle, this).forceLoad()
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Additional Methods...">
    /**
     * Invoked when the user taps [findDefinitionButton].
     *
     * @author kjensen
     * */
    fun findDefinition(sender: View) {
        resetMetrics()
        hideKeyBoard()
        setBusy(false)
        try {
            findDefinition()
        } finally {
            setBusy(true)
        }
    }

    /**
     * Invoked when the user taps [cancelButton].
     *
     * @author kjensen
     * */
    fun cancel(sender: View) {
        isCancelled = true
        resetMetrics()
        setBusy(false)
    }

    /**
     * @author kjensen
     * */
    override fun updateUI(pageIndex: Int, termIndex: Int, pagesExamined: Int, termsExamined: Int, currentTerm: String?) {
        Handler(mainLooper).post {
            page_index_text_view.text = pageIndex.toString()
            term_index_text_view.text = termIndex.toString()
            pages_examined_text_view.text = pagesExamined.toString()
            terms_examined_text_view.text = termsExamined.toString()
            current_term_text_view.text = currentTerm
        }
    }

    /**
     * @author kjensen
     * */
    override fun updateBusyText(busyText: String?) {
        Handler(mainLooper).post {
            progress_text_view.text = busyText ?: getText(R.string.working)
        }
    }

    /**
     * Instructs the robot to reset its metrics for the number of pages and terms examined.
     *
     * @author kjensen
     * */
    private fun resetMetrics() {
        robot.resetCounters()
    }

    /**
     * Updates the UI to reflect the given 'busy' status by hiding or showing the progress bar and
     * progress text, as well as enabling/disabling the appropriate buttons and text fields.
     *
     * @author kjensen
     * */
    private fun setBusy(busy: Boolean) {
        Handler(mainLooper).post {
            progress_bar.visibility = if (busy) View.VISIBLE else View.GONE
            progress_text_view.visibility = if (busy) View.VISIBLE else View.GONE
            progress_text_view.isEnabled = !busy
            cancel_button.visibility = if (busy) View.VISIBLE else View.INVISIBLE
            search_term_edit_text.isEnabled = !busy
            newFloatingActionButtonSearch.isEnabled = !busy;
            if (isCancelled && !busy) isCancelled = false
        }
    }

    /**
     * @author JuanFernandez
     * */
    override fun showProgress(isWorking: Boolean) {
        setBusy(isWorking)
    }

    /**
     * Hide the keyboard
     *
     * @author JuanFernandez
     * */
    private fun hideKeyBoard(){
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(search_term_edit_text.windowToken, 0)
    }

    /**
     * Show the keyboard
     *
     * @author JuanFernandez
     * */
    fun showKeyBoard(view : View){
        search_term_edit_text.requestFocus()
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(search_term_edit_text, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * @author JuanFernandez
     * */
    override fun showWordFound(term: String, definition: String) {
        definition_text_view.text = definition
    }

    /**
     * @author JuanFernandez
     * */
    override fun showAlertError() {
        alert("Word Not Found. Please Try Again","Alert") {
            positiveButton("OK") {
                toast("-.O")
                definition_text_view.text = getString(R.string.the_term_s_definition_will_appear_here_when_found)
            }
        }.show()
        search_term_edit_text.error = getString(R.string.error)
    }
    //</editor-fold>

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        return object : AsyncTaskLoader<String>(this) {

            override fun onStartLoading() {
                if (args == null) {
                    return
                }
            }

            override fun loadInBackground(): String? {
                val wordToSearch = args?.getString(getString(R.string.bundle_word_to_find))

                if (wordToSearch.isNullOrEmpty()) {
                    return null
                }

                return mainActivityBSearchPresenter.findDefinitionBinarySearch(wordToSearch!!)

            }
        }
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        mainActivityBSearchPresenter.notifyUIProgress(false)
        if (!data.isNullOrEmpty() && data?.isNotBlank()!! ){
            if(data.equals("Error")){
                mainActivityBSearchPresenter.showAlertError()
            }else{
                mainActivityBSearchPresenter.showWordFound("", data);
            }
        }else{
            mainActivityBSearchPresenter.showAlertError()
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {
    }

}
