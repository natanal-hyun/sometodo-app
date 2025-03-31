package com.example.todolist.finance;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

public class MonthPayAdapter extends RecyclerView.Adapter<MonthPayAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    List<Categories> cList = new ArrayList<>();
    static List<MoneyFlow> mList = new ArrayList<>();


    public MonthPayAdapter(@NonNull Context context) {
        mContext=context;
        this.mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pay_predict_rv_act_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoneyFlow moneyFlow=mList.get(position);
        holder.setItemView(moneyFlow);
    }


    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public void setmData(List<Categories>cList, List<MoneyFlow> mList){
        this.cList=cList;
        Log.v("cList11",cList.get(0).toString());
        this.mList=mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        ImageView day_category_image;
        TextView day_content;
        TextView day_price;
        TextView day_category_name;
        TextView moneyFlowDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day_category_image=itemView.findViewById(R.id.day_category_image);
            day_content=itemView.findViewById(R.id.day_content);
            day_price=itemView.findViewById(R.id.day_price);
            day_category_name=itemView.findViewById(R.id.day_category_name);
            moneyFlowDate=itemView.findViewById(R.id.moneyFlowDate);

            itemView.setOnClickListener(this);
        }
        public void setItemView(MoneyFlow moneyFlow){
            Long categoryId=moneyFlow.getCategoriesId();
            Categories category=null;
            for(Categories c:cList){
                if(c.getId().equals(categoryId)) category=c;
            }
            if (category==null) return;

            int imageId = this.itemView.getContext().getResources().getIdentifier(
                    category.getImageName(), "drawable", this.itemView.getContext().getPackageName());
            day_category_image.setImageResource(imageId);
            day_category_name.setText(category.getCategoryName());
            day_price.setText(FinanceActivity.formatting(moneyFlow.getCost())+"");
            day_content.setText(moneyFlow.getContent());
            moneyFlowDate.setText(moneyFlow.getNowDate()+"");
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            Intent intent = new Intent(mContext, EditPay.class);
            intent.putExtra("position",pos);
            intent.putExtra("fromActivity",MonthPay.class.getSimpleName());
            ((MonthPay)mContext).getLauncher().launch(intent);
        }
    }

}
