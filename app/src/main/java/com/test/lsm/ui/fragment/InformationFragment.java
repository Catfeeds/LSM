package com.test.lsm.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.swm.algorithm.Algorithm;
import com.swm.algorithm.support.IirFilter;
import com.swm.algorithm.support.heartrate.HeartRate;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.InfoBean;
import com.test.lsm.bean.event.CalorieChgEvent;
import com.test.lsm.bean.event.ECGChgEvent;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.event.StepChgEvent;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.StepService;
import com.test.lsm.db.service.inter.IStepService;
import com.test.lsm.global.Constant;
import com.test.lsm.ui.activity.ECGShowActivity3;
import com.test.lsm.utils.AlgorithmWrapper;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.SportStepJsonUtils;
import com.today.step.lib.TodayStepService;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.lsmalgorithm.MyLib;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：数据信息界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class InformationFragment extends LsmBaseFragment {

    private static final String TAG = "InformationFragment";
    @BindView(R.id.tv_heart_num)
    TextView tvHeartNum;
    @BindView(R.id.ll_con)
    LinearLayout llCon;
    @BindView(R.id.tv_step_num)
    TextView tvStepNum;
    @BindView(R.id.ll_con2)
    LinearLayout llCon2;
    @BindView(R.id.ll_con3)
    LinearLayout llCon3;
    @BindView(R.id.tv_calorie)
    TextView tvCalorie;
    @BindView(R.id.fl_heart)
    FrameLayout flHeart;
    @BindView(R.id.fl_step)
    FrameLayout flStep;
    @BindView(R.id.fl_calorie)
    FrameLayout flCalorie;
    @BindView(R.id.rl_heart)
    RelativeLayout rlHeart;
    @BindView(R.id.rl_step)
    RelativeLayout rlStep;
    @BindView(R.id.rl_calorie)
    RelativeLayout rlCalorie;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.fl_hr_chart)
    FrameLayout flHrChart;

    private Activity mAct;
    private MyApplication application;

    CircularFifoQueue<Integer> ecgBuffer = new CircularFifoQueue(2500);

    private int index= 0;

    private int rriIndex = 0;

    //HeartRate hrImpl = Algorithm.newHeartRateInstance();

    double[] rriAry = new double[196000];
    double[] timeAry = new double[196000];


    private IirFilter iirFilter = Algorithm.newIirFilterInstance();

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {//心电图
                    byte[] obj = (byte[]) msg.obj;
                    // 处理心电图数据
                    /*String hexStr = HexUtil.encodeHexStr(obj);
                    String substring = hexStr.substring(0, 4);
                    int parseInt = Integer.parseInt(substring, 16);
                    //MyLog.e(TAG, "encodeHexStr===" + hexStr);
                    short[] data = new short[]{
                            getECGValue(1, obj),
                            getECGValue(2, obj),
                            getECGValue(3, obj),
                            getECGValue(4, obj),
                            getECGValue(5, obj)};*/

                 /*  int[] data = new int[]{
                           Integer.parseInt(hexStr.substring(0, 4), 16),
                           Integer.parseInt(hexStr.substring(4, 8), 16),
                           Integer.parseInt(hexStr.substring(8, 12), 16),
                           Integer.parseInt(hexStr.substring(12, 16), 16),
                           Integer.parseInt(hexStr.substring(16, 20), 16)
                   };*/


                    short[] ecgData = Algorithm.getEcgByte2Short(obj);

                    EventBus.getDefault().post(new ECGChgEvent(ecgData, "得到ECG的值了==="));
                    for (short ecg: ecgData) {
                        ecgBuffer.add(Integer.valueOf(ecg));
                    }

                    AlgorithmWrapper.startRRI();

                    index++;
                    if (ecgBuffer.size()>=1500 && index%200==0){
                        index = 0;
                        int[] ints = new int[ecgBuffer.size()];
                        for (int i = 0; i < ecgBuffer.size(); i++) {
                            ints[i] = ecgBuffer.get(i);
                        }
                        int heartNum =Algorithm.getCalculateHeartRate(ints);
                         //int heartNum = hrImpl.countHeartRate(ecgData, getShort(obj, 11));
                        //MyLog.e(TAG , "epcData=="+epcData);
                        if (heartNum > 0) {
                            Constant.oneMinHeart.add(heartNum);
                            Constant.hrBuffer.add(heartNum);
                            Constant.hrBuffer2.add(heartNum);
                            tvHeartNum.setText("" + heartNum);
                            application.setHeartNum(heartNum);
                            EventBus.getDefault().post(new HeartChgEvent(heartNum, "心跳变化了"));
                            MyLog.e(TAG, "tvHeartNum：" + heartNum);
                            //Algorithm.initialForModeChange(1);
                            Algorithm.getRtoRIntervalData(rriAry , timeAry);
                            MyLog.e(TAG , "rriAry==111="+ Arrays.toString(rriAry));
                            MyLog.e(TAG , "timeAry==111="+ Arrays.toString(timeAry));
                            Constant.rriBuffer.clear();
                            for (double value: rriAry) {
                                int legalCount = 0;
                                if (value > 200 && value < 2000) {
                                    legalCount++;
                                    Constant.rriBuffer.add(Long.valueOf(Double.valueOf(value).intValue()));
                                    if (legalCount>300){//
                                        AlgorithmWrapper.stopRRI();
                                        // 通知刷新 HRV
                                        EventBus.getDefault().post(new RefreshHearthInfoEvent());
                                    }
                                }
                            }

                        }
                    }

                    //MyLog.e(TAG , epcData);
                    if (Constant.sbHeartData.length() < 500) {
                        Short ecg0 = ecgData[0];
                        Short ecg1 = ecgData[1];
                        Short ecg2 = ecgData[2];
                        Short ecg3 = ecgData[3];
                        Short ecg4 = ecgData[4];
                        String epcData2 = "" + iirFilter.filter(ecg0.intValue()) + ","
                                + iirFilter.filter(ecg1.intValue()) + ","
                                + iirFilter.filter(ecg2.intValue()) + ","
                                + iirFilter.filter(ecg3.intValue()) + ","
                                + iirFilter.filter(ecg4.intValue()) + ",";
                        Constant.sbHeartData.append(epcData2);
                    }
                    //Constant.sbHeartData2.append(epcData2);

                    Constant.egcDataCon.add(ecgData[0]);
                    Constant.egcDataCon.add(ecgData[1]);
                    Constant.egcDataCon.add(ecgData[2]);
                    Constant.egcDataCon.add(ecgData[3]);
                    Constant.egcDataCon.add(ecgData[4]);

                    //List<Long> rri = hrImpl.countRRI(ecgData, getShort(obj, 12));
                 /*   for (Long value : rri) {
                        if (value > 200 && value < 2000) {
                            Constant.rriBuffer.add(value);
                        }
                        //MyLog.e(TAG , "rri====value"+value);
                    }*/
                    break;
                }
                case 1: {
                    byte[] obj = (byte[]) msg.obj;
                    String hexStr = HexUtil.encodeHexStr(obj);
                    MyLog.e(TAG, hexStr);
                    int gyroX = Integer.parseInt(hexStr.substring(0, 4), 16);
                    int gyroY = Integer.parseInt(hexStr.substring(4, 8), 16);
                    int gyroZ = Integer.parseInt(hexStr.substring(8, 12), 16);
                    int accX = Integer.parseInt(hexStr.substring(12, 16), 16);
                    int accY = Integer.parseInt(hexStr.substring(16, 20), 16);
                    int accZ = Integer.parseInt(hexStr.substring(20, 24), 16);
                    int magX = Integer.parseInt(hexStr.substring(24, 28), 16);
                    int magY = Integer.parseInt(hexStr.substring(28, 32), 16);
                    int magZ = Integer.parseInt(hexStr.substring(32, 36), 16);
                    int stepNum = MyLib.countStep(gyroX, gyroY, gyroZ, accX, accY, accZ, magX, magY, magZ);
                    //MyLog.e(TAG, "stepNum：" + stepNum);
                    tvStepNum.setText("" + stepNum);
                    break;
                }
            }
            return false;
        }
    });
    private int heartNum;
    private IStepService stepService;
    private List<FrameLayout> itemContainer;


    /**
     * 得到数据转成short
     *
     * @param n     1、2、3、4、5
     * @param value
     * @return
     */
    public short getECGValue(int n, byte[] value) {
        if (n <= 0 || n > 5) {
            throw new IndexOutOfBoundsException();
        }

        return (short) (((value[(n - 1) * 2 + 1] & 0xFF) << 8) | (value[(n - 1) * 2] & 0xFF));
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index 第几位开始取
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        mAct = getActivity();
        application = (MyApplication) getActivity().getApplication();

        //Algorithm.initialForModeChange(1);

        List<InfoBean> infoBeanList = new ArrayList<>();
        infoBeanList.add(new InfoBean(0, "68"));
        infoBeanList.add(new InfoBean(1, "23"));
        infoBeanList.add(new InfoBean(2, "265"));

        stepService = new StepService();

        itemContainer = new ArrayList<>();
        itemContainer.add(flHeart);
        itemContainer.add(flStep);
        itemContainer.add(flCalorie);
        itemContainer.add(flHrChart);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        initStepCount();
    }

    @OnClick({R.id.rl_heart, R.id.rl_step, R.id.rl_calorie, R.id.rl_ecg , R.id.rl_hr_chart})
    public void onItemClick(View view) {

        // TransitionManager.beginDelayedTransition(llContainer);

        switch (view.getId()) {
            case R.id.rl_heart:
                if (Constant.rriBuffer.size() < 250) {
                    MyToast.showLong(getContext(), "还没有足够的数据请耐心等待" + Constant.rriBuffer.size());
                    return;
                }
                openItem(0);
                if (flHeart.getVisibility() == View.VISIBLE) {
                    EventBus.getDefault().post(new RefreshHearthInfoEvent());
                }
                break;
            case R.id.rl_step:
                openItem(1);
                if (flStep.getVisibility() == View.VISIBLE) {
                    EventBus.getDefault().post(new StepChgEvent(mStepSum, "步数值更新"));
                }
                break;
            case R.id.rl_calorie:
                openItem(2);
                if (flCalorie.getVisibility() == View.VISIBLE) {
                    String calorieByStep = SportStepJsonUtils.getCalorieByStep(mStepSum);
                    EventBus.getDefault().post(new CalorieChgEvent(Float.parseFloat(calorieByStep), "步数值更新"));
                }
                break;
            case R.id.rl_ecg:
                if (!application.isBleConnected()) {
                    MyToast.showLong(getContext(), "蓝牙设备未连接");
                } else {
                    ECGShowActivity3.startAction(getActivity());
                }
                break;
            case R.id.rl_hr_chart:
                openItem(3);
                MyLog.e(TAG , "==============");
                if (flHrChart.getVisibility() == View.VISIBLE){
                    Constant.isHRChartDetailShow = true;
                }
                break;
        }
        view.setVisibility(View.VISIBLE);
    }

    private void openItem(int position){
        Constant.isHRChartDetailShow = false;
        for (int i = 0; i < itemContainer.size(); i++) {
            FrameLayout item = itemContainer.get(i);
            if (position==i){
                if (item.getVisibility() == View.VISIBLE){
                    item.setVisibility(View.GONE);
                }else{
                    item.setVisibility(View.VISIBLE);
                }
            }else{
                item.setVisibility(View.GONE);
            }
        }
    }

    private static final int REFRESH_STEP_WHAT = 1000;
    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL_REFRESH = 500;
    private Handler mDelayHandler = new Handler(new TodayStepCounterCall());
    private int mStepSum;
    private ISportStepInterface iSportStepInterface;
    ServiceConnection stepServiceConnect;

    private void initStepCount() {
        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(mAct, TodayStepService.class);
        mAct.startService(intent);
        mAct.bindService(intent, stepServiceConnect = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
                try {
                    mStepSum = iSportStepInterface.getCurrentTimeSportStep();
                    updateStepCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        int step = 0;
                        try {
                            step = iSportStepInterface.getCurrentTimeSportStep();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (mStepSum != step) {
                            mStepSum = step;
                            updateStepCount();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

    private void updateStepCount() {

        Step step = new Step();
        step.setStepNum(mStepSum);
        step.setDate(MyTimeUtils.getCurrentDate());
        step.setHour(MyTimeUtils.getCurrentHour());
        stepService.addCurrentDayStep(step);

        Log.e(TAG, "updateStepCount : " + mStepSum);
        tvStepNum.setText(mStepSum + "");
        tvCalorie.setText(SportStepJsonUtils.getCalorieByStep(mStepSum) + "");
        String distanceByStep = SportStepJsonUtils.getDistanceByStep(mStepSum);
        String caloriesValue = SportStepJsonUtils.getCalorieByStep(mStepSum);
        application.setStepNum(mStepSum);
        application.setStepDistance(Double.parseDouble(distanceByStep));
        application.setCalorieValue(Double.parseDouble(caloriesValue));
        EventBus.getDefault().post(new StepChgEvent(mStepSum, "步数值更新"));
        //EventBus.getDefault().post(new PushMsgBean(R.mipmap.msg1));

        //EventBus.getDefault().post(new CalorieChgEvent(Float.parseFloat(caloriesValue), "步数值更新"));

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onBleDeviceStatusChg(BleConnectMessage message) {
        int status = message.getStatus();
        BleDevice bleDevice = message.getBleDevice();
        switch (status) {
            case 0://断开连接
                break;
            case 1://建立连接
                //---得到所有的Service
                BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
                List<BluetoothGattService> services = gatt.getServices();
                //---处理Service
                handleService(services, bleDevice);
                break;
        }
    }

    /**
     * 处理ble服务
     *
     * @param services
     */
    private void handleService(List<BluetoothGattService> services, BleDevice bleDevice) {

        for (BluetoothGattService service : services) {
            String serviceUUID = service.getUuid().toString().toLowerCase();
            //MyLog.e(TAG, "serviceUUID：" + serviceUUID);
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            if (serviceUUID.contains("aa70")) {//心电图service
                handleHeartService(characteristics, bleDevice);
            } else if (serviceUUID.contains("aa80")) {//动作service
                //handleMotionService(characteristics, bleDevice);
            } else if (serviceUUID.contains("180f")) {//电量service
                MyLog.e(TAG, "180f==" + serviceUUID);
                //handleBatteryService(characteristics, bleDevice);
            }
        }

    }

    /**
     * 处理电量service
     * <p>
     * F000180F-0451-4000-B000-000000000000
     *
     * @param characteristics
     * @param bleDevice
     */
    private void handleBatteryService(List<BluetoothGattCharacteristic> characteristics, BleDevice bleDevice) {
        for (final BluetoothGattCharacteristic characteristic : characteristics) {
            String characteristicUUID = characteristic.getUuid().toString().toLowerCase();
            if (characteristicUUID.contains("2A19")) {//

            }

        }
    }


    /**
     * 处理动作service
     * <p>
     * F000AA80-0451-4000-B000-000000000000
     *
     * @param characteristics
     */
    private void handleMotionService(final List<BluetoothGattCharacteristic> characteristics, final BleDevice bleDevice) {
        for (final BluetoothGattCharacteristic characteristic : characteristics) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String characteristicUUID = characteristic.getUuid().toString();
            if (characteristicUUID.contains("AA82") || characteristicUUID.contains("aa82")) {
                //发送01使设备处于工作状态 00 是休眠状态
                BleManager.getInstance().write(
                        bleDevice,
                        serviceUUID,
                        characteristicUUID,
                        HexUtil.hexStringToBytes("1"),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                startNotify();
                            }

                            @Override
                            public void onWriteFailure(final BleException exception) {
                                MyToast.showLong(getContext(), "AA82指令写入失败" + exception);
                                MyLog.e(TAG, "AA82指令写入失败" + exception);
                            }

                            //开启通知
                            private void startNotify() {
                                for (final BluetoothGattCharacteristic characteristic : characteristics) {
                                    String serviceUUID = characteristic.getService().getUuid().toString();
                                    String characteristicUUID = characteristic.getUuid().toString();
                                    if (characteristicUUID.contains("AA81") || characteristicUUID.contains("aa81")) {
                                        BleManager.getInstance().notify(
                                                bleDevice,
                                                serviceUUID,
                                                characteristicUUID,
                                                new BleNotifyCallback() {
                                                    @Override
                                                    public void onNotifySuccess() {
                                                        MyLog.d(TAG, "AA81通知开始成功===============");
                                                    }

                                                    @Override
                                                    public void onNotifyFailure(final BleException exception) {
                                                        MyLog.e(TAG, "AA81通知开始失败===============");
                                                    }

                                                    @Override
                                                    public void onCharacteristicChanged(byte[] data) {
                                                        Message message = new Message();
                                                        message.what = 1;
                                                        message.obj = characteristic.getValue();
                                                        mHandler.sendMessage(message);
                                                    }
                                                });
                                    }

                                }
                            }
                        });
            }

        }
    }

    /**
     * 处理心电图service
     * <p>
     * F000AA70-0451-4000-B000-000000000000
     *
     * @param characteristics
     */
    private void handleHeartService(final List<BluetoothGattCharacteristic> characteristics, final BleDevice bleDevice) {

        for (final BluetoothGattCharacteristic characteristic : characteristics) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String characteristicUUID = characteristic.getUuid().toString();
            if (characteristicUUID.contains("AA72") || characteristicUUID.contains("aa72")) {
                //发送01使设备处于工作状态 00 是休眠状态
                BleManager.getInstance().write(
                        bleDevice,
                        serviceUUID,
                        characteristicUUID,
                        HexUtil.hexStringToBytes("01"),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                startNotify();
                            }

                            @Override
                            public void onWriteFailure(final BleException exception) {
                                MyToast.showLong(getContext(), "AA72指令写入失败" + exception);
                                MyLog.e(TAG, "AA72指令写入失败" + exception);
                            }

                            //开启通知
                            private void startNotify() {
                                for (final BluetoothGattCharacteristic characteristic : characteristics) {
                                    String serviceUUID = characteristic.getService().getUuid().toString();
                                    String characteristicUUID = characteristic.getUuid().toString();
                                    if (characteristicUUID.contains("AA71") || characteristicUUID.contains("aa71")) {
                                        BleManager.getInstance().notify(
                                                bleDevice,
                                                serviceUUID,
                                                characteristicUUID,
                                                new BleNotifyCallback() {
                                                    @Override
                                                    public void onNotifySuccess() {
                                                        MyLog.d(TAG, "AA71通知开始成功===============");
                                                    }

                                                    @Override
                                                    public void onNotifyFailure(final BleException exception) {
                                                        MyLog.e(TAG, "AA71通知开始失败===============");
                                                    }

                                                    @Override
                                                    public void onCharacteristicChanged(byte[] data) {
                                                        Message message = new Message();
                                                        message.what = 0;
                                                        message.obj = characteristic.getValue();
                                                        mHandler.sendMessage(message);
                                                    }
                                                });
                                    }

                                }
                            }
                        });
            }

        }

    }

    @Override
    protected void afterInit() {
        super.afterInit();
        BleDevice currentBleDevice = application.getCurrentBleDevice();
        if (currentBleDevice != null &&
                BleManager.getInstance().isConnected(currentBleDevice)) {
            EventBus.getDefault().post(new BleConnectMessage(1, currentBleDevice));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        EventBus.getDefault().unregister(this);
        mAct.unbindService(stepServiceConnect);
    }
}
