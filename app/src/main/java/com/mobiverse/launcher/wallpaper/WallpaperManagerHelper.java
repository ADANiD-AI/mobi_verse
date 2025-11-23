package com.mobiverse.launcher.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WallpaperManagerHelper {
    private Context context;
    private WallpaperManager wallpaperManager;
    private FirebaseStorage storage;

    public WallpaperManagerHelper(Context context) {
        this.context = context;
        this.wallpaperManager = WallpaperManager.getInstance(context);
        this.storage = FirebaseStorage.getInstance();
    }

    public void setWallpaperFromUrl(String url, WallpaperCallback callback) {
        StorageReference ref = storage.getReferenceFromUrl(url);
        
        ref.getBytes(10 * 1024 * 1024) // Max 10MB
            .addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                setWallpaper(bitmap, callback);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void setWallpaperFromUri(Uri uri, WallpaperCallback callback) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            setWallpaper(bitmap, callback);
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public void setWallpaperFromAsset(String assetName, WallpaperCallback callback) {
        try {
            InputStream inputStream = context.getAssets().open("wallpapers/" + assetName);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            setWallpaper(bitmap, callback);
        } catch (IOException e) {
            callback.onError(e.getMessage());
        }
    }

    private void setWallpaper(Bitmap bitmap, WallpaperCallback callback) {
        try {
            wallpaperManager.setBitmap(bitmap);
            callback.onSuccess();
        } catch (IOException e) {
            callback.onError(e.getMessage());
        }
    }

    public void saveWallpaperLocally(Bitmap bitmap, String fileName) throws IOException {
        File wallpaperDir = new File(context.getFilesDir(), "wallpapers");
        if (!wallpaperDir.exists()) wallpaperDir.mkdirs();
        
        File file = new File(wallpaperDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
    }

    public interface WallpaperCallback {
        void onSuccess();
        void onError(String message);
    }
}