package com.Senior.Faff.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Not_Today on 3/17/2017.
 */

public class Helper {
    private static final String TAG = Helper.class.getSimpleName();

    public byte[] ConvertBitmapToArrayOfByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] b = stream.toByteArray();
        return b;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, Context context, String name) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, name + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path, String name) {
        Bitmap b = null;
        try {
            File f = new File(path, name + ".png");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public String multipartRequest(String urlTo, Map<String, String> parmas, ArrayList<String> imgPath, String filefield, String fileMimeType) throws Exception {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        InputStream inputStream = null;


        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        if (imgPath.size() > 0) {
            try {
                URL url = new URL(urlTo);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                outputStream = new DataOutputStream(connection.getOutputStream());

                for (int i = 0; i < imgPath.size(); i++) {
                    String filepath = imgPath.get(i);
                    Log.i(TAG, "  i : " + i + "  filename : " + filepath);
                    String[] q = filepath.split("/");
                    int idx = q.length - 1;

                    File file = new File(filepath);
                    fileInputStream = new FileInputStream(file);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
                    outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                    outputStream.writeBytes(lineEnd);

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    outputStream.writeBytes(lineEnd);
                    fileInputStream.close();
                }

                // Upload POST Data
                Iterator<String> keys = parmas.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = parmas.get(key);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(value);
                    outputStream.writeBytes(lineEnd);
                }

                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                if (200 != connection.getResponseCode()) {
                    throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
                }


                inputStream = connection.getInputStream();
                result = this.convertStreamToString(inputStream);
                inputStream.close();
                outputStream.flush();
                outputStream.close();

                return result;
            } catch (Exception e) {
                throw new Exception(e);
            }
        } else {
            return "Wrong Size";
        }
    }

    public String getRequest(String urlTo) throws Exception {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        String result = "";

        try {
            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setUseCaches(false);

            connection.setRequestMethod("GET");
            if (200 != connection.getResponseCode()) {
                throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();
            result = this.convertStreamToString(inputStream);
            inputStream.close();

            return result;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
