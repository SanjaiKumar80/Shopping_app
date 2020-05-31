package com.example.shoppingapp.config;

import android.content.Context;
import android.content.Intent;

import com.example.shoppingapp.connectivityReceiver.Noconnection;

import java.io.File;

public class Config {

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception ignored) {}
    }
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }
    public static void noConnection(final Context context)
    {
        try
        {
            Intent intent = new Intent(context.getApplicationContext(), Noconnection.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            context.startActivity(intent);

        }
        catch (Exception e)
        {
//            Log.e("No Connection",e.toString());
        }

    }









}
