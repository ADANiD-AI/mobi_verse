package com.mobiverse.launcher.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class ContactSearchProvider implements SearchProvider {

    private Context context;

    public ContactSearchProvider(Context context) {
        this.context = context;
    }

    @Override
    public List<SearchResult> query(String query) {
        List<SearchResult> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        };
        String selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
                intent.setData(contactUri);

                results.add(new SearchResult(
                    SearchResultType.CONTACT,
                    name,
                    null,
                    null, // You can load the contact photo here
                    intent
                ));
            }
            cursor.close();
        }

        return results;
    }
}