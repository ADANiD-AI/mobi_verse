package com.mobiverse.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import com.mobiverse.MobiVerse.R;
import com.mobiverse.ai.MobiverseAIHelper;

public class ShortcutManagerHelper {

    public static void addLinkToHomeScreen(Context context, String url, String title) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);

            if (shortcutManager.isRequestPinShortcutSupported()) {
                // لنک کھولنے کا ارادہ (Intent)
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                
                // شارٹ کٹ کا ڈیزائن
                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(context, "shortcut_" + title)
                        .setShortLabel(title)
                        .setLongLabel("Open " + title)
                        .setIcon(Icon.createWithResource(context, R.drawable.ic_globe_icon)) // ویب کا آئیکن
                        .setIntent(intent)
                        .build();

                // سسٹم سے اجازت مانگیں اور ایڈ کریں
                shortcutManager.requestPinShortcut(pinShortcutInfo, null);
                MobiverseAIHelper.getInstance(context).speak(title + " has been added to your home screen.");
            }
        }
    }
}
