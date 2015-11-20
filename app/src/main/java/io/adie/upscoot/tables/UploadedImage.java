package io.adie.upscoot.tables;

import com.orm.SugarRecord;

public class UploadedImage extends SugarRecord<UploadedImage> {
    String imageURL;
    boolean isPrivate;

    public UploadedImage() {

    }

    public UploadedImage(String imageURL, boolean isPrivate){
        this.imageURL = imageURL;
        this.isPrivate = isPrivate;
    }

    public String getURL(){
        return this.imageURL;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }
}
