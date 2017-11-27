package org.fmod.example;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends Activity implements OnTouchListener, Runnable
{
	private TextView mTxtScreen;
	private Thread mThread;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);

    	// Create the text area
    	mTxtScreen = new TextView(this);
    	mTxtScreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f);
    	mTxtScreen.setTypeface(Typeface.MONOSPACE);

        // Create the buttons
        Button[] buttons = new Button[9];
        for (int i = 0; i < buttons.length; i++)
        {
        	buttons[i] = new Button(this);
        	buttons[i].setText(getButtonLabel(i));
        	buttons[i].setOnTouchListener(this);
        	buttons[i].setId(i);
        }
        
        // Create the button row layouts
        LinearLayout llTopRowButtons = new LinearLayout(this);
        llTopRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout llMiddleRowButtons = new LinearLayout(this);
        llMiddleRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout llBottomRowButtons = new LinearLayout(this);
        llBottomRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        
        // Create the main view layout
        LinearLayout llView = new LinearLayout(this);
        llView.setOrientation(LinearLayout.VERTICAL);       

        // Create layout parameters
        LayoutParams lpLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        
        // Set up the view hierarchy
        llTopRowButtons.addView(buttons[0], lpLayout);
        llTopRowButtons.addView(buttons[6], lpLayout);
        llTopRowButtons.addView(buttons[1], lpLayout);
        llMiddleRowButtons.addView(buttons[4], lpLayout);
        llMiddleRowButtons.addView(buttons[8], lpLayout);
        llMiddleRowButtons.addView(buttons[5], lpLayout);
        llBottomRowButtons.addView(buttons[2], lpLayout);
        llBottomRowButtons.addView(buttons[7], lpLayout);
        llBottomRowButtons.addView(buttons[3], lpLayout);
        llView.addView(mTxtScreen, lpLayout);
        llView.addView(llTopRowButtons);
        llView.addView(llMiddleRowButtons);
        llView.addView(llBottomRowButtons);
        
        setContentView(llView);

        // Request necessary permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

			RxPermissions rxPermission = new RxPermissions(MainActivity.this);
			rxPermission.request(
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)
					.subscribe(new Consumer<Boolean>() {
						@Override
						public void accept(Boolean granted) {
							if (granted) {

							} else {
								// 用户拒绝了该权限，并且选中『不再询问』
								//    Timber.e(permission.name + "  is denied.");
								showDialog();
								//     ToastUtil.showToast(permission.name + "被拒绝,请到设置中打开");
							}
						}
					});

        }

        org.fmod.FMOD.init(this);
        
        mThread = new Thread(this, "Example Main");
        mThread.start();
        
        setStateCreate();
    }
	
    @Override
    protected void onStart()
    {
    	super.onStart();
    	setStateStart();
    }
    
    @Override
    protected void onStop()
    {
    	setStateStop();
    	super.onStop();
    }
    
    @Override
    protected void onDestroy()
    {
    	setStateDestroy();
    	
    	try
    	{
    		mThread.join();
    	}
    	catch (InterruptedException e) { }
    	
    	org.fmod.FMOD.close();
    	
    	super.onDestroy();
    }
    
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent)
	{
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
		{
			buttonDown(view.getId());	
		}
		else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
		{
			buttonUp(view.getId());	
		}			
	    
		return true;
	}

	@Override
	public void run()
	{
        main();
	}
	
	public void updateScreen(final String text)
	{
		runOnUiThread(new Runnable()
		{
	        @Override
	        public void run()
	        {
	            mTxtScreen.setText(text);
	        }
		});
	}
	
	private native String getButtonLabel(int index);
	private native void buttonDown(int index);
	private native void buttonUp(int index);
	private native void setStateCreate();
	private native void setStateStart();
	private native void setStateStop();
	private native void setStateDestroy();
	private native void main();
	
    static 
    {
    	/*
    	 * To simplify our examples we try to load all possible FMOD
    	 * libraries, the Android.mk will copy in the correct ones
    	 * for each example. For real products you would just load
    	 * 'fmod' and if you use the FMOD Studio tool you would also
    	 * load 'fmodstudio'.
    	 */


    	// Try logging libraries...
    	try { System.loadLibrary("fmodL");
		}catch (UnsatisfiedLinkError e) {
    		e.getStackTrace();
		}

		// Try release libraries...
		try { System.loadLibrary("fmod");
		}catch (UnsatisfiedLinkError e) {
			e.getStackTrace();
		}

    	//System.loadLibrary("stlport_shared");
        System.loadLibrary("qq_voicer");
    }


	public void showDialog() {
		//创建对话框创建器
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//设置对话框显示小图标
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		//设置标题
		builder.setTitle("权限申请");
		//设置正文
		builder.setMessage("在设置-应用-录音-权限 中开启录音功能");

		//添加确定按钮点击事件
		builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//这里用来跳到手机设置页，方便用户开启权限
				Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
				intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		//添加取消按钮点击事件
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		//使用构建器创建出对话框对象
		AlertDialog dialog = builder.create();
		dialog.show();//显示对话框
	}

}
