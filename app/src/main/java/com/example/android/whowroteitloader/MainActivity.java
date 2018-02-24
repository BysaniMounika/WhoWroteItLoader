package com.example.android.whowroteitloader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private EditText mSearchEditText;
    private ImageView mBookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.whowroteitloader.R.layout.activity_main);
        mTitleTextView = (TextView) findViewById(com.example.android.whowroteitloader.R.id.text_view_response_book_title);
        mAuthorTextView = (TextView) findViewById(com.example.android.whowroteitloader.R.id.text_view_response_book_authors);
        mSearchEditText = (EditText) findViewById(com.example.android.whowroteitloader.R.id.edit_text_search);
        mBookImage = (ImageView) findViewById(com.example.android.whowroteitloader.R.id.book_image);
        if(getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    public void searchBook(View view) {
        String queryString = mSearchEditText.getText().toString();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        ConnectivityManager conMan = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
            Bundle queryBundle  = new Bundle();
            queryBundle.putString("queryString",queryString);
            getSupportLoaderManager().restartLoader(0,queryBundle,this);
            mAuthorTextView.setText("");
            mTitleTextView.setText("loading...");
        }

        else {
            if (queryString.length() == 0) {
                mAuthorTextView.setText("");
                mTitleTextView.setText("Please enter a search term");
            } else {
                mAuthorTextView.setText("");
                mTitleTextView.setText("Please check your network connection and try again.");
            }
            mBookImage.setImageBitmap(null);
        }
       ;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, args.getString("queryString"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            JSONObject book = itemsArray.getJSONObject(0);
            JSONObject volumeInfo = book.getJSONObject("volumeInfo");
            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
            String title = null;
            String authors = null;
            String imageUrlString =null;
            try {
                title = volumeInfo.getString("title");
                authors = volumeInfo.getString("authors");
                imageUrlString = imageLinks.getString("thumbnail");
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (title != null && authors != null){
                mTitleTextView.setText("Title : "+title);
                mAuthorTextView.setText("Authors : "+authors);
                Picasso.with(getApplicationContext()).load(imageUrlString).into(mBookImage);
                return;
            }
        } catch (JSONException e) {
            mTitleTextView.setText("No Results Found");
            mAuthorTextView.setText("");
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
