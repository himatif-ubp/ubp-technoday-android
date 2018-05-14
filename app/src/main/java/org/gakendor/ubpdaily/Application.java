package org.gakendor.ubpdaily;

import android.support.multidex.MultiDex;

import com.snatik.storage.Storage;

import org.gakendor.ubpdaily.util.Static;

import java.io.File;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by yaziedda on 3/28/18.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        EasyImage.configuration(this)
                .setImagesFolderName(Static.DIR_IMAGE)
                .saveInAppExternalFilesDir()
                .saveInRootPicturesDirectory();
        Storage storage = new Storage(getApplicationContext());
        String path = storage.getExternalStorageDirectory();
        String pathFile = path + File.separator + Static.DIR_IMAGE;
        if(storage.createDirectory(pathFile)){
            System.out.println("EXISTS : "+pathFile);
        }else{
            System.out.println("Not Exists");
        }
        storage.createDirectory(pathFile);
    }
}
