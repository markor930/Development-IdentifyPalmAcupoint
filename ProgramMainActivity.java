package org.nutc.thesis.development_identifypalmacupoint;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class ProgramMainActivity extends Activity implements CvCameraViewListener2, OnTouchListener{

	private boolean mIsColorSelected = false;
	private boolean StartOnTounch = true; 
	private boolean BackHandDetection = false;
	private boolean FrontHandDetection = false;
	private boolean ShowAupoints = false;
	private int BackOrFront = 0;
	private int CheckPress = 0;
	private int AcupointID = 0;
	
	//private boolean ShowInfo = false;
	//private boolean click = true;
	
	private CameraBridgeViewBase  mOpenCvCameraView;
	private ColorBlobDetector mDetector;
	
	private Mat mRgba;
	private Mat mSpectrum;

	private Scalar mBlobColorHsv;
	private Scalar ContourColor; //輪廓顏色
    //private Scalar AreaColor; //區域顏色
    //private Scalar HullColor; //包覆實體顏色(Hull:殼)    
    //private Scalar DefectPointColor; //輪廓顏色
    //private Scalar StartPointColor; //輪廓顏色
    //private Scalar EndPointColor; //輪廓顏色
    
    private Size SpectrumSize; //頻譜宣告：穩定ROI之大小    
    //private Rect boundRect;
    
    private MatOfPoint thisContour;
    private MatOfPoint2f approxContour1;
    private MatOfPoint2f approxContour2;
        
    //private Point start;
    //private Point end;
    //private Point defect;

    /**指尖*/
    private Point fThumbTip;
    private Point fIndexTip;
    private Point fMiddleTip;    
    private Point fRingTip;    
    private Point fLittleTip;
    
    private Point bThumbTip;
    private Point bIndexTip;
    private Point bMiddleTip;
    private Point bRingTip;
    private Point bLittleTip;
        
    /**指谷*/
    private Point Thu_Palm; //0
    private Point Thu_ThuL; //1
    private Point Thu_ThuR; //2
    private Point Thu_Ind; //3
    private Point Ind_Ind; //4
    private Point Ind_Mid; //5
    private Point Mid_Rin; //6
    private Point Rin_Lit; //7
    private Point Lit_Lit; //8 
    
    //private Point bThu_Thu;
    private Point bThu_ThuL;
    private Point bThu_ThuR;
    private Point bThu_Ind;                
    private Point bInd_Ind;
    private Point bInd_Mid;
    private Point bMid_Rin;
    private Point bRin_Lit;
    private Point bLit_Lit;
    private Point bLit_Palm;
    
    /**手指底端點*/
    private Point ThumbDip;
    private Point IndexDip;
    private Point MiddleDip;
    private Point RingDip;
    private Point LittleDip;     
    
    private Point bThumbDip;
    private Point bIndexDip; 
    private Point bMiddleDip;
    private Point bRingDip;
    private Point bLittleDip;
    
    /**手指中心點*/
    //private Point ThumbCenter;
    private Point IndexCenter;
    private Point MiddleCenter;
    private Point RingCenter;
    private Point LittleCenter;
    
    private Point bThumbCenter;
    private Point bIndexCenter;
    private Point bMiddleCenter;
    private Point bRingCenter;
    private Point bLittleCenter;
    
    //指尖與兩指谷連成線段之中點，共兩點
    //private Point ThuLeftLineCenter;
    //private Point ThuRightLineCenter; 
    private Point IndLeftLineCenter;
    private Point IndRightLineCenter;    
    private Point MidLeftLineCenter;
    private Point MidRightLineCenter;   
    private Point RinLeftLineCenter;
    private Point RinRightLineCenter;   
    private Point LitLeftLineCenter;
    private Point LitRightLineCenter;
    
    private Point bIndLeftLineCenter;
    private Point bIndRightLineCenter;
    private Point bMidLeftLineCenter;
    private Point bMidRightLineCenter;
    private Point bRinLeftLineCenter;
    private Point bRinRightLineCenter;
    private Point bLitLeftLineCenter;
    private Point bLitRightLineCenter;

    /**第二指節*/
    private Point IndFingerGravity;
    private Point IndKnucklesDown;    
    private Point MidFingerGravity;
    private Point MidKnucklesDown;  
    private Point RinFingerGravity;
    private Point RinKnucklesDown;    
    private Point LitFingerGravity;
    private Point LitKnucklesDown;
    
    private Point bIndFingerGravity;
    private Point bIndKnucklesDown;
    private Point bMidFingerGravity;
    private Point bMidKnucklesDown;
    private Point bRinFingerGravity;
    private Point bRinKnucklesDown;
    private Point bLitFingerGravity;
    private Point bLitKnucklesDown;
    
    /**第一指節*/
    private Point ThuKnucklesTop;
    private Point IndKnucklesTop;
    private Point MidKnucklesTop;         
    private Point RinKnucklesTop;
    private Point Litfingergravity;
    private Point LitKnucklesTop;
    
    //private Point bThuKnucklesTop;
    private Point bIndKnucklesTop;
    private Point bMidKnucklesTop;         
    private Point bRinKnucklesTop;
    private Point bLitfingergravity;
    private Point bLitKnucklesTop;
    //Back Finger Knuckles Top 參考-根據點
    private Point bIndKnucklesTopTP;
    private Point bIndKnucklesTopDP;
    private Point bMidKnucklesTopTP;
    private Point bMidKnucklesTopDP;
    private Point bRinKnucklesTopTP;
    private Point bRinKnucklesTopDP;
    
    private Point BackPalmCenter; //掌心
    private Point FrontPalmCenter; //掌心
    
    private LinearLayout mainLayout;
    private RelativeLayout Relayout;
    private PopupWindow Popup;
    //private Builder dialog;
    //private RelativeLayout Relayout;              
    //private LinearLayout.LayoutParams LP;
    
    
    private TextView AcuPointsName;
    private TextView AcuPointsBelongs;
    private TextView AcuPointsKind;
    private TextView AcuPointsOrgans;
    private TextView AcuPointsEffect;
    private TextView AcuPointsCure;
    
    //private ImageView ImageInfo; 
    private ImageView ImageKind;
    private ImageView ImageOrgans;
    private WebView GifImage;
    
    private Button BackHandBtn;
    private Button FrontHandBtn;
    private Button ShowInfoBtn;
        
    // Acupoint Button
    private Button Btn_Shaoshang; //1
    private Button Btn_Thenar; //2
    private Button Btn_Taiyuan; //3
    private Button Btn_DaLing; //4
    private Button Btn_Laogong; //5
    private Button Btn_Zhongchong; //6
    private Button Btn_Shenmen; //7
    private Button Btn_ShaoFu; //8
    
    private Button Btn_Shaochong; //1
    private Button Btn_Shangyang; //2  
    private Button Btn_Erjian; //3
    private Button Btn_Sanjian; //4
    private Button Btn_Hoku; //5
    private Button Btn_Yangxi; //6
    private Button Btn_Yanggu; //7
    private Button Btn_Carpus; //8
    private Button Btn_Houxi; //9
    private Button Btn_Qiangu; //10
    private Button Btn_Shaoze; //11
    private Button Btn_Guanchong; //12
    private Button Btn_Yimen; //13
    private Button Btn_Zhongzhuu; //14
    private Button Btn_Yangchi; //15
    
    // Set Acupoint Button XY_Position
    private float IBShaoshangX = 0;
    private float IBShaoshangY = 0;
    private float IBThenarX = 0;
    private float IBThenarY = 0;
    private float IBTaiyuanX = 0;
    private float IBTaiyuanY = 0;
    private float IBDaLingX = 0;
    private float IBDaLingY = 0;
    private float IBLaogongX = 0;
    private float IBLaogongY = 0;
    private float IBZhongchongX = 0;
    private float IBZhongchongY = 0;
    private float IBShenmenX = 0;
    private float IBShenmenY = 0;
    private float IBShaoFuX = 0;
    private float IBShaoFuY = 0;
    
    private float IBShaochongX = 0;
    private float IBShaochongY = 0;
    private float IBShangyangX = 0;
    private float IBShangyangY = 0;
    private float IBErjianX = 0;
    private float IBErjianY = 0;
    private float IBSanjianX = 0;
    private float IBSanjianY = 0;
    private float IBHokuX = 0;
    private float IBHokuY = 0;
    private float IBYangxiX = 0;
    private float IBYangxiY = 0;
    private float IBYangguX = 0;
    private float IBYangguY = 0;
    private float IBCarpusX = 0;
    private float IBCarpusY = 0;
    private float IBHouxiX = 0;
    private float IBHouxiY = 0;
    private float IBQianguX = 0;
    private float IBQianguY = 0;
    private float IBShaozeX = 0;
    private float IBShaozeY = 0;
    private float IBGuanchongX = 0;
    private float IBGuanchongY = 0;
    private float IBYimenX = 0;
    private float IBYimenY = 0;
    private float IBZhongzhuuX = 0;
    private float IBZhongzhuuY = 0;
    private float IBYangchiX = 0;
    private float IBYangchiY = 0;
    
    private Animation shake;
    
    /*private static final int differenceX = 160;
    private static final int differenceY = 15;*/
    
    private Handler mHandler;
  
    /**Initialize and Loader Callback OpenCV4Android version*/
    @SuppressLint("ClickableViewAccessibility") private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                	//open java_camera view
                    mOpenCvCameraView.enableView();
                    //Touch Screen
                    mOpenCvCameraView.setOnTouchListener(ProgramMainActivity.this);
                    
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
	
    /**main scripting 系統初始化 - 只執行一次*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /**跳出 - 視窗*/
        Relayout = new RelativeLayout(this); //覆蓋一層新的框架
        Resources res = this.getResources();
        Relayout.setBackground(res.getDrawable(R.drawable.customborder));
        
        /*// LinearLayout 分隔線 顯示
        layout.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);   
        layout.setDividerDrawable(res.getDrawable(R.drawable.ic_launcher));*/

        Popup = new PopupWindow(this); //新增彈出視窗  
        Popup.setContentView(Relayout); //連結要顯示的框架
        Popup.setFocusable(true);
        Popup.setOutsideTouchable(true);
        Popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(150, 0, 0, 0))); //透明
        //Popup.setBackgroundDrawable(new BitmapDrawable()); //透明
        
        //新增Text在layout的框架中
        AcuPointsName = new TextView(this); 
        AcuPointsName.setY(330);
        AcuPointsName.setRotation(270);
        AcuPointsName.setTextSize(20);
        AcuPointsName.setTypeface(null, Typeface.BOLD);
        AcuPointsName.setTextColor(Color.rgb(255, 255, 255));
        Relayout.addView(AcuPointsName, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        AcuPointsBelongs = new TextView(this);       
        AcuPointsBelongs.setX(105);
        AcuPointsBelongs.setY(350);
        AcuPointsBelongs.setRotation(270);
        AcuPointsBelongs.setTextColor(Color.RED);
        AcuPointsBelongs.setTextSize(10);
        Relayout.addView(AcuPointsBelongs, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        AcuPointsKind = new TextView(this);
        AcuPointsKind.setText("類別：");
        AcuPointsKind.setX(180);
        AcuPointsKind.setY(530);
        AcuPointsKind.setRotation(270);
        AcuPointsKind.setTextColor(Color.rgb(255, 255, 255));
        Relayout.addView(AcuPointsKind, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        AcuPointsOrgans = new TextView(this);
        AcuPointsOrgans.setText("臟腑：");
        AcuPointsOrgans.setX(180);
        AcuPointsOrgans.setY(250);
        AcuPointsOrgans.setRotation(270);
        AcuPointsOrgans.setTextColor(Color.rgb(255, 255, 255));
        Relayout.addView(AcuPointsOrgans, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        AcuPointsEffect = new TextView(this);
        AcuPointsEffect.setX(100);
        AcuPointsEffect.setY(320);
        AcuPointsEffect.setRotation(270);
        AcuPointsEffect.setTextColor(Color.rgb(255, 240, 100));
        Relayout.addView(AcuPointsEffect, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        AcuPointsCure = new TextView(this);
        AcuPointsCure.setX(300);
        AcuPointsCure.setY(355);
        AcuPointsCure.setRotation(270);
        AcuPointsCure.setTextColor(Color.rgb(255, 240, 100));
        Relayout.addView(AcuPointsCure, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        ImageKind = new ImageView(this);
        ImageKind.setX(180);
        ImageKind.setY(400);
        ImageKind.setRotation(270); 
        ImageKind.setImageResource(R.drawable.ic_launcher);
        ImageKind.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Relayout.addView(ImageKind, new LayoutParams(120, 120));
        
        ImageOrgans = new ImageView(this);
        ImageOrgans.setX(180);
        ImageOrgans.setY(120);
        ImageOrgans.setRotation(270); 
        ImageOrgans.setImageResource(R.drawable.ic_launcher);
        ImageOrgans.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Relayout.addView(ImageOrgans, new LayoutParams(120, 120));
        
        /*ImageInfo = new ImageView(this);
        ImageInfo.setX(630);
        ImageInfo.setY(100);
        ImageInfo.setRotation(270); 
        ImageInfo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Relayout.addView(ImageInfo, new LayoutParams(550, 550));*/
        
        GifImage = new WebView(this);
        GifImage.setX(680);
        GifImage.setY(100);
        GifImage.setRotation(270);
        GifImage.getSettings().setUseWideViewPort(true); //設定寬度為view的最大寬度
        GifImage.getSettings().setLoadWithOverviewMode(true);//大於viewport會縮放置view的大小     
        GifImage.setBackgroundColor(0x00000000); //背景透明
        Relayout.addView(GifImage, new LayoutParams(390, 550));
                                  
        //get android screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_program_main);
        
        //start Java_Camera
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.hd_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        
        /**動態生成元件*/
        mainLayout = new LinearLayout(this);
        this.addContentView(mainLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        BackHandBtn = new Button(this);    
        FrontHandBtn = new Button(this);
        ShowInfoBtn = new Button(this);
               
        BackHandBtn.setId(0);
        FrontHandBtn.setId(1);
        ShowInfoBtn.setId(2);
        
        FrontHandBtn.setY(800);       
        ShowInfoBtn.setX(-120);
        ShowInfoBtn.setY(180);
       
        BackHandBtn.setText("Back");
        FrontHandBtn.setText("Front"); 
        ShowInfoBtn.setText("Show Aucupoints");

   		BackHandBtn.setRotation(270); 
   		FrontHandBtn.setRotation(270); 
   		ShowInfoBtn.setRotation(270);  
   		
   		mainLayout.addView(BackHandBtn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
   		mainLayout.addView(FrontHandBtn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
   		mainLayout.addView(ShowInfoBtn, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        GetAcupointButton();
    	setAcupointButton();
    	
    	shake = AnimationUtils.loadAnimation(this, R.anim.shake); 
    	
    	//Timer
        mHandler = new Handler();
        mHandler.post(runnable);
    }
    
    public void GetAcupointButton() {
    	
    	Btn_Shaoshang = new Button(this);
        Btn_Thenar = new Button(this);
        Btn_Taiyuan = new Button(this);
        Btn_DaLing = new Button(this);
        Btn_Laogong = new Button(this);
        Btn_Zhongchong = new Button(this);
        Btn_Shenmen = new Button(this);
        Btn_ShaoFu = new Button(this);
        
        Btn_Shaochong = new Button(this);
        Btn_Shangyang = new Button(this);
        Btn_Erjian = new Button(this);
        Btn_Sanjian = new Button(this);
        Btn_Hoku = new Button(this);
        Btn_Yangxi = new Button(this);
        Btn_Yanggu = new Button(this);
        Btn_Carpus = new Button(this);
        Btn_Houxi = new Button(this);
        Btn_Qiangu = new Button(this);
        Btn_Shaoze = new Button(this);
        Btn_Guanchong = new Button(this);
        Btn_Yimen = new Button(this);
        Btn_Zhongzhuu = new Button(this);
        Btn_Yangchi = new Button(this);
        
        Btn_Shaoshang.setId(11);
        Btn_Thenar.setId(12);
        Btn_Taiyuan.setId(13);
        Btn_DaLing.setId(14);
    	Btn_Laogong.setId(15);
    	Btn_Zhongchong.setId(16);
    	Btn_Shenmen.setId(17);
    	Btn_ShaoFu.setId(18);
    	
    	Btn_Shaochong.setId(21);
        Btn_Shangyang.setId(22);
        Btn_Erjian.setId(23);
        Btn_Sanjian.setId(24);
        Btn_Hoku.setId(25);
        Btn_Yangxi.setId(26);
        Btn_Yanggu.setId(27);
        Btn_Carpus.setId(28);
        Btn_Houxi.setId(29);
        Btn_Qiangu.setId(210);
        Btn_Shaoze.setId(211);
        Btn_Guanchong.setId(212);
        Btn_Yimen.setId(213);
        Btn_Zhongzhuu.setId(214);
        Btn_Yangchi.setId(215);
    	
    	Btn_Shaoshang.setRotation(270);  
    	Btn_Thenar.setRotation(270); 
    	Btn_Taiyuan.setRotation(270); 
    	Btn_DaLing.setRotation(270); 
    	Btn_Laogong.setRotation(270); 
    	Btn_Zhongchong.setRotation(270); 
    	Btn_Shenmen.setRotation(270); 
    	Btn_ShaoFu.setRotation(270); 
    	
    	Btn_Shaochong.setRotation(270);
        Btn_Shangyang.setRotation(270);
        Btn_Erjian.setRotation(270);
        Btn_Sanjian.setRotation(270);
        Btn_Hoku.setRotation(270);
        Btn_Yangxi.setRotation(270);
        Btn_Yanggu.setRotation(270);
        Btn_Carpus.setRotation(270);
        Btn_Houxi.setRotation(270);
        Btn_Qiangu.setRotation(270);
        Btn_Shaoze.setRotation(270);
        Btn_Guanchong.setRotation(270);
        Btn_Yimen.setRotation(270);
        Btn_Zhongzhuu.setRotation(270);
        Btn_Yangchi.setRotation(270);

    	Btn_Shaoshang.setText("少商穴");
    	Btn_Thenar.setText("魚際穴");
    	Btn_Taiyuan.setText("太淵穴");
    	Btn_DaLing.setText("大陵穴");
        Btn_Laogong.setText("勞宮穴");
        Btn_Zhongchong.setText("中衝穴");
        Btn_Shenmen.setText("神門穴");
        Btn_ShaoFu.setText("少府穴");

        Btn_Shaochong.setText("少衝穴");
        Btn_Shangyang.setText("商陽穴");
        Btn_Erjian.setText("二間穴");
        Btn_Sanjian.setText("三間穴");
        Btn_Hoku.setText("合谷穴");
        Btn_Yangxi.setText("陽谿穴");
        Btn_Yanggu.setText("陽谷穴");
        Btn_Carpus.setText("腕骨穴");
        Btn_Houxi.setText("後谿穴");
        Btn_Qiangu.setText("前谷穴");
        Btn_Shaoze.setText("少澤穴");
        Btn_Guanchong.setText("關衝穴");
        Btn_Yimen.setText("液門穴");
        Btn_Zhongzhuu.setText("中渚穴");
        Btn_Yangchi.setText("陽池穴");
        
        /*Btn_DaLing.setTextColor(Color.rgb(255, 20, 150));
        Btn_Laogong.setTextColor(Color.rgb(255, 20, 150));
        Btn_Zhongchong.setTextColor(Color.rgb(255, 20, 150));
        Btn_Shenmen.setTextColor(Color.rgb(140, 0, 0));
        Btn_ShaoFu.setTextColor(Color.rgb(140, 0, 0));
        
        Btn_Shaochong.setTextColor(Color.rgb(140, 0, 0));
        Btn_Shangyang.setTextColor(Color.rgb(50, 200, 50));
        Btn_Erjian.setTextColor(Color.rgb(50, 200, 50));
        Btn_Sanjian.setTextColor(Color.rgb(50, 200, 50));
        Btn_Hoku.setTextColor(Color.rgb(50, 200, 50));
        Btn_Yangxi.setTextColor(Color.rgb(110, 140, 50));
        Btn_Yanggu.setTextColor(Color.rgb(110, 140, 50));
        Btn_Carpus.setTextColor(Color.rgb(110, 140, 50));
        Btn_Houxi.setTextColor(Color.rgb(110, 140, 50));
        Btn_Qiangu.setTextColor(Color.rgb(110, 140, 50));
        Btn_Shaoze.setTextColor(Color.rgb(110, 140, 50));
        Btn_Guanchong.setTextColor(Color.rgb(150, 50, 225));
        Btn_Yimen.setTextColor(Color.rgb(150, 50, 225));
        Btn_Zhongzhuu.setTextColor(Color.rgb(150, 50, 225));
        Btn_Yangchi.setTextColor(Color.rgb(150, 50, 225));*/

        Btn_Shaoshang.setTextSize(10);
    	Btn_Thenar.setTextSize(10);
    	Btn_Taiyuan.setTextSize(10);
    	Btn_DaLing.setTextSize(10);
        Btn_Laogong.setTextSize(10);
        Btn_Zhongchong.setTextSize(10);
        Btn_Shenmen.setTextSize(10);
        Btn_ShaoFu.setTextSize(10);
        
        Btn_Shaochong.setTextSize(10);
        Btn_Shangyang.setTextSize(10);
        Btn_Erjian.setTextSize(10);
        Btn_Sanjian.setTextSize(10);
        Btn_Hoku.setTextSize(10);
        Btn_Yangxi.setTextSize(10);
        Btn_Yanggu.setTextSize(10);
        Btn_Carpus.setTextSize(10);
        Btn_Houxi.setTextSize(10);
        Btn_Qiangu.setTextSize(10);
        Btn_Shaoze.setTextSize(10);
        Btn_Guanchong.setTextSize(10);
        Btn_Yimen.setTextSize(10);
        Btn_Zhongzhuu.setTextSize(10);
        Btn_Yangchi.setTextSize(10);

        Btn_Shaoshang.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    	Btn_Thenar.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    	Btn_Taiyuan.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    	Btn_DaLing.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Laogong.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Zhongchong.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Shenmen.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_ShaoFu.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        
        Btn_Shaochong.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Shangyang.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Erjian.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Sanjian.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Hoku.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Yangxi.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Yanggu.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Carpus.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Houxi.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Qiangu.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Shaoze.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Guanchong.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Yimen.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Zhongzhuu.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Btn_Yangchi.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);

        //背景透明
        Btn_Shaoshang.setBackground(null);
    	Btn_Thenar.setBackground(null);
    	Btn_Taiyuan.setBackground(null);
    	Btn_DaLing.setBackground(null);
        Btn_Laogong.setBackground(null);
        Btn_Zhongchong.setBackground(null);
        Btn_Shenmen.setBackground(null);
        Btn_ShaoFu.setBackground(null);
        
        Btn_Shaochong.setBackground(null);
        Btn_Shangyang.setBackground(null);
        Btn_Erjian.setBackground(null);
        Btn_Sanjian.setBackground(null);
        Btn_Hoku.setBackground(null);
        Btn_Yangxi.setBackground(null);
        Btn_Yanggu.setBackground(null);
        Btn_Carpus.setBackground(null);
        Btn_Houxi.setBackground(null);
        Btn_Qiangu.setBackground(null);
        Btn_Shaoze.setBackground(null);
        Btn_Guanchong.setBackground(null);
        Btn_Yimen.setBackground(null);
        Btn_Zhongzhuu.setBackground(null);
        Btn_Yangchi.setBackground(null);
    	  
        mainLayout.addView(Btn_Shaoshang, 160,100);  
        mainLayout.addView(Btn_Thenar, 160,100);
        mainLayout.addView(Btn_Taiyuan, 160,100);
        mainLayout.addView(Btn_DaLing, 160,100);
        mainLayout.addView(Btn_Laogong, 160,100);
        mainLayout.addView(Btn_Zhongchong, 160,100);
        mainLayout.addView(Btn_Shenmen, 160,100);
        mainLayout.addView(Btn_ShaoFu, 160,100);
        
        mainLayout.addView(Btn_Shaochong, 160,100);
        mainLayout.addView(Btn_Shangyang, 160,100);      
        mainLayout.addView(Btn_Erjian, 160,100);  
        mainLayout.addView(Btn_Sanjian, 160,100);
        mainLayout.addView(Btn_Hoku, 160,100);
        mainLayout.addView(Btn_Yangxi, 160,100);
        mainLayout.addView(Btn_Yanggu, 160,100);
        mainLayout.addView(Btn_Carpus, 160,100);
        mainLayout.addView(Btn_Houxi, 160,100);
        mainLayout.addView(Btn_Qiangu, 160,100);
        mainLayout.addView(Btn_Shaoze, 160,100);
        mainLayout.addView(Btn_Guanchong, 160,100);
        mainLayout.addView(Btn_Yimen, 160,100);
        mainLayout.addView(Btn_Zhongzhuu, 160,100);
        mainLayout.addView(Btn_Yangchi, 160,100);      
    }
    
    public void setAcupointButton() {
    	
    	BackHandBtn.setOnClickListener(ButtonListener);
    	FrontHandBtn.setOnClickListener(ButtonListener);
    	ShowInfoBtn.setOnClickListener(ButtonListener);
    	
    	Btn_Shaoshang.setOnClickListener(ButtonListener);
    	Btn_Thenar.setOnClickListener(ButtonListener);
    	Btn_Taiyuan.setOnClickListener(ButtonListener);
    	Btn_DaLing.setOnClickListener(ButtonListener);
    	Btn_Laogong.setOnClickListener(ButtonListener);
    	Btn_Zhongchong.setOnClickListener(ButtonListener);
    	Btn_Shenmen.setOnClickListener(ButtonListener);
    	Btn_ShaoFu.setOnClickListener(ButtonListener);
    	
    	Btn_Shaochong.setOnClickListener(ButtonListener);
        Btn_Shangyang.setOnClickListener(ButtonListener);
        Btn_Erjian.setOnClickListener(ButtonListener);
        Btn_Sanjian.setOnClickListener(ButtonListener);
        Btn_Hoku.setOnClickListener(ButtonListener);
        Btn_Yangxi.setOnClickListener(ButtonListener);
        Btn_Yanggu.setOnClickListener(ButtonListener);
        Btn_Carpus.setOnClickListener(ButtonListener);
        Btn_Houxi.setOnClickListener(ButtonListener);
        Btn_Qiangu.setOnClickListener(ButtonListener);
        Btn_Shaoze.setOnClickListener(ButtonListener);
        Btn_Guanchong.setOnClickListener(ButtonListener);
        Btn_Yimen.setOnClickListener(ButtonListener);
        Btn_Zhongzhuu.setOnClickListener(ButtonListener);
        Btn_Yangchi.setOnClickListener(ButtonListener);
    }
    
    private OnClickListener ButtonListener = new OnClickListener() {
    	
    	   @Override
    	   public void onClick(View view) {
    		   
    		   //TODO Auto-generated method stub 
    		   //透過id指定button到new button
    		   Button Bufferbtn = (Button)findViewById(AcupointID);
    		   Bufferbtn.setTextColor(Color.rgb(0, 0, 0));
    		   Bufferbtn.clearAnimation();
    		   
    		   if(CheckPress == 1)
    		   {
    			   Popup.update(-100, 0, 1200, 800);
        		   Popup.showAtLocation(Relayout, Gravity.CENTER, 0, 0);  
        		   view.clearAnimation();
        		   CheckPress = 2;
    		   }    		   

    		   switch (view.getId()) {
   	        	case 0:
   	        		if(BackOrFront != 2)
   	        		{
   	        			ShowAupoints = false;
   	        			BackOrFront = 2;
   	        		}
   	        		
   	        		BackHandDetection = true;
   	        		FrontHandDetection = false;
   	        		Popup.update(0, 0, 0, 0);
   	        		CheckPress = 0;
   	        		AcupointID = 0;
   	        		break;
   	        	case 1:
   	        		if(BackOrFront != 1)
   	        		{
   	        			ShowAupoints = false;
   	        			BackOrFront = 1;
   	        		}
   	        		
   	        		BackHandDetection = false;
   	        		FrontHandDetection = true;
   	        		Popup.update(0, 0, 0, 0);
   	        		CheckPress = 0;
   	        		AcupointID = 0;
   	        		break;
   	        	case 2:  	        		
   	        		if(ShowAupoints)
   	        		{
   	        			ShowAupoints = false;
   	        		}
   	        		else
   	        		{
   	        			ShowAupoints = true;
   	        		}
   	        		
   	        		Popup.update(0, 0, 0, 0);
   	        		view.clearAnimation();
   	        		CheckPress = 0;
   	        		AcupointID = 0;
   	        		break;
   	        	case 11:
   	        		if(AcupointID != 11)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Shaoshang.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 11;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Shaoshang.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("少商穴");
   	        		AcuPointsBelongs.setText("井木穴");
   	        		AcuPointsEffect.setText("功效：◎清熱瀉火   ◎利咽開音\n" + "\t\t\t\t\t\t ◎回陽救逆");
   	        		AcuPointsCure.setText("主治：咳嗽，失音，感冒\n" + "\t\t\t\t\t\t 喉嚨痛");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.lung);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Shaoshang.gif");     		
   	        		break;
   	        	case 12:
   	        		if(AcupointID != 12)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Thenar.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 12;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Thenar.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("魚際穴");
   	        		AcuPointsBelongs.setText("滎火穴");
   	        		AcuPointsEffect.setText("功效：◎清瀉肺熱   ◎清咽利喉\n");
   	        		AcuPointsCure.setText("主治：咳嗽，感冒，喉嚨痛\n" + "\t\t\t\t\t\t 扁桃線發炎，發燒");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.lung);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Thenar.gif");
   	        		break;
   	        	case 13: 
   	        		if(AcupointID != 13)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Taiyuan.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 13;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Taiyuan.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("太淵穴");
   	        		AcuPointsBelongs.setText("俞土穴");
   	        		AcuPointsEffect.setText("功效：◎去風清肺   ◎止咳化痰\n");
   	        		AcuPointsCure.setText("主治：咳嗽，氣喘，感冒\n" + "\t\t\t\t\t\t 去痰清肺");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.lung);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Taiyuan.gif");	 
   	        		break;
   	        	case 14: 
   	        		if(AcupointID != 14)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_DaLing.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 14;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_DaLing.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("大陵穴");
   	        		AcuPointsBelongs.setText("俞土穴");
   	        		AcuPointsEffect.setText("功效：◎清心寧神   ◎和胃寬胸\n");
   	        		AcuPointsCure.setText("主治：失眠，嘔吐，胃痛\n" + "\t\t\t\t\t\t 心胸痛，精神病");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.pericardium);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Daling.gif");	 	        		
   	        		break;
   	        	case 15:  
   	        		if(AcupointID != 15)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Laogong.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 15;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Laogong.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	      
   	        		AcuPointsName.setText("勞宮穴");
   	        		AcuPointsBelongs.setText("滎火穴");
   	        		AcuPointsEffect.setText("功效：◎清心瀉熱   ◎安神涼血\n" + "\t\t\t\t\t\t ◎緩和胃火");
   	        		AcuPointsCure.setText("主治：失眠，暈厥，中暑\n" + "\t\t\t\t\t\t 心絞痛，高血壓");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.pericardium);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Laogong.gif");
   	        		//ImageInfo.setImageResource(R.drawable.hand); 	   	        		
   	        		break;
   	        	case 16: 
   	        		if(AcupointID != 16)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Zhongchong.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 16;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Zhongchong.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("中衝穴");
   	        		AcuPointsBelongs.setText("井木穴");
   	        		AcuPointsEffect.setText("功效：◎開竅蘇厥   ◎清心退熱\n");
   	        		AcuPointsCure.setText("主治：躁熱，暈厥，中暑\n" + "\t\t\t\t\t\t 中風昏迷");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.pericardium);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Zhongchong.gif");	 
   	        		break;
   	        	case 17:
   	        		if(AcupointID != 17)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Shenmen.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 17;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Shenmen.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("神門穴");
   	        		AcuPointsBelongs.setText("俞土穴");
   	        		AcuPointsEffect.setText("功效：◎寧心安神   ◎養陰固表\n");
   	        		AcuPointsCure.setText("主治：健忘，焦躁，失眠\n" + "\t\t\t\t\t\t 食欲不振");
   	        		
   	        		ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.heart);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Shenmen.gif");	 
   	        		break;
   	        	case 18:
   	        		if(AcupointID != 18)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_ShaoFu.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 18;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_ShaoFu.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("少府穴");
   	        		AcuPointsBelongs.setText("滎火穴");
   	        		AcuPointsEffect.setText("功效：◎清心除煩   ◎行氣活血\n");
   	        		AcuPointsCure.setText("主治：心悸，胸痛，遺尿\n" + "\t\t\t\t\t\t 心絞痛，陰癢痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.heart);
   	        		GifImage.loadUrl("file:///android_asset/FrontHand/Shaofu.gif"); 	 
   	        		break;
   	        		
   	        	case 21:
   	        		if(AcupointID != 21)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Shaochong.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 21;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Shaochong.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("少衝穴");
   	        		AcuPointsBelongs.setText("井木穴");
   	        		AcuPointsEffect.setText("功效：◎開竅醒神   ◎解熱蘇厥\n");
   	        		AcuPointsCure.setText("主治：躁熱，昏迷，胸痛\n" + "\t\t\t\t\t\t 心絞痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.rain);
   	        		ImageOrgans.setImageResource(R.drawable.heart);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Shaochong.gif");
   	        		break;
   	        	case 22:
   	        		if(AcupointID != 22)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Shangyang.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 22;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Shangyang.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("商陽穴");
   	        		AcuPointsBelongs.setText("井金穴");
   	        		AcuPointsEffect.setText("功效：◎開竅醒神   ◎瀉熱消腫\n");
   	        		AcuPointsCure.setText("主治：腹瀉，躁熱，耳鳴\n" + "\t\t\t\t\t\t 腸胃炎，咽喉腫痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.large_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Shangyang.gif");
   	        		break;
   	        	case 23:
   	        		if(AcupointID != 23)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Erjian.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 23;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Erjian.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("二間穴");
   	        		AcuPointsBelongs.setText("滎水穴");
   	        		AcuPointsEffect.setText("功效：◎散風邪氣   ◎清熱消腫\n");
   	        		AcuPointsCure.setText("主治：血便，嗜睡，躁熱\n" + "\t\t\t\t\t\t 牙齒痛，咽喉腫痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.large_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Erjian.gif");
   	        		break;
   	        	case 24:
   	        		if(AcupointID != 24)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Sanjian.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 24;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Sanjian.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("三間穴");
   	        		AcuPointsBelongs.setText("俞木穴");
   	        		AcuPointsEffect.setText("功效：◎散風邪氣   ◎行氣活血\n" + "\t\t\t\t\t\t ◎清熱瀉火");
   	        		AcuPointsCure.setText("主治：便秘，腹瀉，躁熱\n" + "\t\t\t\t\t\t 口乾舌燥，腹脹氣");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.large_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Sanjian.gif"); 
   	        		break;
   	        	case 25:
   	        		if(AcupointID != 25)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Hoku.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 25;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Hoku.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("合谷穴");
   	        		AcuPointsBelongs.setText("四總穴");
   	        		AcuPointsEffect.setText("功效：◎鎮痛安神  ◎神經活絡\n" + "\t\t\t\t\t\t ◎疏風解表");
   	        		AcuPointsCure.setText("主治：頭痛，腹痛，經痛\n" + "\t\t\t\t\t\t 喉嚨痛，腸胃疾患");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.large_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Hoku.gif"); 
   	        		break;
   	        	case 26:
   	        		if(AcupointID != 26)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Yangxi.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 26;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Yangxi.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("陽谿穴");
   	        		AcuPointsBelongs.setText("經火穴");
   	        		AcuPointsEffect.setText("功效：◎清風散熱   ◎明目利咽\n");
   	        		AcuPointsCure.setText("主治：耳鳴，頭痛，眼紅\n" + "\t\t\t\t\t\t 牙齒痛，咽喉腫痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.large_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Yangchi.gif"); 
   	        		break;
   	        	case 27:
   	        		if(AcupointID != 27)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Yanggu.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 27;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Yanggu.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("陽谷穴");
   	        		AcuPointsBelongs.setText("經火穴");
   	        		AcuPointsEffect.setText("功效：◎舒展筋肉   ◎清熱瀉火\n");
   	        		AcuPointsCure.setText("主治：目眩，頭暈，悶汗\n" + "\t\t\t\t\t\t 齒顎痛，眼紅目赤");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.small_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Yanggu.gif"); 
   	        		break;
   	        	case 28:
   	        		if(AcupointID != 28)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Carpus.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 28;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Carpus.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("腕骨穴");
   	        		AcuPointsBelongs.setText("   ");
   	        		AcuPointsEffect.setText("功效：◎散風舒筋   ◎去濕除熱\n");
   	        		AcuPointsCure.setText("主治：頭痛，耳鳴，嘔吐\n" + "\t\t\t\t\t\t 手腕關節疼痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.small_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Wangu.gif"); 
   	        		break;
   	        	case 29:
   	        		if(AcupointID != 29)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Houxi.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 29;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Houxi.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("後谿穴");
   	        		AcuPointsBelongs.setText("俞木穴");
   	        		AcuPointsEffect.setText("功效：◎散風舒筋   ◎暢通督脈\n" + "\t\t\t\t\t\t ◎寧心安神   ◎清熱利咽");
   	        		AcuPointsCure.setText("主治：頭痛，落枕，腰痛\n" + "\t\t\t\t\t\t 神經衰弱");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.small_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Houqi.gif"); 
   	        		break;
   	        	case 210:
   	        		if(AcupointID != 210)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Qiangu.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 210;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Qiangu.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("前谷穴");
   	        		AcuPointsBelongs.setText("滎水穴");
   	        		AcuPointsEffect.setText("功效：◎清熱瀉火   ◎舒展筋肉\n");
   	        		AcuPointsCure.setText("主治：鼻塞，悶汗，手麻\n" + "\t\t\t\t\t\t 乳腺炎，乳汁不足");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.small_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Qiangu.gif"); 
   	        		break;
   	        	case 211:
   	        		if(AcupointID != 211)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Shaoze.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 211;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Shaoze.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("少澤穴");
   	        		AcuPointsBelongs.setText("井金穴");
   	        		AcuPointsEffect.setText("功效：◎清熱利咽   ◎通筋活絡\n" + "\t\t\t\t\t\t ◎開竅醒神   ◎通乳");
   	        		AcuPointsCure.setText("主治：目翳，頭痛，咽炎\n" + "\t\t\t\t\t\t 乳房疾患");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.small_intestine);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Shaoze.gif");
   	        		break;
   	        	case 212:
   	        		if(AcupointID != 212)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Guanchong.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 212;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Guanchong.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("關衝穴");
   	        		AcuPointsBelongs.setText("井金穴");
   	        		AcuPointsEffect.setText("功效：◎清熱瀉火   ◎開竅醒神\n" + "\t\t\t\t\t\t ◎利喉舌");
   	        		AcuPointsCure.setText("主治：中暑，悶汗，喉炎\n" + "\t\t\t\t\t\t 扁桃體炎，角膜炎");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.sanjiao);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Guanchong.gif"); 
   	        		break;
   	        	case 213:
   	        		if(AcupointID != 213)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Yimen.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 213;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Yimen.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("液門穴");
   	        		AcuPointsBelongs.setText("滎水穴");
   	        		AcuPointsEffect.setText("功效：◎清頭明目   ◎利三焦\n");
   	        		AcuPointsCure.setText("主治：耳疾，躁熱，眼紅\n" + "\t\t\t\t\t\t 指臂攣痛");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.sanjiao);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Yimen.gif"); 
   	        		break;
   	        	case 214:
   	        		if(AcupointID != 214)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Zhongzhuu.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 214;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Zhongzhuu.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("中渚穴");
   	        		AcuPointsBelongs.setText("俞木穴");
   	        		AcuPointsEffect.setText("功效：◎開竅益聰   ◎清熱通絡\n" + "\t\t\t\t\t\t 理氣解鬱");
   	        		AcuPointsCure.setText("主治：耳疾，眼疾，喉炎\n" + "\t\t\t\t\t\t 關節發炎");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.sanjiao);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Zhongzhu.gif"); 
   	        		break;
   	        	case 215:
   	        		if(AcupointID != 215)
   	        		{
   	        			CheckPress = 0;
   	        			Popup.update(0, 0, 0, 0);
   	        		}
   	        		if(CheckPress == 0)
   	        		{
   	        			view.startAnimation(shake);
   	        			
   	        			Btn_Yangchi.setTextColor(Color.rgb(0, 0, 255));
   	        			CheckPress = 1;
   	        			AcupointID = 215;
   	        		}
   	        		else if(CheckPress == 2)
   	        		{
   	        			Btn_Yangchi.setTextColor(Color.rgb(0, 0, 0));
   	        			CheckPress = 0;
   	        			AcupointID = 0;
   	        		}
   	        		
   	        		AcuPointsName.setText("陽池穴");
   	        		AcuPointsBelongs.setText("   ");
   	        		AcuPointsEffect.setText("功效：◎疏風散熱   ◎舒筋活絡\n");
   	        		AcuPointsCure.setText("主治：口乾，眼腫，感冒\n" + "\t\t\t\t\t\t 臂肘疼痛，腕無力");
   	        		
   	            	ImageKind.setImageResource(R.drawable.sun);
   	        		ImageOrgans.setImageResource(R.drawable.sanjiao);
   	        		GifImage.loadUrl("file:///android_asset/BackHand/Yangchi.gif"); 
   	        		break;
   	        	}
    	   }
    };
    
    /**Timer Update Imfo*/
    final Runnable runnable = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub  
        	Btn_Shaoshang.setX(IBShaoshangX + 75);
            Btn_Shaoshang.setY(IBShaoshangY - 55);
            
            Btn_Thenar.setX(IBThenarX + 70);
            Btn_Thenar.setY(IBThenarY - 55);
            
            Btn_Taiyuan.setX(IBTaiyuanX + 70);
            Btn_Taiyuan.setY(IBTaiyuanY - 55);
            
            Btn_DaLing.setX(IBDaLingX + 140);
            Btn_DaLing.setY(IBDaLingY - 55);
            
            Btn_Laogong.setX(IBLaogongX + 70);
            Btn_Laogong.setY(IBLaogongY - 55);
            
            Btn_Zhongchong.setX(IBZhongchongX + 70);
            Btn_Zhongchong.setY(IBZhongchongY - 55);
            
            Btn_Shenmen.setX(IBShenmenX + 70);
            Btn_Shenmen.setY(IBShenmenY - 55);
        	
            Btn_ShaoFu.setX(IBShaoFuX + 70);
            Btn_ShaoFu.setY(IBShaoFuY - 55);
            
            Btn_Shaochong.setX(IBShaochongX + 70);
            Btn_Shaochong.setY(IBShaochongY - 80);
            
            Btn_Shangyang.setX(IBShangyangX + 70);
            Btn_Shangyang.setY(IBShangyangY - 55);
            
            Btn_Erjian.setX(IBErjianX + 70);
            Btn_Erjian.setY(IBErjianY - 55);
            
            Btn_Sanjian.setX(IBSanjianX + 70);
            Btn_Sanjian.setY(IBSanjianY - 55);
            
            Btn_Hoku.setX(IBHokuX + 70);
            Btn_Hoku.setY(IBHokuY - 55);
            
            Btn_Yangxi.setX(IBYangxiX + 70);
            Btn_Yangxi.setY(IBYangxiY - 55);
            
            Btn_Yanggu.setX(IBYangguX + 70);
            Btn_Yanggu.setY(IBYangguY - 55);
            
            Btn_Carpus.setX(IBCarpusX + 70);
            Btn_Carpus.setY(IBCarpusY - 55);
            
            Btn_Houxi.setX(IBHouxiX + 70);
            Btn_Houxi.setY(IBHouxiY - 40);
            
            Btn_Qiangu.setX(IBQianguX + 70);
            Btn_Qiangu.setY(IBQianguY - 40);
            
            Btn_Shaoze.setX(IBShaozeX + 140);
            Btn_Shaoze.setY(IBShaozeY - 30);
            
            Btn_Guanchong.setX(IBGuanchongX + 70);
            Btn_Guanchong.setY(IBGuanchongY - 55);
            
            Btn_Yimen.setX(IBYimenX + 70);
            Btn_Yimen.setY(IBYimenY - 65);
            
            Btn_Zhongzhuu.setX(IBZhongzhuuX + 70);
            Btn_Zhongzhuu.setY(IBZhongzhuuY - 65);
            
            Btn_Yangchi.setX(IBYangchiX + 140);
            Btn_Yangchi.setY(IBYangchiY - 60);

            if(ShowAupoints == false)
            { 
            	Btn_Shaoshang.setVisibility(View.INVISIBLE);
            	Btn_Thenar.setVisibility(View.INVISIBLE);
            	Btn_Taiyuan.setVisibility(View.INVISIBLE);
            	Btn_DaLing.setVisibility(View.INVISIBLE);
            	Btn_Laogong.setVisibility(View.INVISIBLE);
            	Btn_Zhongchong.setVisibility(View.INVISIBLE);
            	Btn_Shenmen.setVisibility(View.INVISIBLE);
            	Btn_ShaoFu.setVisibility(View.INVISIBLE);
            	
            	Btn_Shaochong.setVisibility(View.INVISIBLE);
                Btn_Shangyang.setVisibility(View.INVISIBLE);
                Btn_Erjian.setVisibility(View.INVISIBLE);
                Btn_Sanjian.setVisibility(View.INVISIBLE);
                Btn_Hoku.setVisibility(View.INVISIBLE);
                Btn_Yangxi.setVisibility(View.INVISIBLE);
                Btn_Yanggu.setVisibility(View.INVISIBLE);
                Btn_Carpus.setVisibility(View.INVISIBLE);
                Btn_Houxi.setVisibility(View.INVISIBLE);
                Btn_Qiangu.setVisibility(View.INVISIBLE);
                Btn_Shaoze.setVisibility(View.INVISIBLE);
                Btn_Guanchong.setVisibility(View.INVISIBLE);
                Btn_Yimen.setVisibility(View.INVISIBLE);
                Btn_Zhongzhuu.setVisibility(View.INVISIBLE);
                Btn_Yangchi.setVisibility(View.INVISIBLE);
            } 
            else
            {
            	// 1 ：正面，2 ：反面
            	if(BackOrFront == 1)
            	{
            		Btn_Shaoshang.setVisibility(View.VISIBLE);
                	Btn_Thenar.setVisibility(View.VISIBLE);
                	Btn_Taiyuan.setVisibility(View.VISIBLE);
                	Btn_DaLing.setVisibility(View.VISIBLE);
                	Btn_Laogong.setVisibility(View.VISIBLE);
                	Btn_Zhongchong.setVisibility(View.VISIBLE);
                	Btn_Shenmen.setVisibility(View.VISIBLE);
                	Btn_ShaoFu.setVisibility(View.VISIBLE);	
            	}
            	if(BackOrFront == 2)
            	{
            		Btn_Shaochong.setVisibility(View.VISIBLE);
                    Btn_Shangyang.setVisibility(View.VISIBLE);
                    Btn_Erjian.setVisibility(View.VISIBLE);
                    Btn_Sanjian.setVisibility(View.VISIBLE);
                    Btn_Hoku.setVisibility(View.VISIBLE);
                    Btn_Yangxi.setVisibility(View.VISIBLE);
                    Btn_Yanggu.setVisibility(View.VISIBLE);
                    Btn_Carpus.setVisibility(View.VISIBLE);
                    Btn_Houxi.setVisibility(View.VISIBLE);
                    Btn_Qiangu.setVisibility(View.VISIBLE);
                    Btn_Shaoze.setVisibility(View.VISIBLE);
                    Btn_Guanchong.setVisibility(View.VISIBLE);
                    Btn_Yimen.setVisibility(View.VISIBLE);
                    Btn_Zhongzhuu.setVisibility(View.VISIBLE);
                    Btn_Yangchi.setVisibility(View.VISIBLE);
            	}
            }
            
            //GetImageViewEvent();      	
        	mHandler.postDelayed(runnable, 500);       	
        }
    };   
   
    /**Loader OpenCV4Android version*/
    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);       
    }

	
	@Override
	protected void onPause() {
	    // TODO Auto-generated method stub
	    super.onPause();
	    if (mHandler != null) {
	        mHandler.removeCallbacks(runnable);
	    }
	}
    

    /**Java_Camera Start Initialize Scripting*/
	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
        mRgba = new Mat();
        mSpectrum = new Mat();
        
        SpectrumSize = new Size(200, 64);
        mDetector = new ColorBlobDetector();
        
        //mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        
        //AreaColor = new Scalar(255, 0, 0); //Red color: Rectangle of object(plam) area
        ContourColor = new Scalar(240, 150, 105); // Green color: Contour of object 
        //HullColor = new Scalar(0, 0, 255); // Blue color: Connect the object convex
        //DefectPointColor = new Scalar(255, 255, 0); // Yellow color: Defect Point of object 
        //StartPointColor = new Scalar(140, 0, 255); // Purple color: Contour of object 
        //EndPointColor = new Scalar(128, 128, 128); // Olive color: Contour of object 

        //boundRect = new Rect();     
        
        thisContour = new MatOfPoint();
		approxContour1 = new MatOfPoint2f();
		approxContour2 = new MatOfPoint2f();
        
        //start = new Point();
        //end = new Point();
        //defect = new Point();
        
        FrontPalmCenter = new Point();
        BackPalmCenter = new Point();
        
        /**指尖*/
        fThumbTip = new Point();
        fIndexTip = new Point();
        fMiddleTip = new Point();    
        fRingTip = new Point();    
        fLittleTip = new Point();
        
        bThumbTip = new Point();
        bIndexTip = new Point();
        bMiddleTip = new Point();
        bRingTip = new Point();
        bLittleTip = new Point();
            
        /**指谷*/
        Thu_Palm = new Point(); //0
        Thu_ThuL = new Point(); //1
        Thu_ThuR = new Point(); //2
        Thu_Ind = new Point(); //3
        Ind_Ind = new Point(); //4
        Ind_Mid = new Point(); //5
        Mid_Rin = new Point(); //6
        Rin_Lit = new Point(); //7
        Lit_Lit = new Point(); //8 
        
        //bThu_Thu = new Point();
        bThu_ThuR = new Point();
        bThu_ThuL = new Point();
        bThu_Ind = new Point();                
        bInd_Ind = new Point();
        bInd_Mid = new Point();
        bMid_Rin = new Point();
        bRin_Lit = new Point();
        bLit_Lit = new Point();
        bLit_Palm = new Point();
        
        /**手指底端點*/
        ThumbDip = new Point();
        IndexDip = new Point();
        MiddleDip = new Point();
        RingDip = new Point();
        LittleDip = new Point();     
        
        bThumbDip = new Point();
        bIndexDip = new Point(); 
        bMiddleDip = new Point();
        bRingDip = new Point();
        bLittleDip = new Point();
        
        /**手指中心點*/
        //ThumbCenter = new Point();
        IndexCenter = new Point();
        MiddleCenter = new Point();
        RingCenter = new Point();
        LittleCenter = new Point();
        
        bThumbCenter = new Point();
		bIndexCenter = new Point();
		bMiddleCenter = new Point();
		bRingCenter = new Point();
		bLittleCenter = new Point();
        
		//指尖與兩指谷連成線段之中點，共兩點
		//ThuLeftLineCenter = new Point();
        //ThuRightLineCenter = new Point();
        IndLeftLineCenter = new Point();
        IndRightLineCenter = new Point();    
        MidLeftLineCenter = new Point();
        MidRightLineCenter = new Point();   
        RinLeftLineCenter = new Point();
        RinRightLineCenter = new Point();   
        LitLeftLineCenter = new Point();
        LitRightLineCenter = new Point();
             
		bIndLeftLineCenter = new Point();
		bIndRightLineCenter = new Point();
		bMidLeftLineCenter = new Point();
		bMidRightLineCenter = new Point();
		bRinLeftLineCenter = new Point();
		bRinRightLineCenter = new Point();
		bLitLeftLineCenter = new Point();
		bLitRightLineCenter = new Point();
		
		/**第二指節*/
        IndFingerGravity = new Point();
        IndKnucklesDown = new Point();    
        MidFingerGravity = new Point();
        MidKnucklesDown = new Point();  
        RinFingerGravity = new Point();
        RinKnucklesDown = new Point();    
        LitFingerGravity = new Point();
        LitKnucklesDown = new Point();
        
        bIndFingerGravity = new Point();
        bIndKnucklesDown = new Point();
		bMidFingerGravity = new Point();
		bMidKnucklesDown = new Point();
		bRinFingerGravity = new Point();
		bRinKnucklesDown = new Point();
		bLitFingerGravity = new Point();
		bLitKnucklesDown = new Point();
        
        /**第一指節*/
		ThuKnucklesTop = new Point();
        IndKnucklesTop = new Point();
        MidKnucklesTop = new Point();         
        RinKnucklesTop = new Point();
        Litfingergravity = new Point();
        LitKnucklesTop = new Point();
        
        //bThuKnucklesTop = new Point();
        bIndKnucklesTop = new Point();
		bMidKnucklesTop = new Point();         
		bRinKnucklesTop = new Point();
		bLitfingergravity = new Point();
		bLitKnucklesTop = new Point();
        //Back Finger Knuckles Top 參考-根據點
        bIndKnucklesTopTP = new Point();
        bIndKnucklesTopDP = new Point();
		bMidKnucklesTopTP = new Point();
		bMidKnucklesTopDP = new Point();
		bRinKnucklesTopTP = new Point();
		bRinKnucklesTopDP = new Point();
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
        mRgba.release();
	}
			

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		mRgba = inputFrame.rgba();
		
        //高斯模糊
        Imgproc.GaussianBlur(mRgba, mRgba, new Size(5, 5), 2, 2);
		
        List<MatOfPoint> contours = mDetector.getContours();
        //List<MatOfPoint2f> contours2f   = new ArrayList<MatOfPoint2f>(contours.size());
        
		mDetector.process(mRgba); //執行影像程序
		
		//mIsColorSelected 判斷是否開始偵測輪廓(解決程式一執行就開始偵測的問題)
		if ((!mIsColorSelected) || contours.size() <= 0)
        {
        	return mRgba;
        }
		
		//ConvexHull Detection
		List<MatOfInt> hull = new ArrayList<MatOfInt>();
		List<MatOfInt4> convexDefect = new ArrayList<MatOfInt4>();
		
		for (int i = 0; i < contours.size(); i++) {

			Imgproc.drawContours(mRgba, contours, i, ContourColor, 10); //繪製輪廓
						
			double d = Imgproc.contourArea (contours.get(i));
			int iContourAreaMin = 5;
			
			if(d > iContourAreaMin)
			 {
				 thisContour = contours.get(i);
				 thisContour.convertTo(approxContour1, CvType.CV_32FC2);
				 
				 //對 contour 做多邊形逼近(approxPolyDP)
				 //去除細小和多於的邊緣點.(用粗線描邊來忽略細微的雜點.)
				 Imgproc.approxPolyDP(approxContour1, approxContour2, 3, true);			 
				 approxContour2.convertTo(thisContour, CvType.CV_32S);
			 }
			
			/**Hand ROI*/
			//最小外接矩形
			/*boundRect = Imgproc.boundingRect(new MatOfPoint(thisContour.toArray()));
			Imgproc.rectangle( mRgba, boundRect.tl(), boundRect.br(), AreaColor, 10);						
			//外接圓
			//float[] radius = new float[1];
			float[] radius = new float[contours.size()];
			Point center = new Point();
			Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(i).toArray()), center, radius);
			Imgproc.circle(mRgba, center, 100, AreaColor, 5);
			
			//矩形中心點
			int CX =  boundRect.x + boundRect.width/2;
			int CY =  boundRect.y + boundRect.height/2;		
			Point Center = new Point(CX, CY);
			Imgproc.circle(mRgba, Center, 10, new Scalar(255, 0, 0), 10);*/
		
			//ConvexHull Detection
			hull.add(new MatOfInt());
            convexDefect.add(new MatOfInt4());
            
            Imgproc.convexHull(contours.get(i), hull.get(i));
            Imgproc.convexityDefects(contours.get(i), hull.get(i),convexDefect.get(i));
		}
		
		List<Point[]> hullpoints = new ArrayList<Point[]>();
		
		for(int e = 0; e < hull.size(); e++){
            Point[] points = new Point[hull.get(e).rows()];

            // Loop over all points that need to be hulled in current contour
            for(int j=0; j < hull.get(e).rows(); j++){
                int index = (int)hull.get(e).get(j, 0)[0];
                points[j] = new Point(contours.get(e).get(index, 0)[0], contours.get(e).get(index, 0)[1]);
            }
            hullpoints.add(points);
        }
		
		List<MatOfPoint> hullmop = new ArrayList<MatOfPoint>();
		
		for(int s=0; s < hullpoints.size(); s++){
            MatOfPoint mop = new MatOfPoint();
            mop.fromArray(hullpoints.get(s));
            hullmop.add(mop);
		}
		
		//Draw contours + hull results
        for(int i=0; i < contours.size(); i++){
            
            //Imgproc.drawContours(mRgba, hullmop, i, HullColor, 10); //凸點連結
            
            List<Integer> cdList = convexDefect.get(i).toList();
            
            Point data[] = contours.get(i).toArray();
            for (int k = 0; k < cdList.size(); k += 4) {
            	
                //start = data[cdList.get(k)];
                //end = data[cdList.get(k+1)];
                //defect = data[cdList.get(k+2)];                
                //Imgproc.circle(mRgba, start, 5, StartPointColor, 10);
                //Imgproc.circle(mRgba, defect, 5, DefectPointColor, 10);
                //Imgproc.circle(mRgba, end, 5, EndPointColor, 10);
                
                if(k > 20)
                {
                	/////////Front of Hand Detection/////////
                	/** 指尖 -頂點 */
                	fThumbTip = data[cdList.get(24)];
                	fIndexTip = data[cdList.get(20)];
                	fMiddleTip = data[cdList.get(16)];
                	fRingTip = data[cdList.get(12)];
                	fLittleTip = data[cdList.get(8)];
                	
                	bThumbTip = data[cdList.get(8)];
                	bIndexTip = data[cdList.get(12)];
                	bMiddleTip = data[cdList.get(16)];
                	bRingTip = data[cdList.get(20)];
                	bLittleTip = data[cdList.get(24)];
                	
                	/////////Back of Hand Detection/////////
                	/**指谷*/
                	Thu_Palm = data[cdList.get(26)];//0
                	Thu_Ind = data[cdList.get(22)]; //3               	
                	Ind_Mid = data[cdList.get(18)]; //5
                	Mid_Rin = data[cdList.get(14)]; //6
                	Rin_Lit = data[cdList.get(10)]; //7
                	//Lit_Lit = data[cdList.get(6)]; //8
                	
                	//bThu_Thu = data[cdList.get(6)]; //0
                	bThu_Ind = data[cdList.get(10)]; //3               	
                	bInd_Mid = data[cdList.get(14)]; //5
                	bMid_Rin = data[cdList.get(18)]; //6
                	bRin_Lit = data[cdList.get(22)]; //7
                	bLit_Palm = data[cdList.get(26)]; //8          	
                }        		
                
                if(BackHandDetection)
                {
                	BackOfFeaturePoint();
                    
                }
                if(FrontHandDetection)
                {
                	FrontOfFeaturePoint();
                }            
                               
                /*計算座標半徑
                float R_Coord_X = (float) (PalmCenter.x - Thu_Ind.x);
                float R_Coord_Y = (float) (PalmCenter.y - Thu_Ind.y);              
                //sqrt開平方根, R 為半徑
                int R = (int) Math.sqrt((R_Coord_X*R_Coord_X) + (R_Coord_Y*R_Coord_Y));
                Imgproc.circle(mRgba, PalmCenter, R, new Scalar(50, 0, 0), 5);
                Imgproc.putText(mRgba, "Ji Jiou", new Point((thumb.x)+20, (thumb.y)), Core.FONT_HERSHEY_SIMPLEX, 2, new Scalar(30, 150, 255), 5);*/                                                                     
            }
            /*BackDrawPoint();
            BackAcupoints();*/           
            if(BackHandDetection)
            {
            	//BackDrawPoint();
            	if(BackOrFront == 2)
                {
            		if(ShowAupoints)
            		{
            			BackAcupoints();
            		}        
                }  
            }
            if(FrontHandDetection)
            {
            	//FrontDrawPoint(); 
            	if(BackOrFront == 1)
                {
            		if(ShowAupoints)
            		{
            			FrnotAcupoints();
            		}               	
                }  
            }
        }

        return mRgba;
	}
	
	public void BackOfFeaturePoint()
	{		
		//食指外側指谷
    	bInd_Ind = new Point((bThumbTip.x + 3*bInd_Mid.x)/4, (bThumbTip.y + 3*bInd_Mid.y)/4);
    	
    	//小拇指外側指谷
    	Point O = new Point((5*bRin_Lit.x + 3*bLittleTip.x)/8, (5*bRin_Lit.y + 3*bLittleTip.y)/8);   	
    	bLit_Lit = new Point((11*O.x - 3*bRingTip.x)/8, (11*O.y - 3*bRingTip.y)/8);
    	//bLit_Lit = new Point((1771*bRin_Lit.x + 997*bLittleTip.x - 720*bRingTip.x)/2048, (1771*bRin_Lit.y + 997*bLittleTip.y - 720*bRingTip.y)/2048);
    	   	
    	//大拇指右側指谷
    	bThu_ThuR = new Point(2*bThu_Ind.x - bInd_Mid.x, 2*bThu_Ind.y - bInd_Mid.y);
    	
    	//大拇指左側指谷
    	Point H = new Point((bThumbTip.x + 7*bThu_Ind.x)/8, (bThumbTip.y + 7*bThu_Ind.y)/8);
    	bThu_ThuL = new Point((3*H.x + bThu_ThuR.x)/4, (3*H.y + bThu_ThuR.y)/4);
    	
    	/**手指底端點*/
    	bThumbDip = new Point((bThu_ThuR.x + bThu_ThuL.x)/2, (bThu_ThuL.y + bThu_ThuR.y)/2);
    	bIndexDip = new Point((bInd_Ind.x + bInd_Mid.x)/2, (bInd_Ind.y + bInd_Mid.y)/2); 
    	bMiddleDip = new Point((bInd_Mid.x + bMid_Rin.x)/2, (bInd_Mid.y + bMid_Rin.y)/2);
    	bRingDip = new Point((bMid_Rin.x + bRin_Lit.x)/2, (bMid_Rin.y + bRin_Lit.y)/2);
    	bLittleDip = new Point((bRin_Lit.x + bLit_Lit.x)/2, (bRin_Lit.y + bLit_Lit.y)/2);
    
    	/**手指中心點*/
    	bThumbCenter = new Point((bThumbTip.x + bThumbDip.x)/2, (bThumbTip.y + bThumbDip.y)/2);
    	bIndexCenter = new Point((bIndexTip.x + bIndexDip.x)/2, (bIndexTip.y + bIndexDip.y)/2);
    	bMiddleCenter = new Point((bMiddleTip.x + bMiddleDip.x)/2, (bMiddleTip.y + bMiddleDip.y)/2);
    	bRingCenter = new Point((bRingTip.x + bRingDip.x)/2, (bRingTip.y + bRingDip.y)/2);
    	bLittleCenter = new Point((bLittleTip.x + bLittleDip.x)/2, (bLittleTip.y + bLittleDip.y)/2);
    
    	//指尖與兩指谷連成線段之中點，共兩點
    	bIndLeftLineCenter = new Point((bIndexTip.x + bInd_Ind.x)/2, (bIndexTip.y + bInd_Ind.y)/2);
    	bMidLeftLineCenter = new Point((bMiddleTip.x + bInd_Mid.x)/2, (bMiddleTip.y + bInd_Mid.y)/2);
    	bRinLeftLineCenter = new Point((bRingTip.x + bMid_Rin.x)/2, (bRingTip.y + bMid_Rin.y)/2);
    	bLitLeftLineCenter = new Point((bLittleTip.x + bRin_Lit.x)/2, (bLittleTip.y + bRin_Lit.y)/2);
	
    	bIndRightLineCenter = new Point((bIndexTip.x + bInd_Mid.x)/2, (bIndexTip.y + bInd_Mid.y)/2);
    	bMidRightLineCenter = new Point((bMiddleTip.x + bMid_Rin.x)/2, (bMiddleTip.y + bMid_Rin.y)/2);
    	bRinRightLineCenter = new Point((bRingTip.x + bRin_Lit.x)/2, (bRingTip.y + bRin_Lit.y)/2);
    	bLitRightLineCenter = new Point((bLittleTip.x + bLit_Lit.x)/2, (bLittleTip.y + bLit_Lit.y)/2);
    
    	///指節主要運算法："三角形重心"
    	/**第二指節：Dip → 2Center(tip → 2recess)三角形重心，並取得此重心與手指中心點連線靠近重心之X軸的1/4處*/
    	bIndFingerGravity = new Point((bIndexDip.x + bIndLeftLineCenter.x + bIndRightLineCenter.x)/3, (bIndexDip.y + bIndLeftLineCenter.y + bIndRightLineCenter.y)/3);
    	bMidFingerGravity = new Point((bMiddleDip.x + bMidLeftLineCenter.x + bMidRightLineCenter.x)/3, (bMiddleDip.y + bMidLeftLineCenter.y + bMidRightLineCenter.y)/3);
    	bRinFingerGravity = new Point((bRingDip.x + bRinLeftLineCenter.x + bRinRightLineCenter.x)/3, (bRingDip.y + bRinLeftLineCenter.y + bRinRightLineCenter.y)/3);
    	bLitFingerGravity = new Point((bLittleDip.x + bLitLeftLineCenter.x + bLitRightLineCenter.x)/3, (bLittleDip.y + bLitLeftLineCenter.y + bLitRightLineCenter.y)/3);
	
    	bIndKnucklesDown = new Point((3*bIndFingerGravity.x + bIndexCenter.x)/4, bIndFingerGravity.y);
    	bMidKnucklesDown = new Point((3*bMidFingerGravity.x + bMiddleCenter.x)/4, bMidFingerGravity.y);
    	bRinKnucklesDown = new Point((3*bRinFingerGravity.x + bRingCenter.x)/4, bRinFingerGravity.y);
    	bLitKnucklesDown = new Point((3*bLitFingerGravity.x + bLittleCenter.x)/4, bLitFingerGravity.y);
	
    	/**第一指節：Tip → 2Center(tip → 2recess)三角形重心，並取得此重心與(tip → fingerCenter之中點)的中心點*/
    	bIndKnucklesTopTP = new Point((bIndexTip.x + bIndLeftLineCenter.x + bIndRightLineCenter.x)/3, (bIndexTip.y + bIndLeftLineCenter.y + bIndRightLineCenter.y)/3);
    	bMidKnucklesTopTP = new Point((bMiddleTip.x + bMidLeftLineCenter.x + bMidRightLineCenter.x)/3, (bMiddleTip.y + bMidLeftLineCenter.y + bMidRightLineCenter.y)/3);         
    	bRinKnucklesTopTP = new Point((bRingTip.x + bRinLeftLineCenter.x + bRinRightLineCenter.x)/3, (bRingTip.y + bRinLeftLineCenter.y + bRinRightLineCenter.y)/3);
	
    	bIndKnucklesTopDP = new Point((bIndexTip.x + bIndLeftLineCenter.x)/2, (bIndexTip.y + bIndexCenter.y)/2);
    	bMidKnucklesTopDP = new Point((bMiddleTip.x + bMiddleCenter.x)/2, (bMiddleTip.y + bMiddleCenter.y)/2);         
    	bRinKnucklesTopDP = new Point((bRingTip.x + bRingCenter.x)/2, (bRingTip.y + bRingCenter.y)/2);
	
    	bIndKnucklesTop = new Point((bIndKnucklesTopTP.x + bIndKnucklesTopDP.x)/2, (bIndKnucklesTopTP.y + bIndKnucklesTopDP.y)/2);
    	bMidKnucklesTop = new Point((bMidKnucklesTopTP.x + bMidKnucklesTopDP.x)/2, (bMidKnucklesTopTP.y + bMidKnucklesTopDP.y)/2);         
    	bRinKnucklesTop = new Point((bRinKnucklesTopTP.x + bRinKnucklesTopDP.x)/2, (bRinKnucklesTopTP.y + bRinKnucklesTopDP.y)/2);
    	//小指第一指節會有偏差，因此使用第二指節之方式找尋
    	bLitfingergravity = new Point((bLittleTip.x + bLitLeftLineCenter.x + bLitRightLineCenter.x)/3, (bLittleTip.y + bLitLeftLineCenter.y + bLitRightLineCenter.y)/3);
    	bLitKnucklesTop = new Point((3*bLitfingergravity.x + bLittleCenter.x)/4, bLitfingergravity.y);
    	
    	/**手背掌心*/
    	BackPalmCenter = new Point(2*bMiddleDip.x - bMidKnucklesDown.x, 2*bMiddleDip.y - bMidKnucklesDown.y);
	}	
	
	public void FrontOfFeaturePoint()
	{
		//大拇指兩端指谷
		Point A = new Point((3*fThumbTip.x + 5*Thu_Palm.x)/8, (3*fThumbTip.y + 5*Thu_Palm.y)/8); //1
		Thu_ThuL = new Point((15*A.x + Thu_Ind.x)/16, (15*A.y + Thu_Ind.y)/16);
		
		Point B = new Point((3*Thu_Ind.x + fThumbTip.x)/4, (3*Thu_Ind.y + fThumbTip.y)/4);
		Thu_ThuR = new Point((55*B.x + 9*Thu_Palm.x)/64, (55*B.y + 9*Thu_Palm.y)/64); //2
		
		//食指外側指谷
        Ind_Ind = new Point((29*fThumbTip.x + 99*Ind_Mid.x)/128, (29*fThumbTip.y + 99*Ind_Mid.y)/128);
        
        //小拇指外側指谷
        //Point C = new Point((33*Mid_Rin.x + 31*fLittleTip.x)/64, (33*Mid_Rin.y + 31*fLittleTip.y)/64);
        Point C = new Point((Mid_Rin.x + fLittleTip.x)/2, (Mid_Rin.y + fLittleTip.y)/2);
        Lit_Lit = new Point((51*C.x - 19*fRingTip.x)/32, (51*C.y - 19*fRingTip.y)/32);
		
        /**手指底端點*/
        ThumbDip = new Point((Thu_ThuL.x + Thu_ThuR.x)/2, (Thu_ThuL.y + Thu_ThuR.y)/2);
        MiddleDip = new Point((Ind_Mid.x + Mid_Rin.x)/2, (Ind_Mid.y + Mid_Rin.y)/2);
        RingDip = new Point((Mid_Rin.x + Rin_Lit.x)/2, (Mid_Rin.y + Rin_Lit.y)/2);
        LittleDip = new Point((Rin_Lit.x + Lit_Lit.x)/2, (Rin_Lit.y + Lit_Lit.y)/2);
        IndexDip = new Point((Ind_Ind.x + Ind_Mid.x)/2, (Ind_Ind.y + Ind_Mid.y)/2);
                                       
        /**手指中心點*/
        //ThumbCenter = new Point((fThumbTip.x + ThumbDip.x)/2, (fThumbTip.y + ThumbDip.y)/2);
        IndexCenter = new Point((fIndexTip.x + IndexDip.x)/2, (fIndexTip.y + IndexDip.y)/2);
        MiddleCenter = new Point((fMiddleTip.x + MiddleDip.x)/2, (fMiddleTip.y + MiddleDip.y)/2);
        RingCenter = new Point((fRingTip.x + RingDip.x)/2, (fRingTip.y + RingDip.y)/2);
        LittleCenter = new Point((fLittleTip.x + LittleDip.x)/2, (fLittleTip.y + LittleDip.y)/2);
        
        //指尖與兩指谷連成線段之中點，共兩點
        //ThuLeftLineCenter = new Point((fThumbTip.x + Thu_ThuL.x)/2, (fThumbTip.y + Thu_ThuL.y)/2);
        IndLeftLineCenter = new Point((fIndexTip.x + Ind_Ind.x)/2, (fIndexTip.y + Ind_Ind.y)/2);
        MidLeftLineCenter = new Point((fMiddleTip.x + Ind_Mid.x)/2, (fMiddleTip.y + Ind_Mid.y)/2);
        RinLeftLineCenter = new Point((fRingTip.x + Mid_Rin.x)/2, (fRingTip.y + Mid_Rin.y)/2);
        LitLeftLineCenter = new Point((fLittleTip.x + Rin_Lit.x)/2, (fLittleTip.y + Rin_Lit.y)/2);
        
        //ThuRightLineCenter = new Point((fThumbTip.x + Thu_ThuR.x)/2, (fThumbTip.y + Thu_ThuR.y)/2);
        IndRightLineCenter = new Point((fIndexTip.x + Ind_Mid.x)/2, (fIndexTip.y + Ind_Mid.y)/2);      
        MidRightLineCenter = new Point((fMiddleTip.x + Mid_Rin.x)/2, (fMiddleTip.y + Mid_Rin.y)/2);
        RinRightLineCenter = new Point((fRingTip.x + Rin_Lit.x)/2, (fRingTip.y + Rin_Lit.y)/2);
        LitRightLineCenter = new Point((fLittleTip.x + Lit_Lit.x)/2, (fLittleTip.y + Lit_Lit.y)/2);
        
        ///指節主要運算法："三角形重心"
        /**第二指節：Dip → 2Center(tip → 2recess)三角形重心，並取得與手指中心點連線靠近重心之X軸的1/4處*/
        IndFingerGravity = new Point((IndexDip.x + IndLeftLineCenter.x + IndRightLineCenter.x)/3, (IndexDip.y + IndLeftLineCenter.y + IndRightLineCenter.y)/3);
        MidFingerGravity = new Point((MiddleDip.x + MidLeftLineCenter.x + MidRightLineCenter.x)/3, (MiddleDip.y + MidLeftLineCenter.y + MidRightLineCenter.y)/3);
        RinFingerGravity = new Point((RingDip.x + RinLeftLineCenter.x + RinRightLineCenter.x)/3, (RingDip.y + RinLeftLineCenter.y + RinRightLineCenter.y)/3);
        LitFingerGravity = new Point((LittleDip.x + LitLeftLineCenter.x + LitRightLineCenter.x)/3, (LittleDip.y + LitLeftLineCenter.y + LitRightLineCenter.y)/3);
        
        IndKnucklesDown = new Point((3*IndFingerGravity.x + IndexCenter.x)/4, IndFingerGravity.y);
        MidKnucklesDown = new Point((3*MidFingerGravity.x + MiddleCenter.x)/4, MidFingerGravity.y);
        RinKnucklesDown = new Point((3*RinFingerGravity.x + RingCenter.x)/4, RinFingerGravity.y);
        LitKnucklesDown = new Point((3*LitFingerGravity.x + LittleCenter.x)/4, LitFingerGravity.y);
        
        /**第一指節：Tip → 2Center(tip → 2recess)三角形重心處*/       
        IndKnucklesTop = new Point((fIndexTip.x + IndLeftLineCenter.x + IndRightLineCenter.x)/3, (fIndexTip.y + IndLeftLineCenter.y + IndRightLineCenter.y)/3);
        MidKnucklesTop = new Point((fMiddleTip.x + MidLeftLineCenter.x + MidRightLineCenter.x)/3, (fMiddleTip.y + MidLeftLineCenter.y + MidRightLineCenter.y)/3);         
        RinKnucklesTop = new Point((fRingTip.x + RinLeftLineCenter.x + RinRightLineCenter.x)/3, (fRingTip.y + RinLeftLineCenter.y + RinRightLineCenter.y)/3);                               
        
        //小指第一指節會有偏差，因此使用第二指節之方式找尋
        Litfingergravity = new Point((fLittleTip.x + LitLeftLineCenter.x + LitRightLineCenter.x)/3, (fLittleTip.y + LitLeftLineCenter.y + LitRightLineCenter.y)/3);
        LitKnucklesTop = new Point((3*Litfingergravity.x + LittleCenter.x)/4, Litfingergravity.y);
        
        //大拇指 - 指節算法
        Point D = new Point((2*fThumbTip.x + ThumbDip.x)/3, (2*fThumbTip.y + ThumbDip.y)/3);
        Point E = new Point((9*D.x + 7*Thu_ThuR.x)/16, (9*D.y + 7*Thu_ThuR.y)/16);
        Point F = new Point((3*E.x - Thu_ThuL.x)/2, (3*E.y - Thu_ThuL.y)/2);
        ThuKnucklesTop = new Point((D.x + F.x)/2, (D.y + F.y)/2);
        
        /**手掌掌心*/
        FrontPalmCenter = new Point(2*MiddleDip.x - MidKnucklesDown.x, 2*MiddleDip.y - MidKnucklesDown.y);
	}
	
	public void BackDrawPoint()
	{
    	/** 指尖 -頂點 */
    	Imgproc.circle(mRgba, bThumbTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, bIndexTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, bMiddleTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, bRingTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, bLittleTip, 5, new Scalar(255, 0, 0), 15);
    	
    	/**指谷*/
    	//Imgproc.circle(mRgba, bThu_Thu, 5, new Scalar(255, 215, 0), 8);
    	Imgproc.circle(mRgba, bThu_ThuR, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bThu_ThuL, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bThu_Ind, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bInd_Ind, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bInd_Mid, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bMid_Rin, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bRin_Lit, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bLit_Lit, 5, new Scalar(0, 255, 0), 15);
    	Imgproc.circle(mRgba, bLit_Palm, 5, new Scalar(0, 255, 0), 15);
       
    	/**手指底端點*/
    	/*Imgproc.circle(mRgba, bThumbDip, 5, new Scalar(0, 0, 255), 5);
    	Imgproc.circle(mRgba, bIndexDip, 5, new Scalar(0, 0, 255), 5);
    	Imgproc.circle(mRgba, bMiddleDip, 5, new Scalar(0, 0, 255), 5);
    	Imgproc.circle(mRgba, bRingDip, 5, new Scalar(0, 0, 255), 5);
    	Imgproc.circle(mRgba, bLittleDip, 5, new Scalar(0, 0, 255), 5);
    	//Dip → Tip Line
    	Imgproc.line(mRgba, bThumbTip, bThumbDip, new Scalar(255, 0, 255),5);
    	Imgproc.line(mRgba, bIndexTip, bIndexDip, new Scalar(255, 0, 255),5);
    	Imgproc.line(mRgba, bMiddleTip, bMiddleDip, new Scalar(255, 0, 255),5);
    	Imgproc.line(mRgba, bRingTip, bRingDip, new Scalar(255, 0, 255),5);
    	Imgproc.line(mRgba, bLittleTip, bLittleDip, new Scalar(255, 0, 255),5);*/ 
    
    	/**第二指節*/
    	Imgproc.circle(mRgba, bIndKnucklesDown, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bMidKnucklesDown, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bRinKnucklesDown, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bLitKnucklesDown, 5, new Scalar(0, 0, 255), 15);
	
    	/**第一指節*/
    	Imgproc.circle(mRgba, bThumbCenter, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bIndKnucklesTop, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bMidKnucklesTop, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bRinKnucklesTop, 5, new Scalar(0, 0, 255), 15);
    	Imgproc.circle(mRgba, bLitKnucklesTop, 5, new Scalar(0, 0, 255), 15);
    	
    	/**手背掌心*/
    	Imgproc.circle(mRgba, BackPalmCenter, 5, new Scalar(0, 0, 0), 15);
	}
	
	public void FrontDrawPoint()
	{
		/** 指尖 -頂點 */
		Imgproc.circle(mRgba, fThumbTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, fIndexTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, fMiddleTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, fRingTip, 5, new Scalar(255, 0, 0), 15); 
		Imgproc.circle(mRgba, fLittleTip, 5, new Scalar(255, 0, 0), 15);

		/**指谷*/
		Imgproc.circle(mRgba, Thu_ThuL, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Thu_ThuR, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Thu_Palm, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Thu_Ind, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Ind_Ind, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Ind_Mid, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Mid_Rin, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Rin_Lit, 5, new Scalar(0, 255, 0), 15);
		Imgproc.circle(mRgba, Lit_Lit, 5, new Scalar(0, 255, 0), 15);

		/**手指底端點*/
		/*Imgproc.circle(mRgba, ThumbDip, 5, new Scalar(0, 0, 255), 5);
		Imgproc.circle(mRgba, IndexDip, 5, new Scalar(0, 0, 255), 5);
		Imgproc.circle(mRgba, MiddleDip, 5, new Scalar(0, 0, 255), 5);
		Imgproc.circle(mRgba, RingDip, 5, new Scalar(0, 0, 255), 5);
		Imgproc.circle(mRgba, LittleDip, 5, new Scalar(0, 0, 255), 5);
		//手指 tip-dip 連線
		Imgproc.line(mRgba, fThumbTip, ThumbDip, new Scalar(255, 0, 255),5);
		Imgproc.line(mRgba, fIndexTip, IndexDip, new Scalar(255, 0, 255),5);
		Imgproc.line(mRgba, fMiddleTip, MiddleDip, new Scalar(255, 0, 255),5);
		Imgproc.line(mRgba, fRingTip, RingDip, new Scalar(255, 0, 255),5);
		Imgproc.line(mRgba, fLittleTip, LittleDip, new Scalar(255, 0, 255),5);*/

		/**第二指節*/           
		Imgproc.circle(mRgba, IndKnucklesDown, 5, new Scalar(0, 0, 255), 15);
		Imgproc.circle(mRgba, MidKnucklesDown, 5, new Scalar(0, 0, 255), 15);
		Imgproc.circle(mRgba, RinKnucklesDown, 5, new Scalar(0, 0, 255), 15);
		Imgproc.circle(mRgba, LitKnucklesDown, 5, new Scalar(0, 0, 255), 15);

		/**第一指節*/
		Imgproc.circle(mRgba, ThuKnucklesTop, 5, new Scalar(0, 0, 255), 15); //拇指指節
		Imgproc.circle(mRgba, IndKnucklesTop, 5, new Scalar(0, 0, 255), 15);
		Imgproc.circle(mRgba, MidKnucklesTop, 5, new Scalar(0, 0, 255), 15);
		Imgproc.circle(mRgba, RinKnucklesTop, 5, new Scalar(0, 0, 255), 15);
		Imgproc.circle(mRgba, LitKnucklesTop, 5, new Scalar(0, 0, 255), 15);
		
		/**手掌掌心*/
    	Imgproc.circle(mRgba, FrontPalmCenter, 5, new Scalar(0, 0, 0), 15);
	}
	
	/**搜尋穴道的方法 - 共22個穴道*/
	//手掌穴道
	public void FrnotAcupoints()
	{
		///*** 手太陰肺經  ***///
		//少商穴(Shaoshang)
		Point Shaoshang = new Point((37*fThumbTip.x + 11*ThumbDip.x)/48, (3*ThuKnucklesTop.y + 5*fThumbTip.y)/8);
		Imgproc.circle(mRgba, Shaoshang, 5, new Scalar(255, 0, 0), 15);	
		IBShaoshangX = (float) Shaoshang.x;
    	IBShaoshangY = (float) Shaoshang.y;
		//魚際穴(Thenar)
    	Point G = new Point((fThumbTip.x + Thu_Palm.x)/2, (fThumbTip.y + Thu_Palm.y)/2);
		Point Thenar = new Point((385*ThumbDip.x + 495*G.x - 368*fThumbTip.x)/512, (385*ThumbDip.y + 495*G.y - 368*fThumbTip.y)/512);
		Imgproc.circle(mRgba, Thenar, 5, new Scalar(255, 0, 0), 15);
		IBThenarX = (float) Thenar.x;
    	IBThenarY = (float) Thenar.y;
		//太淵穴(Taiyuan)
		Point Taiyuan = new Point((4608*Thu_Palm.x - 133*ThumbDip.x - 171*Thu_ThuL.x - 208*fThumbTip.x)/4096, (4608*Thu_Palm.y - 133*ThumbDip.y - 171*Thu_ThuL.y - 208*fThumbTip.y)/4096);
		Imgproc.circle(mRgba, Taiyuan, 5, new Scalar(255, 0, 0), 15);
		IBTaiyuanX = (float) Taiyuan.x;
    	IBTaiyuanY = (float) Taiyuan.y;
		
		///*** 手厥陰心包經  ***///
		//大陵穴(DaLing)
		Point O = new Point(2*Thenar.x - ThumbDip.x, 2*Thenar.y - ThumbDip.y);
		Point DaLing = new Point((3*O.x + Taiyuan.x)/4, (3*O.y + Taiyuan.y)/4);
		Imgproc.circle(mRgba, DaLing, 5, new Scalar(255, 0, 0), 15);
		IBDaLingX = (float) DaLing.x;
    	IBDaLingY = (float) DaLing.y;
		//勞宮穴(Laogong)
		Point Laogong = new Point(FrontPalmCenter.x + Ind_Mid.x - MiddleDip.x, FrontPalmCenter.y + Ind_Mid.y - MiddleDip.y);
		Imgproc.circle(mRgba, Laogong, 5, new Scalar(255, 0, 0), 15);
		IBLaogongX = (float) Laogong.x;
    	IBLaogongY = (float) Laogong.y;
		//中衝穴(Zhongchong)：中指指尖
		Point Zhongchong = new Point(fMiddleTip.x, fMiddleTip.y);
		Imgproc.circle(mRgba, Zhongchong, 5, new Scalar(255, 0, 0), 15);
		IBZhongchongX = (float) Zhongchong.x;
    	IBZhongchongY = (float) Zhongchong.y;
		
		///*** 手少陰心經  ***///
		//神門穴(Shenmen)
		Point Shenmen = new Point(DaLing.x, 2*DaLing.y - Taiyuan.y);	
		Imgproc.circle(mRgba, Shenmen, 5, new Scalar(255, 0, 0), 15);
		IBShenmenX = (float) Shenmen.x;
    	IBShenmenY = (float) Shenmen.y;
		//少府穴(ShaoFu)：Line(Laogong → ShaoFu)： Line(ShaoFu → Lit_Lit) = 1 ：1，且與PalmCenter同高(因為必須感情線相交)
		Point ShaoFu = new Point(3*FrontPalmCenter.x - 2*Laogong.x, 3*FrontPalmCenter.y - 2*Laogong.y);
		Imgproc.circle(mRgba, ShaoFu, 5, new Scalar(255, 0, 0), 15);
		IBShaoFuX = (float) ShaoFu.x;
    	IBShaoFuY = (float) ShaoFu.y;
    }
	
	//手背穴道
	public void BackAcupoints()
	{
		///*** 手少陰心經  ***///
		//少衝穴(Shaochong)
		Point Shaochong = new Point((bLitKnucklesTop.x + 2*bLittleCenter.x - bLit_Lit.x)/2, (bLitKnucklesTop.y + 2*bLittleCenter.y - bLit_Lit.y)/2);
		Imgproc.circle(mRgba, Shaochong, 5, new Scalar(255, 0, 0), 15);
		IBShaochongX = (float) Shaochong.x;
	    IBShaochongY = (float) Shaochong.y;
				
		///*** 手陽明大腸經 ***///
		//商陽穴(ShangYang)
		Point ShangYang = new Point((3*bIndexTip.x + 5*bIndKnucklesTop.x)/8, (3*bIndexTip.y + 25*bIndKnucklesTop.y - 12*bIndRightLineCenter.y)/16);
		Imgproc.circle(mRgba, ShangYang, 5, new Scalar(255, 0, 0), 15);
		IBShangyangX = (float) ShangYang.x;
	    IBShangyangY = (float) ShangYang.y;
		//二間穴(Erjian)
		Point Erjian = new Point((3*bIndLeftLineCenter.x + 61*bInd_Ind.x)/64, (3*bIndLeftLineCenter.y + 61*bInd_Ind.y)/64);		
		Imgproc.circle(mRgba, Erjian, 5, new Scalar(255, 0, 0), 15);
		IBErjianX = (float) Erjian.x;
	    IBErjianY = (float) Erjian.y;
		//三間穴(Sanjian)
	    Point Sanjian = new Point((181*BackPalmCenter.x + 75*bThumbTip.x)/256, (181*BackPalmCenter.y + 75*bThumbTip.y)/256);
		//Point Sanjian = new Point((371*BackPalmCenter.x + 154*bThumbTip.x - 13*Erjian.x)/512, (371*BackPalmCenter.y + 154*bThumbTip.y - 13*Erjian.y)/512);
		Imgproc.circle(mRgba, Sanjian, 5, new Scalar(255, 0, 0), 15);
		IBSanjianX = (float) Sanjian.x;
	    IBSanjianY = (float) Sanjian.y;
		//合谷穴(Hoku)	
	    Point I = new Point((7*BackPalmCenter.x - 3*bMidKnucklesDown.x)/4, (7*BackPalmCenter.y - 3*bMidKnucklesDown.y)/4); 
	    Point J = new Point((58*Sanjian.x - 29*Erjian.x + 3*bThumbTip.x)/32, (58*Sanjian.y - 29*Erjian.y + 3*bThumbTip.y)/32);
	    Point K = new Point((3*I.x + bThumbTip.x)/4, (3*I.y + bThumbTip.y)/4);
		Point Hoku = new Point((J.x + K.x)/2, (J.y + K.y)/2);
		Imgproc.circle(mRgba, Hoku, 5, new Scalar(255, 0, 0), 15);
		IBHokuX = (float) Hoku.x;
	    IBHokuY = (float) Hoku.y;
		//陽谿穴(Yangxi)
	    Point M = new Point((3*BackPalmCenter.x + 5*J.x)/8, (3*BackPalmCenter.y + 5*J.y)/8);
	    Point Yangxi = new Point((67*M.x - 35*bIndexDip.x)/32, (67*M.y - 35*bIndexDip.y)/32);
	    //Point Yangxi = new Point((102*BackPalmCenter.x + 167*J.x - 141*bIndexDip.x)/128, (102*BackPalmCenter.y + 167*J.y - 141*bIndexDip.y)/128);
		Imgproc.circle(mRgba, Yangxi, 5, new Scalar(255, 0, 0), 15);
		IBYangxiX = (float) Yangxi.x;
	    IBYangxiY = (float) Yangxi.y;
		
		///***手太陽小腸經***///
		//少澤穴(Shaoze)
		Point Shaoze = new Point((11*bLittleTip.x + 10*bLitKnucklesTop.x - 5*Shaochong.x)/16, (11*bLittleTip.y + 10*bLitKnucklesTop.y - 5*Shaochong.y)/16);
		Imgproc.circle(mRgba, Shaoze, 5, new Scalar(255, 0, 0), 15);
		IBShaozeX = (float) Shaoze.x;
		IBShaozeY = (float) Shaoze.y;
		//前谷穴(Qiangu)：Line(Shaoze → bLit_Lit)： Line(bLit_Lit → Qiangu) = 1 ：27
		Point Qiangu = new Point((29*bLit_Lit.x - Shaoze.x)/28, (29*bLit_Lit.y - Shaoze.y)/28);
		Imgproc.circle(mRgba, Qiangu, 5, new Scalar(255, 0, 0), 15);	
		IBQianguX = (float) Qiangu.x;
		IBQianguY = (float) Qiangu.y;
		//後谿穴(Houxi)
		Point Houxi = new Point((28*bLit_Lit.x + 4*bLittleDip.x - 16*bLitKnucklesDown.x)/16 , (28*bLit_Lit.y + 4*bLittleDip.y - 16*bLitKnucklesDown.y)/16);
		Imgproc.circle(mRgba, Houxi, 5, new Scalar(255, 0, 0), 15);
		IBHouxiX = (float) Houxi.x;
	    IBHouxiY = (float) Houxi.y;
		//腕骨穴(Carpus)
		Point Carpus = new Point((41*BackPalmCenter.x - 25*bIndexDip.x)/16, bLit_Palm.y);
		Imgproc.circle(mRgba, Carpus, 5, new Scalar(255, 0, 0), 15);
		IBCarpusX = (float) Carpus.x;
	    IBCarpusY = (float) Carpus.y;
		//陽谷穴(Yanggu)
		Point L = new Point(2*Hoku.x - bThumbTip.x, 2*Hoku.y - bThumbTip.y);
		Point Yanggu = new Point((63*L.x + Yangxi.x)/64, (63*L.y + Yangxi.y)/64);
		Imgproc.circle(mRgba, Yanggu, 5, new Scalar(255, 0, 0), 15);
		IBYangguX = (float) Yanggu.x;
	    IBYangguY = (float) Yanggu.y;
		
		///***手少陽三焦經***///
		//關衝穴(Guanchong)
	    Point N = new Point((13*bRingTip.x + 3*bRin_Lit.x)/16,(13*bRingTip.y + 3*bRin_Lit.y)/16);
	    Point Guanchong = new Point((7*N.x - 3*bRinKnucklesTop.x)/4, (7*N.y - 3*bRinKnucklesTop.y)/4);
		//Point Guanchong = new Point((79*bRingTip.x + 17*bRin_Lit.x - 32*bRinKnucklesTop.x)/64, (79*bRingTip.y + 17*bRin_Lit.y - 32*bRinKnucklesTop.y)/64);
		Imgproc.circle(mRgba, Guanchong, 5, new Scalar(255, 0, 0), 15);
		IBGuanchongX = (float) Guanchong.x;
	    IBGuanchongY = (float) Guanchong.y;
		//液門穴(Yimen)
		Point Yimen = new Point((BackPalmCenter.x + 3*bLittleDip.x)/4, (BackPalmCenter.y + 3*bLittleDip.y)/4);
		Imgproc.circle(mRgba, Yimen, 5, new Scalar(255, 0, 0), 15);	
		IBYimenX = (float) Yimen.x;
	    IBYimenY = (float) Yimen.y;
		//中渚穴(Zhongzhuu)
		Point Zhongzhuu = new Point((265*BackPalmCenter.x + 375*bLit_Lit.x - 384*Yimen.x)/256, (265*BackPalmCenter.y + 375*bLit_Lit.y - 384*Yimen.y)/256);
		Imgproc.circle(mRgba, Zhongzhuu, 5, new Scalar(255, 0, 0), 15);
		IBZhongzhuuX = (float) Zhongzhuu.x;
	    IBZhongzhuuY = (float) Zhongzhuu.y;
		//陽池穴(Yangchi)		
		Point Yangchi = new Point((49*Yanggu.x + 15*Yangxi.x)/64, (49*Yanggu.y + 15*Yangxi.y)/64);
		Imgproc.circle(mRgba, Yangchi, 5, new Scalar(255, 0, 0), 15);
		IBYangchiX = (float) Yangchi.x;
	    IBYangchiY = (float) Yangchi.y;
	}
	
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
	    
		int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2 ;

        int x = (int)event.getX() - xOffset;
        int y = (int)event.getY() - yOffset;

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows))
        {
        	return false;
        }
        
        Rect touchedRect = new Rect();

        touchedRect.x = (x>5) ? x-5 : 0;
        touchedRect.y = (y>5) ? y-5 : 0;

        touchedRect.width = (x+5 < cols) ? x + 5 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+5 < rows) ? y + 5 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);
        Mat touchedRegionHsv = new Mat();
        
        if(StartOnTounch)
        {
        	Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2YCrCb);
        	//Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);
        	
            // Calculate average color of touched region
            //計算點擊區塊的平均顏色，以此顏色框選區域範圍
            mBlobColorHsv = Core.sumElems(touchedRegionHsv);
            int pointCount = touchedRect.width*touchedRect.height;
            for (int i = 0; i < mBlobColorHsv.val.length; i++)
                mBlobColorHsv.val[i] /= pointCount;

            mDetector.setHsvColor(mBlobColorHsv);
            
            Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SpectrumSize);
            
            //BackHandDetection = true;
            mIsColorSelected = true;    
        }      
        touchedRegionRgba.release();
        touchedRegionHsv.release();   
        
        return true; // don't need subsequent touch events
	}
	
}
