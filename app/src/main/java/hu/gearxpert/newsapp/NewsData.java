package hu.gearxpert.newsapp;

/**
 * Created by melinda.kostenszki on 2017.05.28..
 */

public class NewsData {
    private String mWebTitle;
    private String mSectionName;
    private String mAuthor;
    private String mWebPublicationDate;
    private String mWebUrl;

    public NewsData(String webTitle, String author, String sectionName, String webPublishedDate, String webUrl) {
        mWebTitle = webTitle;
        mSectionName = sectionName;
        mAuthor = author;
        mWebPublicationDate = webPublishedDate;
        mWebUrl = webUrl;
    }

    public String getTitle() {
        return mWebTitle;
    }

    public String getSectionName()    {
        return mSectionName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    public String getWebUrl() {
        return mWebUrl;
    }
}
