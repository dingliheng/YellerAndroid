package edu.utaustin.yusun.yellerandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.data.ListItem;
import edu.utaustin.yusun.yellerandroid.function_activities.CommentDialog;

/**
 * Created by yusun on 11/25/15.
 */
public abstract class PullToRefreshListViewAdapter extends android.widget.BaseAdapter {
    private ArrayList<ListItem> items = new ArrayList<ListItem>();;
    private Context mContext;
    private View rowView;
    public PullToRefreshListViewAdapter(Context context, ArrayList<ListItem> items) {
        mContext = context;
        this.items = items;
        TypefaceProvider.registerDefaultIconSets();
    }
    public class ViewHolder {
        public String id;
        public TextView name;
        public ImageView profilePic;
        public BootstrapThumbnail feedImageView;
        public TextView timestamp;
        public TextView statusMsg;
    }


    /**
     * Loads the data.
     */
    public void loadData() {

        // Here add your code to load the data for example from a webservice or DB

        // MANDATORY: Notify that the data has changed
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Show one list item
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        rowView = convertView;

        ListItem record = (ListItem) getItem(position);

        final LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null){
            rowView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.name = (TextView) rowView.findViewById(R.id.name);
            viewHolder.timestamp = (TextView) rowView.findViewById(R.id.timestamp);
            viewHolder.statusMsg = (TextView) rowView.findViewById(R.id.txtStatusMsg);
            viewHolder.profilePic = (ImageView) rowView.findViewById(R.id.profilePic);
            viewHolder.feedImageView = (BootstrapThumbnail) rowView.findViewById(R.id.feedImage1);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        final String feed_id = record.getYeller_id();
        holder.name.setText(record.getName());
        holder.timestamp.setText(record.getTimeStamp()); // need to modify the time to the elapse time
        holder.statusMsg.setText(record.getStatus());

        Picasso.with(mContext).load(record.getProfilePic()).into(holder.profilePic);
        Picasso.with(mContext).load(record.getImage()).into(holder.feedImageView);

        if (convertView == null) {

            final LinearLayout reply = (LinearLayout) rowView.findViewById(R.id.reply);

            String comment_url = "http://socialyeller.appspot.com/android_comment";
//            final ArrayList<String> authors = new ArrayList<String>();
//            final ArrayList<String> comments = new ArrayList<String>();
            RequestParams params = new RequestParams();
            params.put("yeller_id", feed_id);
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.get(comment_url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject jObject = new JSONObject(new String(responseBody));
                        JSONArray authors_json = jObject.getJSONArray("authors");
                        JSONArray comments_json = jObject.getJSONArray("comments");
                        for (int i = 0; i < authors_json.length(); i++) {
                            inflater.inflate(R.layout.comment, reply, true);
                            TextView comment = (TextView) reply.getChildAt(i).findViewById(R.id.comment_content);
                            TextView comment_owner = (TextView) reply.getChildAt(i).findViewById(R.id.comment_owner);
                            comment_owner.setText(authors_json.getString(i));
                            comment.setText(comments_json.getString(i));
                        }
                    } catch (JSONException j) {
                        System.out.println("JSON Error");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }

        rowView.findViewById(R.id.comment_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog commentDialog = new CommentDialog(v.getContext(), feed_id, PullToRefreshListViewAdapter.this);
                commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                commentDialog.show();
            }
        });
        return rowView;
    }

}
