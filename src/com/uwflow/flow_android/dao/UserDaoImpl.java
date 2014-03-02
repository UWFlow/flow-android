package com.uwflow.flow_android.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.uwflow.flow_android.db_object.User;

import java.sql.SQLException;

public class UserDaoImpl extends BaseDaoImpl<User, String> implements UserDao{
    public UserDaoImpl(ConnectionSource connectionSource)
            throws SQLException {
        super(connectionSource, User.class);
    }

    protected UserDaoImpl(ConnectionSource connectionSource, DatabaseTableConfig tableConfig) throws java.sql.SQLException {
        super(connectionSource, tableConfig);
    }
}
