package org.fmod.example;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.tbruyelle.rxpermissions2.RxPermissions;
import org.fmod.FMOD;
import java.io.File;
import io.reactivex.functions.Consumer;

/**
 * Created by Xionghu on 2017/11/24.
 * Desc:
 */

public class QQVoiceActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FMOD.init(this);
        setContentView(R.layout.activity_qq_voice);


    }

    public void mFix(final View btn) {
//        Log.d("QQVoiceActivity", "55555");
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "myvoice.wav";
//        Log.d("QQVoiceActivity", path);
//        EffectUtils.fix(path, EffectUtils.MODE_NORMAL);


        RxPermissions rxPermission = new RxPermissions(QQVoiceActivity.this);
        rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "myvoice.wav";
                           //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "drumloop.wav";
                            Log.d("QQVoiceActivity", path);
                            switch (btn.getId()) {
                                case R.id.btn_normal:
                                    EffectUtils.fix(path, EffectUtils.MODE_NORMAL);
                                    break;

                                case R.id.btn_luoli:
                                    EffectUtils.fix(path, EffectUtils.MODE_LUOLI);
                                    break;

                                case R.id.btn_dashu:
                                    EffectUtils.fix(path, EffectUtils.MODE_DASHU);
                                    break;

                                case R.id.btn_jingsong:
                                    EffectUtils.fix(path, EffectUtils.MODE_JINGSONG);
                                    break;

                                case R.id.btn_gaoguai:
                                    EffectUtils.fix(path, EffectUtils.MODE_GAOGUAI);
                                    break;

                                case R.id.btn_kongling:
                                    EffectUtils.fix(path, EffectUtils.MODE_KONGLING);
                                    break;

                                default:
                                    break;
                            }


                        } else {
                              Toast.makeText(QQVoiceActivity.this, "权限被拒绝,请到设置中打开",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FMOD.close();
    }
}
