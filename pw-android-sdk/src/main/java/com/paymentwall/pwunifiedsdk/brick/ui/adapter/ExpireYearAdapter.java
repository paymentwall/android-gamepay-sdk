package com.paymentwall.pwunifiedsdk.brick.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paymentwall.pwunifiedsdk.R;

import java.util.List;

/**
 * Created by nguyen.anh on 4/1/2016.
 */
public class ExpireYearAdapter extends BaseAdapter {

    private List<Integer> arrYears;
    private Context context;
    private LayoutInflater inflater;

    public ExpireYearAdapter(Context mContext, List<Integer> years){
        this.context = mContext;
        this.arrYears = years;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrYears.size();
    }

    @Override
    public Integer getItem(int i) {
        return arrYears.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if(view == null){
            holder= new ViewHolder();
            view = inflater.inflate(R.layout.row_expire_year, null);
            holder.tvYear = (TextView)view.findViewById(R.id.tvYear);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }

        holder.tvYear.setText(arrYears.get(i) + "");


        return view;
    }

    class ViewHolder{
        TextView tvYear;
    }


}
