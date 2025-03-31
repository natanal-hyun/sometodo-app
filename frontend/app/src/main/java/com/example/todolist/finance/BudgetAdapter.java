package com.example.todolist.finance;

import static com.example.todolist.finance.FinanceActivity.formatting;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    List<Categories> bCategory;

    List<Predict> bPredict;

    int year;
    int month;

    public List<Predict> getbPredict() {
        return bPredict;
    }

    public BudgetAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pay_predict_budgetting_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories category = bCategory.get(position);
        Long categoriesId = category.getId();  //1,2,3,4,5,6,7
        Predict matchedPredict = null; //카테고리 아이디 7개중에 하나를 mPredict에서 찾을것임
        for (Predict k : bPredict) {
            Log.v("RETROFIT3", "onResponse: 성공, 결과 \n" + k.toString());
        }
        if (bPredict != null) {
            for (Predict p : bPredict) {
                if (p.getCategoryId() != null && p.getCategoryId().equals(categoriesId)) {
                    matchedPredict = p;
                    break;
                }
            }
        } else {
            System.out.println(bPredict + "는 null입니다. ");

        }
        holder.setItemView(matchedPredict, category);
    }

    @Override
    public int getItemCount() {
        if (bCategory == null) return 0;
        return bCategory.size()-1;
    }

    public void setbData(List<Categories> cList, List<Predict> pList) {
        bCategory = cList;
        bPredict = pList;
        year = bPredict.get(0).getYear();
        month = bPredict.get(0).getMonth();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        EditText budget;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            budget = itemView.findViewById(R.id.budget);
        }

        public void setItemView(Predict p, Categories category) {
            int imageId = this.itemView.getContext().getResources().getIdentifier(
                    category.getImageName(), "drawable", this.itemView.getContext().getPackageName());
            categoryImage.setImageResource(imageId);
            categoryName.setText(category.getCategoryName());
            if (p != null) budget.setHint(formatting(p.getPredict()));
            else budget.setHint(formatting(0));
            budget.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (p != null) {
                        Predict changedBudget = bPredict.get(getAdapterPosition());
                        try {
                            changedBudget.setPredict(Integer.parseInt(editable.toString()));
                        } catch (NumberFormatException e) {
                            changedBudget.setPredict(0);
                        }
                    } else {
                        String inputText = budget.getText().toString();
                        int newPredictValue = inputText.isEmpty() ? 0 : Integer.parseInt(inputText);

                        bPredict.add(new Predict(null, year, month, category.getId(), category,newPredictValue, 0));

                    }
                }
            });

        }
    }
}
