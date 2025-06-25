package com.paymentwall.pwunifiedsdk.mobiamo.core;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.mobiamo.adapter.PricepointAdapter;

import java.util.List;

/**
 * Created by nguyen.anh on 11/9/2016.
 */

public class PackageAdapter extends BaseAdapter {

    private Context context;
    private List<MobiamoPacks> arrPacks;
    private LayoutInflater inflater;
    private int selectedPosition = -1;

    public PackageAdapter(Context context, List<MobiamoPacks> arrPacks){
        this.context = context;
        this.arrPacks = arrPacks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrPacks.size();
    }

    @Override
    public Object getItem(int position) {
        return arrPacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_spn_dropdown, null);
            holder.tv = (TextView) view.findViewById(R.id.row_spn_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MobiamoPacks pack = arrPacks.get(position);
        if(pack != null){
            holder.tv.setText(pack.getQuantity() + " " + pack.getName() + " " + context.getString(R.string._for) + " " +  pack.getAmount() + " " + pack.getCurrency());
        }

        if(selectedPosition >= 0 && selectedPosition == position){
            holder.tv.setBackgroundColor(context.getResources().getColor(R.color.primary_color));
        }
        else{
            holder.tv.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }


        return view;
    }

    class ViewHolder{
        private TextView tv;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
