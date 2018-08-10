package com.demon.softmanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SoftActivity extends Activity implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> list = null;
    private ListView softlist = null;
    private Context mContext;
    private PackageManager mPackageManager;
    private List<ResolveInfo> mAllApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft);
        setTitle("程序管理器");
        mContext = this;
        mPackageManager = getPackageManager();
        softlist = findViewById(R.id.softlist);

        //应用过滤条件
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        softlist.setAdapter(new MyAdapter(mContext, mAllApps));
        softlist.setOnItemClickListener(this);
    }


    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<ResolveInfo> resInfo;
        private ResolveInfo res;
        private LayoutInflater infater = null;

        public MyAdapter(Context context, List<ResolveInfo> resInfo) {
            this.context = context;
            this.resInfo = resInfo;
            infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return resInfo.size();
        }

        @Override
        public Object getItem(int arg0) {

            return arg0;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null || convertView.getTag() == null) {
                convertView = infater.inflate(R.layout.list_soft, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //获取应用程序包名，程序名称，程序图标
            res = resInfo.get(position);
            holder.appIcon.setImageDrawable(res.loadIcon(mPackageManager));
            holder.tvAppLabel.setText(res.loadLabel(mPackageManager).toString());
            holder.tvPkgName.setText(res.activityInfo.packageName + '\n' + res.activityInfo.name);
            return convertView;
        }
    }

    //设定界面布局
    class ViewHolder {
        ImageView appIcon;
        TextView tvAppLabel;
        TextView tvPkgName;

        public ViewHolder(View view) {
            this.appIcon = view.findViewById(R.id.img);
            this.tvAppLabel = view.findViewById(R.id.name);
            this.tvPkgName = view.findViewById(R.id.desc);
        }
    }

    /**
     * 单击应用程序后进入系统应用管理界面
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        ResolveInfo res = mAllApps.get(position);
        //该应用的包名和主Activity
        String pkg = res.activityInfo.packageName;
        String cls = res.activityInfo.name;

        ComponentName componet = new ComponentName(pkg, cls);

        Intent i = new Intent();
        i.setComponent(componet);
        startActivity(i);

    }

}
