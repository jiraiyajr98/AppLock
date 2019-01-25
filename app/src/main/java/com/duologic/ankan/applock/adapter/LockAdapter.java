package com.duologic.ankan.applock.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.duologic.ankan.applock.R;
import com.duologic.ankan.applock.dataobjects.LockObject;
import static com.duologic.ankan.applock.ExampleService.selectedList;

import java.util.List;

public class LockAdapter extends RecyclerView.Adapter<LockAdapter.LockHolder> {

    private Context context;
    private List<LockObject> list;


    public LockAdapter(Context context, List<LockObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LockHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new LockHolder(LayoutInflater.from(context).inflate(R.layout.lock_items,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LockHolder lockHolder, int i) {

        LockObject object = list.get(i);

        lockHolder.pkg_name.setText(object.getPkg_Name());
        lockHolder.aSwitch.setChecked(object.getStatus());

        lockHolder.name.setText(object.getName());

        try {
            lockHolder.icon.setImageDrawable( context.getPackageManager().getApplicationIcon(object.getPkg_Name()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        lockHolder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lockHolder.aSwitch.isChecked()) {
                    lockHolder.aSwitch.setChecked(true);
                    list.get(lockHolder.getAdapterPosition()).setStatus(true);
                    selectedList.add(lockHolder.pkg_name.getText().toString());
                    Toast.makeText(context, " Set "+lockHolder.pkg_name.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else {
                    lockHolder.aSwitch.setChecked(false);
                    list.get(lockHolder.getAdapterPosition()).setStatus(false);
                    selectedList.remove(lockHolder.pkg_name.getText().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LockHolder extends RecyclerView.ViewHolder{

        TextView pkg_name;
        Switch aSwitch;
        TextView name;
        ImageView icon;

         LockHolder(@NonNull View itemView) {
            super(itemView);

            pkg_name = (TextView) itemView.findViewById(R.id.textView);
            aSwitch = (Switch) itemView.findViewById(R.id.switch1);
            name = (TextView)itemView.findViewById(R.id.name);
            icon = (ImageView)itemView.findViewById(R.id.icon);
        }
    }

}
