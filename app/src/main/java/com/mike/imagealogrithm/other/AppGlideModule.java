package com.mike.imagealogrithm.other;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;

/**
 * app glide module for gallery
 */

@GlideModule
public final class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setLogLevel(Log.VERBOSE);
    }
}
