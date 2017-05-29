package hu.gearxpert.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by melinda.kostenszki on 2017.05.28..
 */

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder> {

    /***** Creating OnItemClickListener *****/
    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Define viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTextView;
        TextView titleTextView;
        TextView authorTextView;
        TextView publishedTextView;

        public ViewHolder(final View itemView) {
            super(itemView);

            sectionTextView = (TextView) itemView.findViewById(R.id.section);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            authorTextView = (TextView) itemView.findViewById(R.id.author);
            publishedTextView = (TextView) itemView.findViewById(R.id.published);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    private List<NewsData> mNewsInfos;
    private Context mContext;

    /**
     * Constructs a new {@link NewsDataAdapter}.
     *
     * @param context of the app
     * @param news   is the list of news, which is the data source of the adapter
     */
    public NewsDataAdapter(Context context, List<NewsData> news) {
        mNewsInfos = news;
        mContext = context;
    }

    // Inflating a layout from XML and returning the holder
    @Override
    public NewsDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newsListItemView = inflater.inflate(R.layout.news_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(newsListItemView);

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(NewsDataAdapter.ViewHolder viewHolder, int position) {
        // Find the news at the given position in the list of news
        NewsData currentNews = mNewsInfos.get(position);

        String convertedDateTime ="";
        String originalDateTime = currentNews.getWebPublicationDate();
        String substringDateTime = originalDateTime.substring(0, 16);
        String convertedDateTime1 = substringDateTime.replace("-", ".");
        convertedDateTime = convertedDateTime1.replace("T", ", ");

        viewHolder.sectionTextView.setText(currentNews.getSectionName());
        viewHolder.titleTextView.setText(currentNews.getTitle());
        viewHolder.authorTextView.setText(currentNews.getAuthor());
        viewHolder.publishedTextView.setText(convertedDateTime);



    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mNewsInfos.size();
    }

    // Helper method to set the actual news list into the recyclerview on the activity
    public void setNewsInfoList(List<NewsData> newsList) {
        mNewsInfos = newsList;
    }

}
