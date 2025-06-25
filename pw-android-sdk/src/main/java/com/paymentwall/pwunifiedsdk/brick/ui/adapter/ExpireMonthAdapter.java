package com.paymentwall.pwunifiedsdk.brick.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paymentwall.pwunifiedsdk.R;
import com.paymentwall.pwunifiedsdk.util.PwUtils;

import java.util.List;

/**
 * Created by nguyen.anh on 4/1/2016.
 */
public class ExpireMonthAdapter extends BaseAdapter {

    private List<Integer> arrMonths;
    private Context context;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private IExpirateMonth listener;


    public ExpireMonthAdapter(Context mContext, List<Integer> months, IExpirateMonth _listener) {
        this.context = mContext;
        this.arrMonths = months;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = _listener;
    }

    @Override
    public int getCount() {
        return arrMonths.size();
    }

    @Override
    public Integer getItem(int i) {
        return arrMonths.get(i);
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
            view = inflater.inflate(R.layout.row_expire_month, null);
            holder.tvMonth = (TextView) view.findViewById(R.id.tvMonth);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(i == selectedPosition){
            holder.tvMonth.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(context, "monthButtonPressed"));
        }else{
            holder.tvMonth.setBackgroundDrawable(PwUtils.getDrawableFromAttribute(context, "monthButtonNormal"));
        }

        holder.tvMonth.setText(context.getResources().getString(arrMonths.get(i)));

        holder.tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickMonth(i);
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
        TextView tvMonth;
    }

    public interface IExpirateMonth{
        void onClickMonth(int position);
    }

}