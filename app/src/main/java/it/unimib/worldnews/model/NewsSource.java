package it.unimib.worldnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsSource implements Parcelable {
    private String name;

    public NewsSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewsSource{" +
                "name='" + name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
    }

    protected NewsSource(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<NewsSource> CREATOR = new Parcelable.Creator<NewsSource>() {
        @Override
        public NewsSource createFromParcel(Parcel source) {
            return new NewsSource(source);
        }

        @Override
        public NewsSource[] newArray(int size) {
            return new NewsSource[size];
        }
    };
}
