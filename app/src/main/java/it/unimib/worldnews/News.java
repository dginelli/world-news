package it.unimib.worldnews;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    private String title;
    private String source;

    public News(String title, String source) {
        this.title = title;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.source);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.source = source.readString();
    }

    protected News(Parcel in) {
        this.title = in.readString();
        this.source = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
