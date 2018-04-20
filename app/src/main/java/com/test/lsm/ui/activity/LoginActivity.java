package com.test.lsm.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.UserBean;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.lsmalgorithm.MyLib;

import butterknife.BindView;

/**
 * 功能：登录界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class LoginActivity extends LsmBaseActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_user_tel)
    EditText etUserTel;
    @BindView(R.id.til_tel)
    TextInputLayout tilTel;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;
    private APIMethodManager apiMethodManager;
    private Gson mGson;
    private MyApplication application;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getApplication();
        apiMethodManager = APIMethodManager.getInstance();
        mGson = new Gson();
    }

    @Override
    protected void initView() {
        //LoginRegUtils.checkEdit(tilTel, LoginRegUtils.CheckType.tel);
        //LoginRegUtils.checkEdit(tilPwd, LoginRegUtils.CheckType.pwd);
    }

    @Override
    protected void initListener() {

    }

    public void toLogin(View view) {

        //double[] doubles1 = new double[]{50155,50175,50196,50219,50238,50252,50267,50278,50288,50296,50299,50299,50296,50285,50269,50250,50233,50211,50185,50161,50137,50115,50093,50072,50054,50041,50033,50026,50016,50005,49993,49982,49977,49975,49974,49973,49969,49965,49962,49957,49953,49955,49954,49947,49940,49940,49939,49934,49928,49919,49912,49906,49901,49900,49899,49896,49896,49894,49894,49892,49886,49881,49878,49879,49879,49879,49876,49874,49869,49862,49860,49857,49851,49846,49843,49845,49846,49839,49834,49831,49833,49834,49833,49826,49818,49807,49799,49790,49789,49802,49853,49963,50118,50268,50350,50259,49973,49636,49445,49429,49458,49473,49499,49560,49636,49691,49722,49748,49770,49779,49788,49795,49800,49807,49813,49820,49826,49833,49841,49845,49847,49848,49854,49860,49865,49872,49882,49893,49902,49910,49915,49928,49944,49962,49980,49997,50012,50031,50052,50069,50080,50093,50106,50115,50119,50116,50108,50097,50087,50076,50059,50040,50018,49994,49971,49948,49929,49912,49899,49890,49881,49871,49862,49856,49851,49845,49838,49834,49832,49827,49821,49817,49810,49804,49802,49802,49803,49801,49801,49799,49798,49797,49793,49788,49781,49778,49777,49781,49785,49782,49769,49757,49754,49755,49755,49754,49752,49750,49749,49748,49745,49744,49743,49740,49733,49729,49727,49730,49732,49729,49732,49732,49731, 49728,};
        //double[] doubles1 = new double[]{16901,16901,16902,16902,16902,16903,16903,16904,16904,16904,16904,16903,16903,16901,16901,16902,16903,16904,16907,16909,16910,16911,16911,16912,16913,16913,16913,16912,16910,16910,16910,16910,16910,16910,16911,16911,16911,16911,16911,16909,16904,16901,16900,16908,16922,16955,17028,17138,17264,17380,17444,17392,17254,17102,16995,16946,16928,16915,16905,16903,16910,16924,16937,16947,16953,16958,16962,16967,16972,16976,16979,16983,16986,16990,16993,16995,16999,17002,17007,17012,17017,17022,17026,17030,17036,17042,17049,17056,17063,17071,17080,17088,17098,17109,17118,17129,17141,17153,17167,17181,17196,17212,17229,17247,17265,17283,17302,17319,17335,17351,17366,17380,17393,17404,17413,17418,17419,17417,17412,17403,17389,17372,17350,17326,17302,17276,17252,17227,17203,17181,17161,17142,17125,17109,17095,17082,17071,17060,17052,17044,17037,17030,17027,17022,17017,17014,17010,17007,17006,17004,17002,17000,16998,16996,16995,16995,16994,16994,16992,16991,16989,16988,16987,16986,16985,16983,16982,16980,16978,16976,16974,16972,16970,16968,16965,16964,16963,16961,16960,16959,16958,16955,16955,16953,16951,16949,16947,16947,16946,16944,16944,16943,16943,16942,16942,16941,16939,16938,16936,16935,16936,16936,16936,16935,16935,16935,16935,16933,16933,16933,16933,16934,16933,16933,16933,16931,16930,16930,16929,16930,16929,16930,16930,16930,16930,16930,16930,16929,16928,16927,16926,16925,16924,16924,16923,16923,16923,16923,16923,16923,16922,16921,16919,16919,16919,16918,16919,16919,16921,16921,16920,16920,16919,16919,16919,16919,16920,16921,16918,16917,16917,16915,16915,16916,16917,16916,16914,16914,16914,16914,16913,16911,16911,16911,16911,16910,16910,16910,16908,16908,16907,16905,16904,16903,16901,16898,16897,16897,16898,16899,16899,16899,16897,16897,16899,16898,16900,16901,16900,16898,16897,16897,16897,16896,16896,16897,16896,16896,16896,16896,16895,16894,16890,16885,16888,16901,16921,16971,17063,17184,17304,17400,17401,17295,17141,17009,16936,16903,16884,16871,16865,16872};
        //double[] doubles1 = new double[]{57895,57850,57816,57789,57762,57734,57705,57676,57658,57651,57640,57621,57610,57603,57591,57575,57570,57571,57563,57557,57554,57549,57541,57536,57534,57528,57526,57526,57520,57516,57511,57506,57500,57483,57468,57465,57470,57479,57475,57462,57447,57432,57423,57420,57421,57425,57427,57423,57414,57403,57400,57403,57406,57404,57403,57404,57399,57389,57379,57375,57374,57370,57363,57356,57353,57354,57353,57355,57357,57355,57354,57354,57345,57328,57313,57307,57318,57345,57421,57590,57817,58037,58165,58044,57643,57179,56917,56897,56945,56977,57001,57051,57128,57198,57243,57272,57290,57304,57317,57317,57318,57321,57326,57336,57347,57349,57350,57349,57347,57347,57353,57359,57365,57375,57387,57401,57415,57435,57457,57482,57505,57524,57544,57568,57593,57618,57643,57660,57673,57677,57679,57680,57676,57666,57654,57637,57614,57588,57558,57530,57503,57474,57446,57424,57403,57382,57362,57343,57329,57319,57310,57304,57301,57302,57301,57299,57291,57282,57272,57265,57262,57259,57256,57252,57248,57243,57241,57240,57238,57234,57229,57219,57211,57203,57195,57191,57188,57184,57183,57179,57177,57172,57172,57172,57171,57163,57150,57145,57148,57145,57142,57143,57141,57137,57130,57126,57124,57121,57116,57110,57110,57115,57121,57125,57127,57127,57127,57131,57131,57123,57111,57090,57064,57048,57059,57093,57196,57392,57639,57865,57954,57767,57330,56885,56680,56676,56713,56733,56773,56850,56935,57000,57038,57066,57087,57100,57107,57113,57119,57124,57132,57135,57141,57150,57159,57166,57169,57167,57169,57183,57200,57215,57227,57244,57266,57282,57295,57310,57324,57338,57356,57375,57398,57419,57437,57452,57464,57471,57473,57473,57466,57457,57449,57441,57425,57406,57382,57355,57326,57302,57280,57259,57236,57214,57200,57192,57183,57178,57171,57163,57163,57164,57162,57159,57155,57151,57149,57146,57150,57156,57155,57151,57148,57144,57139,57133,57134,57129,57125,57121,57113,57105,57102,57100,57093,57087,57083,57081,57080,57078,57078,57079,57082,57084,57079,57063,57047,57039,57040,57041,57041,57040,57039,57038,57036,57034,57032,57027,57020,57019,57020,57019,57020,57023,57027,57031,57033,57037,57038,57032,57018,56995,56971,56967,56997,57075,57235,57465,57701,57861,57798,57445,56994,56708,56650,56682,56706,56727,56777,56854,56930,56982,57014,57031,57039,57043,57046,57050,57056,57060,57063,57067,57074,57082,57086,57091,57100,57110,57119,57123,57129,57140,57150};

/*        for (double d : doubles1){
            int i1 = MyLib.countHeartRate(d);
            if (i1>0){
                MyLog.e(TAG , "==countHeartRate==="+i1);
            }
        }*/

        String tel = etUserTel.getText().toString();
        String pwd = etUserPwd.getText().toString();

        showLoadDialog("登录中....");

        apiMethodManager.login(tel, pwd, new IRequestCallback<UserLoginReturn>() {
            @Override
            public void onSuccess(UserLoginReturn result) {
                String code = result.getResult();
               if ("01".equals(code)){//登录成功
                   UserLoginReturn.PdBean pd = result.getPd();
                   String pdStr = mGson.toJson(pd);
                   //保存用户信息
                   MySPUtils.put(LoginActivity.this , LoginRegUtils.USER_INFO , pdStr);
                   application.setUser(LoginRegUtils.getLoginUser(LoginActivity.this));
                   MainActivity.startAction(LoginActivity.this);
                   finish();
               }else /*if("07".equals(code))*/{//用户名密码错误
                   MyToast.showShort(LoginActivity.this, "用户名或密码错误");
               }
                hiddenLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                hiddenLoadDialog();
                MyToast.showShort(LoginActivity.this, "网络异常："+throwable.getMessage());
            }
        });

    }

    public void toRegister(View view) {
        RegisterActivity1.startAction(this);
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
