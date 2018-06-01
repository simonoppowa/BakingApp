package com.github.simonoppowa.bakingapp.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.util.HashMap;

public class VideoRequestHandler extends RequestHandler{

    public static final String SCHEME_VIDEO = "https";

    private static final int VIDEO_THUMBNAIL_WIDTH = 853;
    private static final int VIDEO_THUMBNAIL_HEIGHT = 480;

    @Override
    public boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        return SCHEME_VIDEO.equals(scheme);
    }

    @Override
    public Result load(Request request, int networkPolicy) {
        Bitmap bitmap = null;
        try {
            bitmap = retrieveVideoFrameFromVideo(request.uri.toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new Result(bitmap, Picasso.LoadedFrom.DISK);
    }

    /**
     * Fetches the first frame of the video and cuts it to VIDEO_THUMBNAIL_WIDTH x VIDEO_THUMBNAIL_HEIGHT pixels
     * @param videoPath Url to video
     * @return cutted bitmap
     */
    private static Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retrieveVideoFrameFromVideo" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        //cutting down image for better performance
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, VIDEO_THUMBNAIL_WIDTH, VIDEO_THUMBNAIL_HEIGHT, false
        );

        return resizedBitmap;
    }

}
