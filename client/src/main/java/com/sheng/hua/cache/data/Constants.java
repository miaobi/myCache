package com.sheng.hua.cache.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
    public static final int TYPE_CACHE_PUT = 0;
    public static final int TYPE_CACHE_GET = 1;

    public static final String SPLIT = "|";

    public static final String FILE_NAME = "D://workspace_intellij/clientCat" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".index";
}
