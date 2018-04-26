package com.test.lsm.ui.activity;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.utils.LoginRegUtils;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * 功能：启动页
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class SplashActivity extends LsmBaseActivity {

    private static final String TAG = "SplashActivity";

    @BindView(R.id.iv_splash)
    ImageView iv_splash;
    private MyApplication application;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getApplication();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void afterInit() {
        super.afterInit();

        toConnectLsm();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anime);
        iv_splash.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               /* if(LoginRegUtils.isLogin(SplashActivity.this)){//登录了
                    UserLoginReturn.PdBean loginUser = LoginRegUtils.getLoginUser(SplashActivity.this);
                    ((MyApplication)getApplication()).setUser(loginUser);
                    MainActivity.startAction(SplashActivity.this);
                }else{
                    LoginActivity.startAction(SplashActivity.this);
                }*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 连接ble设备
     *
     * @return
     */
    private void toConnectLsm() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描（主线程）
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String dName = bleDevice.getDevice().getName();
                String mac = bleDevice.getMac();
                if (dName != null) {
                    if (dName.contains("CC") || dName.contains("Sensor") || dName.contains("Tag")) {
                        String connectDeviceMac = BleBTUtils.getConnectDevice(SplashActivity.this);
                        if (!TextUtils.isEmpty(mac) && mac.equals(connectDeviceMac)) {//已经配对过的设备
                            BleManager.getInstance().connectWapper(bleDevice, new BleGattCallback() {
                                @Override
                                public void onStartConnect() {
                                    MyLog.d(TAG, "onStartConnect===");
                                }

                                @Override
                                public void onConnectFail(BleException exception) {
                                    MyLog.e(TAG, "onConnectFail===" + exception.getDescription());
                                    //TODO 蓝牙连接失败处理
                                }

                                @Override
                                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                                    application.setCurrentBleDevice(bleDevice);
                                    resolveSkip();
                                    MyLog.d(TAG, "onConnectSuccess===");
                                }

                                @Override
                                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                                    MyLog.d(TAG, "onDisConnected===");
                                }
                            });
                        }
                    }
                }
                // 扫描到一个符合扫描规则的BLE设备（主线程）
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                BleDevice currentBleDevice = application.getCurrentBleDevice();
                if (currentBleDevice == null || !BleManager.getInstance().isConnected(currentBleDevice)) {//没有连接上设备
                    resolveSkip();
                    MyToast.showLong(SplashActivity.this, "蓝牙设备连接失败，请在主界面再次连接！");
                }
                // 扫描结束，列出所有扫描到的符合扫描规则的BLE设备（主线程）
                MyLog.e(TAG, "扫描完成==");
            }
        });
    }

    private void resolveSkip() {
        if (LoginRegUtils.isLogin(SplashActivity.this)) {//登录了
            UserLoginReturn.PdBean loginUser = LoginRegUtils.getLoginUser(SplashActivity.this);
            ((MyApplication) getApplication()).setUser(loginUser);
            MainActivity.startAction(SplashActivity.this);
        } else {
            LoginActivity.startAction(SplashActivity.this);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
