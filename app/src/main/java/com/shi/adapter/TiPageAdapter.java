package com.shi.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shi.BeforeNextListener;
import com.shi.R;
import com.shi.bean.TiBean;

import java.util.ArrayList;

/**
 * Author: Yunr
 * Date: 2018-04-21 17:35
 */
public class TiPageAdapter extends PagerAdapter {

    private BeforeNextListener listener;
    private ArrayList<TiBean> data;
    private ArrayList<String> key;

    public TiPageAdapter(ArrayList<TiBean> data, BeforeNextListener listener) {
        this.data = data;
        key = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            key.add("");
        }

        this.listener = listener;
    }

    public int getAllUnit() {
        if (key.size() == 0) {
            return 0;
        }

        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            TiBean tiBean = data.get(i);
            if (tiBean.getKey().contentEquals(key.get(i))) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.ti_item, null, false);
        final TiBean tiBean = data.get(position);
        TextView tiText = itemView.findViewById(R.id.ti_text);
        View editView = itemView.findViewById(R.id.edit_layout);
        final EditText inputText = itemView.findViewById(R.id.input_edit);
        View submit = itemView.findViewById(R.id.submit);
        final ViewGroup selectView = itemView.findViewById(R.id.select_layout);
        View before = itemView.findViewById(R.id.before);
        View next = itemView.findViewById(R.id.next);
        tiText.setText(tiBean.getTi());
        if (tiBean.isEdit()) {
            editView.setVisibility(View.VISIBLE);
            selectView.setVisibility(View.GONE);
        } else {
            editView.setVisibility(View.GONE);
            selectView.setVisibility(View.VISIBLE);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s=((TextView) v).getText().toString();
                    key.add(position,s);

                    for (int i = 0; i < selectView.getChildCount(); i++) {
                        selectView.getChildAt(i)
                                .setBackgroundColor(
                                        selectView.getResources().getColor(R.color.colorPrimary));
                    }

                    v.setBackgroundResource(R.color.colorAccent);

                    if (position <= getCount() - 1) {
                        listener.onNext(position + 1);
                    }
                }
            };

            for (int i = 0; i < tiBean.getSelectStr().length; i++) {
                TextView textView = (TextView) LayoutInflater.from(container.getContext())
                        .inflate(R.layout.ti_select_item, selectView, false);
                textView.setText(tiBean.getSelectStr()[i]);
                textView.setOnClickListener(clickListener);
                selectView.addView(textView);
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key.add(position, inputText.getText().toString() + "");
                if (position <= getCount() - 1) {
                    listener.onNext(position + 1);
                }
            }
        });

        if (position == 0) {
            before.setVisibility(View.GONE);
        } else {
            before.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tiBean.isEdit()) {
                        key.add(position, inputText.getText().toString() + "");
                    } else {
                        key.add(position,"");
                    }

                    listener.onBefore(position - 1);
                }
            });
        }

        if (position == getCount() - 1) {
            next.setVisibility(View.GONE);
        } else {
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tiBean.isEdit()) {
                        key.add(position, inputText.getText().toString() + "");
                    } else {
                        key.add(position,"");
                    }

                    listener.onNext(position + 1);
                }
            });
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
