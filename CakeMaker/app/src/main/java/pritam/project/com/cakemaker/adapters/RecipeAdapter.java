package pritam.project.com.cakemaker.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.R;
import pritam.project.com.cakemaker.model.Recipe;

/**
 * Created by Pritam on 11-01-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private ArrayList<Recipe> mRecipeList;
    private Context mContext;
    private ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedItemIndex);
    }

    public RecipeAdapter(ArrayList<Recipe> recipeList, Context context, ListItemClickListener listItemClickListener) {
        mRecipeList = recipeList;
        mContext = context;
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeHolder recipeHolder = new RecipeHolder(view);

        return recipeHolder;
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        holder.mTextViewRecipeTitle.setText(mRecipeList.get(position).getName());

        String recipeImageUrl = mRecipeList.get(position).getImage();

        if (recipeImageUrl != "") {
            Uri imageUri = Uri.parse(recipeImageUrl).buildUpon().build();
            Picasso.with(mContext).load(imageUri).into(holder.mRecipeImageView);
        } else if (holder.mTextViewRecipeTitle.getText().equals("Nutella Pie")){
            Picasso.with(mContext).load(R.drawable.nutella_cake).into(holder.mRecipeImageView);
        }else if (holder.mTextViewRecipeTitle.getText().equals("Brownies")){
            Picasso.with(mContext).load(R.drawable.brownies_cake).into(holder.mRecipeImageView);
        }else if (holder.mTextViewRecipeTitle.getText().equals("Yellow Cake")){
            Picasso.with(mContext).load(R.drawable.yellow_cake).into(holder.mRecipeImageView);
        }else if (holder.mTextViewRecipeTitle.getText().equals("Cheesecake")){
            Picasso.with(mContext).load(R.drawable.cheese_cake).into(holder.mRecipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_recipe_name)
        TextView mTextViewRecipeTitle;

        @BindView(R.id.recipe_image_view)
        ImageView mRecipeImageView;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(mRecipeList.get(clickedPosition));
        }
    }
}
