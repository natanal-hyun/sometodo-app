package com.example.todolist.finance;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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

import com.example.todolist.MainActivity;
import com.example.todolist.R;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    static public List<MoneyFlow> moneyFlowList;
    private Context mContext;
    private MoneyFlow moneyFlow;
    private String date;
    private Categories category;

    public DayAdapter(Context context,List<MoneyFlow> moneyFlowList){
        this.mInflater=LayoutInflater.from(context); //뷰의 인플레이터다.
        mContext=context; //콘텍스트는 뷰다.
        this.moneyFlowList=moneyFlowList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.pay_day_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position<moneyFlowList.size()) {
            moneyFlow=moneyFlowList.get(position);//리스트중 하나의 참조자
            category= moneyFlow.getCategory();
            moneyFlow.setCategoriesId(category.getId());
            holder.setItemView(moneyFlow, category);
        }else{
            holder.setFinalView();
        }
    }

    @Override
    public int getItemCount() {
        return moneyFlowList.size()+1;
    }

    public void setDate(String date){
        this.date=date;
    }



    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView dayCategoryImage;
        TextView dayPrice;
        TextView dayContent;
        TextView dayCategoryName;

        int position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dayCategoryImage=itemView.findViewById(R.id.day_category_image);
            dayContent=itemView.findViewById(R.id.day_content);
            dayCategoryName=itemView.findViewById(R.id.day_category_name);
            dayPrice=itemView.findViewById(R.id.day_price);

            itemView.setOnClickListener(this);
        }

        public void setItemView(MoneyFlow moneyFlow,Categories category){
            int imageId= this.itemView.getContext().getResources().getIdentifier(
                    category.getImageName(),"drawable",this.itemView.getContext().getPackageName());
            dayContent.setVisibility(VISIBLE);
            dayCategoryName.setVisibility(VISIBLE);
            dayCategoryImage.setImageResource(imageId);
            dayContent.setText(moneyFlow.getContent());
            dayCategoryName.setText(category.getCategoryName());
            dayPrice.setText(FinanceActivity.formatting(moneyFlow.getCost()));
        }
        public void setFinalView(){
            dayCategoryImage.setImageResource(R.drawable.add);
            dayContent.setVisibility(INVISIBLE);
            dayCategoryName.setVisibility(INVISIBLE);
            dayPrice.setText("내역 추가");
        }

        @Override
        public void onClick(View view) {

          int position=getAdapterPosition();

            if (position==moneyFlowList.size()) { // "내역 추가" 뷰 클릭 시
                Intent intent = new Intent(mContext, AddPay.class);
                intent.putExtra("Date", date);
                ((FinanceActivity)mContext).getLauncher().launch(intent);
            } else { // 일반 아이템 클릭 시
                Log.v("post",position+"");
                Intent intent = new Intent(mContext, EditPay.class);
                intent.putExtra("position",position);
                intent.putExtra("fromActivity",MainActivity.class.getSimpleName());
                ((FinanceActivity)mContext).getLauncher().launch(intent);
            }
        }
    }
}
