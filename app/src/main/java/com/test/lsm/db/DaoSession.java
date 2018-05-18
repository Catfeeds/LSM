package com.test.lsm.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.test.lsm.db.bean.PushMsg;
import com.test.lsm.db.bean.Step;

import com.test.lsm.db.PushMsgDao;
import com.test.lsm.db.StepDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig pushMsgDaoConfig;
    private final DaoConfig stepDaoConfig;

    private final PushMsgDao pushMsgDao;
    private final StepDao stepDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        pushMsgDaoConfig = daoConfigMap.get(PushMsgDao.class).clone();
        pushMsgDaoConfig.initIdentityScope(type);

        stepDaoConfig = daoConfigMap.get(StepDao.class).clone();
        stepDaoConfig.initIdentityScope(type);

        pushMsgDao = new PushMsgDao(pushMsgDaoConfig, this);
        stepDao = new StepDao(stepDaoConfig, this);

        registerDao(PushMsg.class, pushMsgDao);
        registerDao(Step.class, stepDao);
    }
    
    public void clear() {
        pushMsgDaoConfig.clearIdentityScope();
        stepDaoConfig.clearIdentityScope();
    }

    public PushMsgDao getPushMsgDao() {
        return pushMsgDao;
    }

    public StepDao getStepDao() {
        return stepDao;
    }

}
