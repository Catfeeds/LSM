package com.test.lsm.db.service.inter;

import com.test.lsm.db.bean.Step;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
public interface IStepService {

    /**
     * 记录步数
     * 当前天 当前小时 下只有一条记录 存在则更新
     *step中的步数为总步数
     * @param step
     */
    void addCurrentDayStep(Step step);

    /**
     * 得到当前天的步数(所有小时的)
     *
     * @return
     */
    List<Step> getCurrentDateStep();

    /**
     * 得到当天除了某个小时的其他信息
     *
     * @return
     */
    List<Step> getOtherStepByHour(int hour);

    /**
     * 得到当前天某一小时的步数
     *
     * @param hour
     * @return
     */
    Step getStepByHourOnCurrentDay(int hour);

}
