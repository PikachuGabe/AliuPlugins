package com.aluicord.plugins;

import android.content.Context;
import android.net.Uri;

import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PreHook;
import com.discord.utilities.io.NetworkUtils;

import java.util.List;

import kotlin.jvm.functions.Function1;

@AliucordPlugin
public class TwitterJPGFix extends Plugin {

    @Override
    public void start(Context context) throws Throwable {

        patcher.patch(
                NetworkUtils.class.getDeclaredMethod("downloadFile", Context.class, Uri.class, String.class, String.class, Function1.class, Function1.class),
                new PreHook(param -> {
                    Uri uri = (Uri) param.args[1];

                    if (!uri.getPath().contains(".twimg.com")) {
                        return;
                    }

                    try {
                        String uriStr = uri.toString();
                        String newUri = uriStr.replaceAll("(?i)((\\.jpg:large)|(\\.jpg%3Alarge)|(\\.jpg_large)|(\\.jpg%5Flarge))$", ".jpg");
                        uri = Uri.parse(newUri);
                    } catch (Exception e) {
                        logger.error(e);
                    }

                    param.args[1] = uri;
                })
        );

    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }


}
