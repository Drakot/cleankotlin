package es.demokt.kotlindemoproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by aluengo on 6/10/16.
 */

public class FileUtil {
  public static final String LOG_TAG = FileUtil.class.getSimpleName();

  public static final String TEMP_FOLDER = "temp";
  public static final String TEMP_PATH =
      Environment.getExternalStorageDirectory() + File.separator + TEMP_FOLDER + File.separator;
  public static final String VIDEO_EXTENSION = ".mp4";
  public static final String VIDEO_FILE_NAME = "video_temp_file" + VIDEO_EXTENSION;

  public static final String IMAGE_EXTENSION = ".jpg";
  public static final String IMAGE_FILE_NAME = "image_temp_file" + IMAGE_EXTENSION;

  public static String convertFileToBase64(String path) {
    String file = null;
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(path);
      byte[] buffer = new byte[8192];
      int bytesRead;
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        output64.write(buffer, 0, bytesRead);
      }
      output64.close();
      file = output.toString();
    } catch (Exception e) {
      Log.e(LOG_TAG, e.getMessage());
    }
    return file;
  }

  public static String convertBitmapToBase64(Bitmap bitmap) {
    String image = null;
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    byte[] byteArray = stream.toByteArray();
    image = Base64.encodeToString(byteArray, Base64.DEFAULT);
    return image;
  }

  public static String getPathFromUri(Activity activity, Uri data) {
    String videoName = null;
    String path = data.getPath();
    int lastSeparatorIndex = path.lastIndexOf(File.separator);
    String videoId = path.substring(lastSeparatorIndex + 1);

    Cursor videoCursor =
        activity.getContentResolver().query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] {
                MediaStore.Video.Media._ID, MediaStore.Images.Media.DATA
            }, null, null, null);
    if (videoCursor != null) {
      if (videoCursor.moveToFirst()) {
        do {
          String id =
              videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media._ID));
          if (videoId.equals(id)) {
            videoName =
                videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            break;
          }
        } while (videoCursor.moveToNext());
      }
      videoCursor.close();
    }
    return videoName;
  }

  public static String getVideoPath(Intent data) {
    Uri uri = null;
    String fullPath = null;
    if (data != null) {
      uri = data.getData();
    }
    if (uri != null) {
      try {
        fullPath = FileUtil.TEMP_PATH + FileUtil.VIDEO_FILE_NAME;
        String output = FileUtil.convertFileToBase64(fullPath);
        //new File(fullPath).delete();
        if (output != null && !output.isEmpty()) {
          Log.d(LOG_TAG, "video Ok");
        } else {
          Log.e(LOG_TAG, "outputError");
        }
      } catch (Exception e) {
        Log.e(LOG_TAG, e.getMessage());
      }
    }
    return fullPath;
  }

  public static String getImage(Intent data) {
    String image = null;
    Bitmap bitmap = getImageDataFromIntent(data);
    if (bitmap != null) {
      image = FileUtil.convertBitmapToBase64(bitmap);
    } else {
      Log.e(LOG_TAG, "Could not capture image");
    }
    return image;
  }

  public static final String DATA = "data";

  public static Bitmap getImageDataFromIntent(Intent intent) {
    Bitmap bitmap = null;
    if (intent != null && intent.getExtras() != null && intent.getExtras().get(DATA) != null) {
      Object data = intent.getExtras().get(DATA);
      bitmap = data != null ? (Bitmap) data : null;
    }
    return bitmap;
  }

  public static String convertUriToImageBase64(Activity activity, Uri uri) {
    String base64 = null;
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
      base64 = FileUtil.convertBitmapToBase64(bitmap);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return base64;
  }

  public static boolean saveToFile(Activity activity, String base64) {
    byte[] pdfAsBytes = Base64.decode(base64, 0);
    try {
      File filePath = new File(Environment.getExternalStorageDirectory() + "/braodcasts.pdf");
      FileOutputStream os = new FileOutputStream(filePath, true);

      os.write(pdfAsBytes);
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public static String convertUriToVideoBase64(Activity activity, Uri uri) {
    String videoPath = FileUtil.getPathFromUri(activity, uri);
    //Log.printParams(LOG_TAG, "Gallery VideoPath ", videoPath);
    String videoBase64 = FileUtil.convertFileToBase64(videoPath);
    return videoBase64;
  }

  public static String convertBytesToKb(int length) {
    return (length / 1024) + "kb";
  }

  public static String convertBytesToKb(long length) {
    return (length / 1024) + "kb";
  }

  public static String saveToInternalStorage(Bitmap bitmapImage, Context context) {
    int IMAGE_QUALITY = 80;
    File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
    File myPath = new File(directory, "image" + System.currentTimeMillis() + ".jpg");
    FileOutputStream fos;
    try {
      fos = new FileOutputStream(myPath);
      // Use the compress method on the BitMap object to write image to
      // the OutputStream
      getResizedBitmap(bitmapImage, 750).compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, fos);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return myPath.getAbsolutePath();
  }

  public static String saveToInternalStorage(Activity activity, Uri uri) {
    Bitmap bitmap = null;
    try {
      bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
      return saveToInternalStorage(bitmap, activity);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
    int width = image.getWidth();
    int height = image.getHeight();

    float bitmapRatio = (float) width / (float) height;
    if (bitmapRatio > 1) {
      width = maxSize;
      height = (int) (width / bitmapRatio);
    } else {
      height = maxSize;
      width = (int) (height * bitmapRatio);
    }

    return Bitmap.createScaledBitmap(image, width, height, true);
  }
}
