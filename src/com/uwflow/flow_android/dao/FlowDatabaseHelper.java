package com.uwflow.flow_android.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.*;

import java.sql.SQLException;

public class FlowDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "FlowDatabaseHelper";

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "flowDatabaseAndroid.db";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    /**
     * NOTE IF YOU CHANGE ANYTHING IN ANY OF THE DATABASE FILES OR ADD NEW DATABASE FILES
     * YOU MUST RE-RUN THE MAIN METHOD IN DatabaseConfigUtil class in the util folder
     * Use Run as a different java application! Not the same CONTEXT as flow.
     * @param context
     */

    private Dao<User, String> userDao;
    private Dao<Course, String> userCourseDao;
    private Dao<ScheduleCourse, String> userScheduleCourseDao;
    private Dao<Exam, Integer> userExamDao;
    private Dao<UserCourse, String> userCourseExtraDao;
    private Dao<ScheduleImage, String> userSchduleImageDao;

    /**
     * NOTE IF YOU CHANGE ANYTHING IN ANY OF THE DATABASE FILES OR ADD NEW DATABASE FILES
     * YOU MUST RE-RUN THE MAIN METHOD IN DatabaseConfigUtil class in the util folder
     * Use Run as a different java application! Not the same CONTEXT as flow.
     * @param context
     */
    public FlowDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Course.class);
            TableUtils.createTable(connectionSource, ScheduleCourse.class);
            TableUtils.createTable(connectionSource, Exam.class);
            TableUtils.createTable(connectionSource, UserCourse.class);
            TableUtils.createTable(connectionSource, ScheduleImage.class);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to create databases", e);
            Crashlytics.logException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Course.class, true);
            TableUtils.dropTable(connectionSource, ScheduleCourse.class, true);
            TableUtils.dropTable(connectionSource, Exam.class, true);
            TableUtils.dropTable(connectionSource, UserCourse.class, true);
            TableUtils.dropTable(connectionSource, ScheduleImage.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
            Crashlytics.logException(e);
        }
    }

    public Dao<User, String> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    public Dao<Course, String> getUserCourseDao() throws SQLException {
        if (userCourseDao == null){
            userCourseDao = getDao(Course.class);
        }
        return userCourseDao;
    }

    public Dao<ScheduleCourse, String> getUserScheduleCourseDao() throws SQLException {
        if (userScheduleCourseDao == null){
            userScheduleCourseDao = getDao(ScheduleCourse.class);
        }
        return userScheduleCourseDao;
    }

    public Dao<Exam, Integer> getUserExamDao() throws SQLException {
        if (userExamDao == null){
            userExamDao = getDao(Exam.class);
        }
        return userExamDao;
    }

    public Dao<UserCourse, String> getUserCourseExtraDao() throws SQLException {
        if (userCourseExtraDao == null){
            userCourseExtraDao = getDao(UserCourse.class);
        }
        return userCourseExtraDao;
    }

    public Dao<ScheduleImage, String> getUserSchduleImageDao() throws SQLException {
        if (userSchduleImageDao == null){
            userSchduleImageDao = getDao(ScheduleImage.class);
        }
        return userSchduleImageDao;
    }
}
