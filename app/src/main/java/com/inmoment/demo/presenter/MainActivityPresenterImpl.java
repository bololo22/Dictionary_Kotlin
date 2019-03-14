package com.inmoment.demo.presenter;

import android.content.Context;
import android.util.Log;

import com.inmoment.demo.view.MainActivityView;
import com.inmoment.demo.R;
import com.inmoment.demo.Robot;

/**
 * Created by manolofernandez on 3/27/18.
 */

public class MainActivityPresenterImpl implements MainActivityPresenter {
    private static final String TAG = MainActivityPresenterImpl.class.getName();
    private Robot robot;
    private MainActivityView mainActivityView;
    private Context context;

    public MainActivityPresenterImpl(Robot robot, MainActivityView mainActivityView, Context context){
        this.robot = robot;
        this.mainActivityView = mainActivityView;
        this.context = context;
    }

    @Override
    public void notifyUIProgress(boolean isWorking) {
        mainActivityView.showProgress(isWorking);
    }

    @Override
    public void showAlertError() {
        mainActivityView.showAlertError();
    }

    @Override
    public void showWordFound(String word, String definition) {
        mainActivityView.showWordFound(word, definition);
    }

    @Override
    public String findDefinition(String word) {
        robot.jumpToFirstPage();
        robot.jumpToFirstTerm();

        while (robot.getHasMorePages() || robot.getHasMoreTerms()) {
            Log.d(TAG, "Page " + robot.getCurrentPageIndex());
            if(robot.getCurrentTerm().equalsIgnoreCase(context.getResources().getString(R.string.term_usage))
                    && !robot.getCurrentTermDefinition().startsWith(context.getResources().getString(R.string.start_of_definition_for_usage))){
                robot.moveToNextTerm();
            }

            if (word.compareTo(robot.getCurrentTerm().toLowerCase()) >= 0) {
                Log.d(TAG, "First Term " + robot.getCurrentTerm());
                robot.jumpToLastTerm();

                if(robot.getCurrentTerm().equalsIgnoreCase(context.getResources().getString(R.string.term_usage))
                        && !robot.getCurrentTermDefinition().startsWith(context.getResources().getString(R.string.start_of_definition_for_usage))){
                    robot.moveToPreviousTerm();
                }

                if(word.compareTo(robot.getCurrentTerm().toLowerCase()) < 0) {
                    Log.d(TAG, "Last Term " + robot.getCurrentTerm());
                    robot.jumpToFirstTerm();
                    return findDefinitionInPage(word);
                }if(word.compareTo(robot.getCurrentTerm().toLowerCase()) == 0){
                    //mainActivityView.showWordFound(robot.getCurrentTerm(), robot.getCurrentTermDefinition());
                    String definition = robot.getCurrentTermDefinition();
                    if(robot.getHasMoreTerms()) {
                        robot.moveToNextTerm();
                    }
                    if(robot.getCurrentTerm().equalsIgnoreCase(context.getResources().getString(R.string.term_usage))){
                        return definition + "\n " + robot.getCurrentTerm()
                                + " " + robot.getCurrentTermDefinition();
                    }else {
                        return definition;
                    }
                }else{
                    robot.moveToNextPage();
                    robot.jumpToFirstTerm();
                }
            }else{
                //mainActivityView.showError();
                return context.getResources().getString(R.string.error);
            }
        }
        return "";
    }

    private String findDefinitionInPage(String wordToFind){
        while (robot.getHasMoreTerms()) {
            if(robot.getCurrentTerm().equalsIgnoreCase(context.getResources().getString(R.string.term_usage))
                    && !robot.getCurrentTermDefinition().startsWith(context.getResources().getString(R.string.start_of_definition_for_usage))){
                robot.moveToNextTerm();
            }

            if (wordToFind.compareTo(robot.getCurrentTerm().toLowerCase()) > 0) {
                robot.moveToNextTerm();
            } else if (wordToFind.compareTo(robot.getCurrentTerm().toLowerCase()) < 0) {
                //mainActivityView.showError();
                return context.getResources().getString(R.string.error);
            } else {
                //mainActivityView.showWordFound(robot.getCurrentTerm(), robot.getCurrentTermDefinition());
                String definition = robot.getCurrentTermDefinition();
                robot.moveToNextTerm();
                if(robot.getCurrentTerm().equalsIgnoreCase(context.getResources().getString(R.string.term_usage))){
                    return definition + "\n " + robot.getCurrentTerm()
                            + " " + robot.getCurrentTermDefinition();
                }else {
                    return definition;
                }
            }
        }
        return "";
    }
}
