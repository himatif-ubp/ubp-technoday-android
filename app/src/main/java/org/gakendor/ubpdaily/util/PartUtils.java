package org.gakendor.ubpdaily.util;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Dizzay on 11/15/2017.
 */

public class PartUtils {

    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*"),
                        file
                );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static RequestBody createPartFromString(String string){
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }
}

