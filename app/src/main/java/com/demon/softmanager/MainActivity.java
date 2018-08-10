package com.demon.softmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etApp;
    private TextView tvApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvApp = findViewById(R.id.tv_appinfo);
        etApp = findViewById(R.id.et_appname);
        findViewById(R.id.btn_getapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etAppString = etApp.getText().toString().trim();
                if (TextUtils.isEmpty(etAppString)) {
                    return;
                }
                ResolveInfo resolveInfo = findInstallAppDetails(MainActivity.this, etAppString);
                if (resolveInfo == null) {
                    Toast.makeText(MainActivity.this, "程序不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvApp.setText("包名：" + resolveInfo.activityInfo.packageName + "\n程序入口：" + resolveInfo.activityInfo.name);
            }
        });
        findViewById(R.id.btn_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SoftActivity.class));
            }
        });

        findViewById(R.id.btn_Service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, SoftService.class));
            }
        });
    }


    /**
     * 根据应用程序的名称获取对应的信息
     * 获取图标：resolveInfo.activityInfo.loadIcon(packageManager)
     * 获取包名：resolveInfo.activityInfo.packageName
     * 获取程序入口activity：resolveInfo.activityInfo.name
     *
     * @param context
     * @param appLabel
     * @return
     */
    public ResolveInfo findInstallAppDetails(Context context, String appLabel) {
        PackageManager packageManager = context.getPackageManager();
        //匹配程序的入口
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //查询
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (int i = 0; i < resolveInfos.size(); i++) {
            String appName = resolveInfos.get(i).loadLabel(packageManager).toString();
            if (appLabel.equals(appName)) {
                return resolveInfos.get(i);
            }
        }
        //程序不存在
        return null;
    }


}
