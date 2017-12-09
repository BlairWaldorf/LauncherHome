package com.example.jeffersonmendes.launcherhome;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jefferson Mendes on 09/12/2017.
 */

public class AppsListActivity extends Activity {
    private PackageManager manager;
    private List<AppDetail> apps;
    private GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        loadApps();
        loadListView();
        addClickListener();
    }

    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<AppDetail>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }

    private void loadListView(){
        grid = findViewById(R.id.apps_grid);

        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this,
                R.layout.list_item,
                apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageView appIcon = convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);

                TextView appLabel = convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);

                /*TextView appName = convertView.findViewById(R.id.item_app_name);
                appName.setText(apps.get(position).name);*/

                return convertView;
            }
        };

        grid.setAdapter(adapter);
    }

   private void addClickListener(){
       grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
                AppsListActivity.this.startActivity(i);
            }
    });
    }
}

