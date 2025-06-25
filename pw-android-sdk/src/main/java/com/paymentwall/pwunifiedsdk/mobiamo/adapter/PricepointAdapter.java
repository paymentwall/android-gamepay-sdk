package com.paymentwall.pwunifiedsdk.mobiamo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.util.SmartLog;

import java.util.List;

/**
 * Created by nguyen.anh on 8/1/2016.
 */

public class PricepointAdapter extends BaseAdapter {

    private List<String> arrPricepoint;
    private Context context;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private IPricepoint listener;


    public PricepointAdapter(Context mContext, List<String> list, IPricepoint _listener) {
        this.context = mContext;
        this.arrPricepoint= list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = _listener;
    }

    @Override
    public int getCount() {
        return arrPricepoint.size();
    }

    @Override
    public String getItem(int i) {
        return arrPricepoint.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_pricepoint, null);
            holder.tvPricepoint = (TextView) view.findViewById(R.id.tvPricepoint);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SmartLog.i(getClass().getSimpleName(), i + "--" + arrPricepoint.get(i) == null?"NULL":arrPricepoint.get(i));

        if(i == selectedPosition){
            holder.tvPricepoint.setBackgroundResource(R.drawable.btn_price_point_pressed);
        }else{
            holder.tvPricepoint.setBackgroundResource(R.drawable.btn_price_point);
        }

        holder.tvPricepoint.setText(arrPricepoint.get(i));

        holder.tvPricepoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickPricepoint(i);
            }
        });

        return view;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    class ViewHolder {
        TextView tvPricepoint;
    }

    public interface IPricepoint{
        void onClickPricepoint(int position);
    }

}
