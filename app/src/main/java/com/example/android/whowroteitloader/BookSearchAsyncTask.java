package com.example.android.whowroteitloader;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin1 on 16/2/18.
 */

public class BookSearchAsyncTask extends AsyncTask<String,Void,String> {

    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private ImageView mBookImage;
    private Context mContext;

    public BookSearchAsyncTask(TextView titleTextView, TextView authorTextView,ImageView bookImage,Context context) {
        mTitleTextView = titleTextView;
        mAuthorTextView = authorTextView;
        mBookImage = bookImage;
        mContext = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
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
                Picasso.with(mContext).load(imageUrlString).into(mBookImage);
                return;
            }
        } catch (JSONException e) {
            mTitleTextView.setText("No Results Found");
            mAuthorTextView.setText("");
            e.printStackTrace();
        }
    }
}
