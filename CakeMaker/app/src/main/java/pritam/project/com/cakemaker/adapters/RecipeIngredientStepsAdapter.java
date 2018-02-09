package pritam.project.com.cakemaker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.R;
import pritam.project.com.cakemaker.model.Steps;

/**
 * Created by Pritam on 11-01-2018.
 */

public class RecipeIngredientStepsAdapter extends RecyclerView.Adapter<RecipeIngredientStepsAdapter.RecipeIngredientStepHolder> {

    private List<Steps> mStepsList;
    private Context mContext;
    private String mRecipeName;

    private ListStepItemClickListener mListStepItemClickListener;

    public interface ListStepItemClickListener {
        void onListItemClick(List<Steps> stepsList, int clickedItemIndex, String recipeName);
    }

    public RecipeIngredientStepsAdapter(ListStepItemClickListener listStepItemClickListener) {
        mListStepItemClickListener = listStepItemClickListener;
    }

    public RecipeIngredientStepsAdapter(List<Steps> stepsList, Context context, ListStepItemClickListener listener) {
        mStepsList = stepsList;
        mContext = context;
        mListStepItemClickListener = listener;
    }

    @Override
    public RecipeIngredientStepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_recipe_ingredient_steps_detail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        RecipeIngredientStepHolder viewHolder = new RecipeIngredientStepHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeIngredientStepHolder holder, int position) {
        holder.mTextViewIngredients.setText(mStepsList.get(position).getId() + ". " +
                mStepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }

    public class RecipeIngredientStepHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_steps)
        TextView mTextViewIngredients;

        public RecipeIngredientStepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListStepItemClickListener.onListItemClick(mStepsList, clickedPosition, mRecipeName);
        }
    }
}
