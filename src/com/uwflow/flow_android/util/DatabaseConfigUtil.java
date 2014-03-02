package com.uwflow.flow_android.util;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/** Run this file to keep database data in sync with output file, this will
 * increase performance by 20x
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile("ormlite_config.txt");
    }
}
