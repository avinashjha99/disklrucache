package com.avin.disklrucache;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by avin on 04/08/15.
 */
public class FileHelper {

    public static void writeStringAsFile(final String fileContents, File file) {
        try {
            FileWriter out = new FileWriter(file);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.d("error", "IOException " + e);
        }
    }

    public static String readFileAsString(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Log.d("error", "FNFException " + e);
        } catch (IOException e) {
            Log.d("error", "IOException " + e);
        }
        return stringBuilder.toString();
    }

  public static boolean doesFileExist(File file){
      if(null!= file){
          return file.canRead();
      }
      return false;
  }

}
