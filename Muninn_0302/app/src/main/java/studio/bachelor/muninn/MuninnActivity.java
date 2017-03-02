package studio.bachelor.muninn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;

import studio.bachelor.draft.Draft;
import studio.bachelor.draft.DraftDirector;
import studio.bachelor.draft.DraftView;

public class MuninnActivity extends AppCompatActivity {//activity提供介面 這是程式的主介面(4大元件之一)
    private static final int SELECT_PICTURE = 21101;
    private static final int SELECT_ZIP = 21102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//初始化用
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muninn);//顯示layout的名字
        final Context context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        findViewById(R.id.select_photo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {//按鈕
                swithcToGallery();
            }
        });
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {//按鈕
                switchToSetting();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按鈕
                DraftDirector.instance.exportToZip();
            }
        });
        findViewById(R.id.sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按鈕
                DraftDirector.instance.showSignPad(context);
            }
        });
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//按鈕
                switchToZIPBrowsing();
            }
        });
        findViewById(R.id.btnSound).setSoundEffectsEnabled(false);//不履行預設button音效
        findViewById(R.id.btnSound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Muninn.sound_Ding.seekTo(0);
                Muninn.sound_Ding.start();
            }
        });
    }

    private void swithcToGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_photo_string)), SELECT_PICTURE);
    }

    private void switchToZIPBrowsing() {//上傳功能 尚未完成
        Intent intent = new Intent();
        intent.setType("application/zip");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_ZIP);
    }

    private void switchToSetting() {
        Intent act = new Intent(getApplicationContext(), SettingActivity.class);//intent 用來開啟另一個activity (開啟設定畫面)
        startActivity(act);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();//URI就是一個位址 與URL不同是URL專指interent上的位址
                DraftDirector.instance.setBirdviewImageByUri(uri);
            }
            else if(requestCode == SELECT_ZIP) {
                Uri uri = data.getData();
                DraftDirector.instance.uploadToSever(uri);
            }
        }
    }
}
