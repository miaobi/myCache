package com.hua.cache.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
//    public static final String FILE_NAME = "D:/workspace_intellij/serverCat.index";
//    public static final String FILE_NAME = "D:/workspace_intellij/10.log";
public static final String FILE_NAME = "D://workspace_intellij/serverCat" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".index";
}
