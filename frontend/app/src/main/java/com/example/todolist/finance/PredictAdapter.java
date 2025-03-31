package com.example.todolist.finance;

import static com.example.todolist.finance.FinanceActivity.formatting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.List;

public class PredictAdapter extends RecyclerView.Adapter<PredictAdapter.ViewHolder> {
    List<Predict> mPredict;
    List<Categories> mCategory;
    private Context mContext;
    private LayoutInflater mInflater;

    public PredictAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pay_predict_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories category = mCategory.get(position);
        Long categoriesId = category.getId();  //1,2,3,4,5,6,7
        Predict matchedPredict = null; //카테고리 아이디 7개중에 하나를 mPredict에서 찾을것임
//        for(Predict k : mPredict) {
//            Log.v("RETROFIT3", "onResponse: 성공, 결과 \n" + k.toString());
//        }
        if (mPredict != null) {
            for (Predict p : mPredict) {
                if (p.getCategoryId() != null && p.getCategoryId().equals(categoriesId)) {
                    matchedPredict = p;
                    break;
                }
            }
        } else {
            System.out.println(mPredict + "는 null입니다. ");

        }
        holder.setItemView(matchedPredict, category);

    }


    @Override
    public int getItemCount() {
        if (mCategory == null) return 0;
        return mCategory.size();
    }

    public void setmData(List<Categories> cList, List<Predict> pList) {
        mCategory = cList;
        mPredict = pList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView categoryName;
        ImageView categoryImage;
        TextView predict;
        TextView extraPredict;
        TextView monthlyCost;
        ProgressBar budgetRatio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            predict = itemView.findViewById(R.id.predict);
            extraPredict = itemView.findViewById(R.id.extraPredict);
            monthlyCost = itemView.findViewById(R.id.monthlyCost);
            budgetRatio=itemView.findViewById(R.id.budgetRatio);


            monthlyCost.setOnClickListener(this);
        }

        public void setItemView(Predict p, Categories category) {
            if (p != null) {
                categoryName.setText(category.getCategoryName());
                int imageId = this.itemView.getContext().getResources().getIdentifier(
                        category.getImageName(), "drawable", this.itemView.getContext().getPackageName());
                categoryImage.setImageResource(imageId);
                predict.setText(formatting(p.getPredict()));
                monthlyCost.setText(formatting(p.getMonthCost()));
                int isOver = p.getPredict() - p.getMonthCost();
                if (isOver >= 0){
                    extraPredict.setText(formatting(isOver) + " 남음");
                    extraPredict.setTextColor(Color.BLUE);
                }
                else {
                    extraPredict.setText((formatting(Math.abs(isOver))) + " 과소비");
                    extraPredict.setTextColor(Color.RED);
                }
                int monthAmount=p.getMonthCost();
                int mothBudget=p.getPredict();
                int percentage=(int) ((monthAmount/(float)mothBudget)*100);
                budgetRatio.setProgress(percentage);
            } else {
                categoryName.setText(category.getCategoryName());
                int imageId = this.itemView.getContext().getResources().getIdentifier(
                        category.getImageName(), "drawable", this.itemView.getContext().getPackageName());
                categoryImage.setImageResource(imageId);
                predict.setText(formatting(0));
                extraPredict.setText(formatting(0));
                monthlyCost.setText(formatting(0));
                budgetRatio.setProgress(0);
            }



        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            if(mPredict.size()!=0) {
                Intent intent = new Intent(mContext, MonthPay.class);
                intent.putExtra("Year", mPredict.get(pos).getYear());
                intent.putExtra("Month", mPredict.get(pos).getMonth());
                intent.putExtra("categoryID", mPredict.get(pos).getCategoryId());
                ((PredictActivity) mContext).getLauncher().launch(intent);
            }else{
                Toast.makeText(mContext, "편집할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
