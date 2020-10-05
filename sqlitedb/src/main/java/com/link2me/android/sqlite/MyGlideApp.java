package com.link2me.android.sqlite;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class MyGlideApp extends AppGlideModule {

    // Failed to find GeneratedAppGlideModule 메시지 나오는 것 없애기 위해서

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}

