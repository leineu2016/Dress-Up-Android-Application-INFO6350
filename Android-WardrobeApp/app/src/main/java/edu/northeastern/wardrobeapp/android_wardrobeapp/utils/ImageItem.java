package edu.northeastern.wardrobeapp.android_wardrobeapp.utils;

public class ImageItem {
    private String title;
    private String url;

    public ImageItem(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String image_title) {
        this.title = image_title;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String image_id) {
        this.url = image_id;
    }
}
