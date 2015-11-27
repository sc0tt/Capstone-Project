package io.adie.upscoot.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Random;

import io.adie.upscoot.R;
import io.adie.upscoot.tables.UploadedImage;

/**
 * Implementation of App Widget functionality.
 */
public class UploadedImagesWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {


        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.uploaded_images_widget);

        Random generator = new Random();
        // Get a list of the user's uploaded images
        List<UploadedImage> images = UploadedImage.listAll(UploadedImage.class);

        if (images.size() > 0) {
            int rndIndex = generator.nextInt(images.size());

            UploadedImage selected = images.get(rndIndex);

            Ion.with(context)
                    .load(selected.getURL())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            views.setImageViewBitmap(R.id.uploaded_image, result);
                            // Instruct the widget manager to update the widget
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    });
        } else {
            views.setImageViewResource(R.id.uploaded_image, R.drawable.uploaded_image_none);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

