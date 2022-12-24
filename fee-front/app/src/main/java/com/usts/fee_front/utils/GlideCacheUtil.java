package com.usts.fee_front.utils;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author zdaneel
 */
public class GlideCacheUtil {
    /**
     * 获取不缓存策略
     * @return 缓存策略
     */
    public static RequestOptions getCacheStrategy() {
        RequestOptions requestOptions = new RequestOptions();
        return requestOptions.circleCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
    }
}
