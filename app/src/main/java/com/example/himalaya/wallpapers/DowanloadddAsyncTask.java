package com.example.himalaya.wallpapers;

import android.app.ProgressDialog;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.himalaya.wallpapers.Activity.DownloadImage;
import com.example.himalaya.wallpapers.Activity.Wallpapers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by himalaya on 20/1/17.
 */

public class DowanloadddAsyncTask extends AsyncTask<String,Void,String>
{
    int count;
    ProgressDialog pDialog;
    Wallpapers newString;
    private  File file;

    public DowanloadddAsyncTask(Wallpapers wallpapers)
    {
        newString=wallpapers;
    }


    @Override
    protected String doInBackground(String... params)
    {
        try {
            URL url = new URL(params[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),8192);
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/PixawallWallpapers/");
            if(folder.mkdir())
            {
                 file = new File(new File(String.valueOf(folder)),getCurrentTimeStamp() + ".jpg");
            }

            else
            {
                 file = new File(new File(String.valueOf(folder)),getCurrentTimeStamp() + ".jpg");

            }

            // Output stream
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1)
            {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    private void publishProgress(String s)
    {

        pDialog.setProgress(Integer.parseInt(s));
    }



    @Override
    protected void onPreExecute()
    {
         pDialog = new ProgressDialog(newString);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String s)
    {
        pDialog.dismiss();
        Toast.makeText(newString,"Successfully Saved",Toast.LENGTH_SHORT).show();
        MediaScannerConnection.scanFile(
               newString,
                new String[]{file.getAbsolutePath()},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri)
                    {
                        Log.v("grokkingandroid","file"+path+"Was Scanned seccessfully: " + uri);
                    }
                });

    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }


}
