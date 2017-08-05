package me.linyujie.mydribbbo.utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import me.linyujie.mydribbbo.model.Shot;

/**
 * Created by linyujie on 8/4/17.
 */

public class ImageUtils {


    public static void loadUserPicture(@NonNull String UserImageUrl, @NonNull SimpleDraweeView imageView) {
        Uri imageUri = Uri.parse(UserImageUrl);
        imageView.setImageURI(imageUri);

    }

    public static void loadShotImage(@NonNull Shot shot, @NonNull SimpleDraweeView imageView) {
        String imageUrl = shot.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            Uri imageUri = Uri.parse(imageUrl);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(imageUri)
                    .setAutoPlayAnimations(true)
                    .build();
            imageView.setController(controller);
        }
    }
}
