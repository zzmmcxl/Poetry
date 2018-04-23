package com.shi.result;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shi.ContentActivity;
import com.shi.R;
import com.shi.bean.ShiBean;

import java.util.ArrayList;

/**
 * Author: Yunr
 * Date: 2018-04-21 15:34
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private ArrayList<ShiBean> showShiBeans;

    public ResultAdapter(ArrayList<ShiBean> shiBeans) {

        showShiBeans = shiBeans;
    }

    @NonNull
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ViewHolder holder, int position) {
        ShiBean shiBean = showShiBeans.get(position);
        holder.title.setText(shiBean.getTitle());
        holder.author.setText(shiBean.getAuthor());
        holder.content.setText(shiBean.getDetail());
        holder.setListener(shiBean);
    }

    @Override
    public int getItemCount() {
        return showShiBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView author;
        TextView content;
        private ShiBean shiBean;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
            content = itemView.findViewById(R.id.item_content);
        }

        public void setListener(ShiBean shiBean) {
            this.shiBean = shiBean;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ContentActivity.class);
            intent.putExtra("data", shiBean);
            v.getContext().startActivity(intent);
        }
    }
}
