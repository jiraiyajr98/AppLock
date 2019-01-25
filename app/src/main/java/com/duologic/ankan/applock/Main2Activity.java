package com.duologic.ankan.applock;


import android.content.pm.PackageInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.duologic.ankan.applock.adapter.LockAdapter;
import com.duologic.ankan.applock.dataobjects.LockObject;

import java.util.List;

import static com.duologic.ankan.applock.ExampleService.list;

public class Main2Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        LockAdapter adapter = new LockAdapter(Main2Activity.this,list);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));

        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);


        for(int i=0;i<apps.size();i++) {
            PackageInfo p = apps.get(i);

//            AppInfo newInfo = new AppInfo();
//            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
//            newInfo.pname = p.packageName;
//            newInfo.versionName = p.versionName;
//            newInfo.versionCode = p.versionCode;
//            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
//            res.add(newInfo);

            list.add(new LockObject(p.packageName,false,
                    p.applicationInfo.loadLabel(getPackageManager()).toString()));
        }


        adapter.notifyItemRangeInserted(0,list.size());
    }
}
