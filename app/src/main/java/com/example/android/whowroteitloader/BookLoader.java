package com.example.android.whowroteitloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by admin1 on 16/2/18.
 */

public class BookLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public BookLoader(Context context,String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }
}
