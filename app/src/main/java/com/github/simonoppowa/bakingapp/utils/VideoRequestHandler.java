package com.github.simonoppowa.bakingapp.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.util.HashMap;

public class VideoRequestHandler extends RequestHandler{

    public static final String SCHEME_VIDEO = "https";

    private static final int VIDEO_THUMBNAIL_WIDTH = 1280;
    private static final int VIDEO_THUMBNAIL_HEIGHT = 720;

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

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();

            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());

            bitmap = mediaMetadataRetriever.getFrameAtTime();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retrieveVideoFrameFromVideo" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, VIDEO_THUMBNAIL_WIDTH, VIDEO_THUMBNAIL_HEIGHT, false
        );

        return resizedBitmap;
    }

}
