package com.avin.disklrucache;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by avin on 04/08/15.
 */
public class LruCacheImpl implements LruCache {


    //Useful to delete least recently used item
    LinkedHashMap<String, String> singlyLinkedKeyList= new LinkedHashMap<>();
    File configFile;

    private static LruCacheImpl instance;

    public static LruCacheImpl getInstance(){
        if(null == instance){
            instance= new LruCacheImpl();
        }
        return instance;
    }

    private LruCacheImpl(){
        init();
    }


    private void init(){
        File sdcard = Environment.getExternalStorageDirectory();
        File root = new File(sdcard, Config.CACHE_DIRECTORY);
        if(!root.exists()){
            root.mkdir();
        }
        configFile = new File(root,Config.CACHE_LINKED_LIST_FILE);

        try {

            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = br.readLine()) != null) {
                singlyLinkedKeyList.put(line, null);
            } } catch (FileNotFoundException e1) {
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }


    //To be called when App is closing
    @Override
    public void finish(){
        StringBuilder builder= new StringBuilder();
        int totalElements=singlyLinkedKeyList.size();
        int i=0;
        for (String s: singlyLinkedKeyList.keySet()){
            builder.append(s);
            if(i!= totalElements-1){
                builder.append("\n");
            }
        }
        FileHelper.writeStringAsFile(builder.toString(), configFile );
    }

    @Override
    public String get(String key) {
        File file = new File(getRootDirectory(), key);
        if(FileHelper.doesFileExist(file)){
            //the below two lines bring Element to the front
            singlyLinkedKeyList.remove(key);
            singlyLinkedKeyList.put(key, key);
            return FileHelper.readFileAsString(file);
        }
        return null;
    }

    @Override
    public void put(String key, String value) {
        if(singlyLinkedKeyList.size()>Config.MAX_ITEMS_ALLOWED){
            removeLeastRecentlyUsedItem();
        }
        File file = new File(getRootDirectory(), key);
        singlyLinkedKeyList.put(key, key);
        FileHelper.writeStringAsFile(value, file);
    }

    @Override
    public void purge(String key) {
        File file = new File(getRootDirectory(), key);
        singlyLinkedKeyList.remove(key);
        file.delete();
    }

    @Override
    public void purgeAll() {
        for(String key: singlyLinkedKeyList.keySet()){
            File file = new File(getRootDirectory(), key);
            file.delete();
        }
        singlyLinkedKeyList.clear();
    }

    private void removeLeastRecentlyUsedItem(){
        //remove the first key
        for(String key: singlyLinkedKeyList.keySet()){
            purge(key);
            break;
        }
    }

    private File getRootDirectory(){
        File sdcard = Environment.getExternalStorageDirectory();
        File root = new File(sdcard, Config.CACHE_DIRECTORY);
        if(!root.exists()){
            root.mkdir();
        }
        return root;
    }
}
