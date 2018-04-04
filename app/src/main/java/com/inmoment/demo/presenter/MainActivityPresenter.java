package com.inmoment.demo.presenter;

/**
 * Created by manolofernandez on 3/27/18.
 */

public interface MainActivityPresenter {
    void notifyUIProgress(boolean isWorking);
    String findDefinition(String word);
    void showAlertError();
    void showWordFound(String word, String definition);
}
