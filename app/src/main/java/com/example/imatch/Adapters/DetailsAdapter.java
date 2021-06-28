package com.example.imatch.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imatch.JavaClasses.Detail;
import com.example.imatch.R;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.viewholder>{

    Context settingActivity;
    ArrayList<Detail> UserDataArray;

    public DetailsAdapter(Context setting_page, ArrayList<Detail> dataStoringclassArrayList) {

        this.settingActivity = setting_page;
        this.UserDataArray = dataStoringclassArrayList ;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(settingActivity);
        View view = layoutInflater.inflate(R.layout.setting_data_list_layout, parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Detail detailclass = UserDataArray.get(position);


        String DataName = detailclass.dataname ;
        String DataDetail = detailclass.dataDetail ;

        holder.datanameTextView.setText(DataName);
        holder.datanameDetailTextView.setText(DataDetail);

        holder.datanameDetailTextView.setOnClickListener(v -> {

        });

    }

    @Override
    public int getItemCount() {
        return UserDataArray.size() ;
    }



    class  viewholder extends RecyclerView.ViewHolder{

        TextView datanameTextView ,datanameDetailTextView ;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            datanameTextView = itemView.findViewById(R.id.logged_In_Nametxt);
            datanameDetailTextView = itemView.findViewById(R.id.logged_In_Datatxt);


        }
    }
}
