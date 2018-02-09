package pritam.project.com.cakemaker.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.CakeMakerService;
import pritam.project.com.cakemaker.R;
import pritam.project.com.cakemaker.RecipeActivity;
import pritam.project.com.cakemaker.RecipeIngredientStepsActivity;
import pritam.project.com.cakemaker.adapters.RecipeAdapter;
import pritam.project.com.cakemaker.idlingresource.SimpleIdlingResource;
import pritam.project.com.cakemaker.model.Recipe;
import pritam.project.com.cakemaker.retrofit.RetrofitNetworkUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pritam on 11-01-2018.
 */

public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    public static final String TAG = RecipeFragment.class.getSimpleName();

    public static final String TOTAL_RECIPES_BUNDLE = "total_recipes_bundle";

    private RecipeAdapter mRecipeAdapter;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView mRecipeRecyclerView;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecipeRecyclerView.setHasFixedSize(true);

        int orientation = getResources().getConfiguration().orientation;

        switch (orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
                break;
            default:
                mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RetrofitNetworkUtil client = retrofit.create(RetrofitNetworkUtil.class);
        Call<ArrayList<Recipe>> call = client.getRecipes();

        SimpleIdlingResource idlingResource = (SimpleIdlingResource) ((RecipeActivity)getActivity()).getIdlingResource();

        if (idlingResource != null){
            idlingResource.setIdleState(false);
        }

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipes = response.body();

                Bundle recipesTotalBundle = new Bundle();
                recipesTotalBundle.putParcelableArrayList(TOTAL_RECIPES_BUNDLE, recipes);

                mRecipeAdapter = new RecipeAdapter(recipes, getContext(), RecipeFragment.this);

                mRecipeRecyclerView.setAdapter(mRecipeAdapter);

                if (idlingResource != null){
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.no_recipes_found, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onListItemClick(Recipe clickedItemIndex) {
        Bundle recipeBundle = new Bundle();
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(clickedItemIndex);
        recipeBundle.putParcelableArrayList(TOTAL_RECIPES_BUNDLE, recipes);

        Intent intent = new Intent(getContext(), RecipeIngredientStepsActivity.class);
        intent.putExtras(recipeBundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
