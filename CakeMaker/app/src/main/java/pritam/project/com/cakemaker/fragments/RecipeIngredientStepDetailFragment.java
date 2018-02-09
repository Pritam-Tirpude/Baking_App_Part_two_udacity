package pritam.project.com.cakemaker.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.CakeMakerService;
import pritam.project.com.cakemaker.PlayerViewActitvity;
import pritam.project.com.cakemaker.R;
import pritam.project.com.cakemaker.adapters.RecipeIngredientStepsAdapter;
import pritam.project.com.cakemaker.model.Ingredient;
import pritam.project.com.cakemaker.model.Recipe;
import pritam.project.com.cakemaker.model.Steps;

/**
 * Created by Pritam on 11-01-2018.
 */

public class RecipeIngredientStepDetailFragment extends Fragment implements
        RecipeIngredientStepsAdapter.ListStepItemClickListener{

    public static final String TOTAL_RECIPES_BUNDLE = "total_recipes_bundle";
    public static final String TOTAL_STEPS = "total_steps";
    public static final String STEP_INDEX = "step_index";
    public static final String RECIPE_TITLE = "recipe_title";
    public static final String STEP_BACK_STACK = "step_back_stack";

    public static final String KEY_PASS = "key_pass";

    private ArrayList<Recipe> mRecipesList;
    private String mRecipeNameTitle;

    private RecipeIngredientStepsAdapter mRecipeIngredientStepsAdapter;

    @BindView(R.id.steps_recycler_view)
    RecyclerView mRecyclerViewDetail;
    @BindView(R.id.text_view_steps)
    TextView mTextViewIngredients;

    public RecipeIngredientStepDetailFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_recipe_ingredient_steps_detail, container, false);
        ButterKnife.bind(this, view);

        mRecyclerViewDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewDetail.setHasFixedSize(true);

        savedInstanceState = getActivity().getIntent().getExtras();

        if (savedInstanceState != null) {
            mRecipesList = getActivity().getIntent().getParcelableArrayListExtra(TOTAL_RECIPES_BUNDLE);
        } else {
            mRecipesList = getArguments().getParcelableArrayList(TOTAL_RECIPES_BUNDLE);
        }

        mRecipeNameTitle = mRecipesList.get(0).getName();
        List<Ingredient> ingredients = mRecipesList.get(0).getIngredients();

        StringBuilder ingredientString = new StringBuilder();
        double quantity;
        String ingredientName;
        String measure;

        for (int i = 0; i < ingredients.size(); i++) {
            ingredientName = ingredients.get(i).getIngredient();
            quantity = ingredients.get(i).getQuantity();
            measure = ingredients.get(i).getMeasure();


            ingredientString.append("\u25CF ");
            ingredientString.append(ingredientName);
            ingredientString.append(" (");
            ingredientString.append(quantity);
            ingredientString.append(" ");
            ingredientString.append(measure);
            ingredientString.append(")");
            ingredientString.append("\n");
        }

        mTextViewIngredients.setText(ingredientString.toString());

        List<Steps> mStepsList = mRecipesList.get(0).getSteps();

        final ArrayList<String> recipeIngredientsList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            ingredients.forEach((a) ->
                    recipeIngredientsList.add(a.getIngredient() + "\n" +
                            "Quantity: " + a.getQuantity().toString() + "\n" + "Measure: " + a.getMeasure() + "\n"));
        } else {

            for (int i = 0; i < ingredients.size(); i++) {

                recipeIngredientsList.add(ingredients.get(i).getIngredient() + "\n" +
                        "Quantity: " + ingredients.get(i).getQuantity().toString() + "\n" + "Measure: " +
                        ingredients.get(i).getMeasure() + "\n");
            }
        }


        mRecipeIngredientStepsAdapter = new RecipeIngredientStepsAdapter(mStepsList, getContext(),
                RecipeIngredientStepDetailFragment.this);

        mRecyclerViewDetail.setAdapter(mRecipeIngredientStepsAdapter);

        getActivity().setTitle(mRecipeNameTitle.toString());

        CakeMakerService.startActionCakeMaker(getContext(), recipeIngredientsList);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(TOTAL_RECIPES_BUNDLE, mRecipesList);
        outState.putString(RECIPE_TITLE, mRecipeNameTitle);
    }

    @Override
    public void onListItemClick(List<Steps> stepsList, int clickedItemIndex, String recipeName) {
        recipeName = getActivity().getTitle().toString();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TOTAL_STEPS, (ArrayList<Steps>) stepsList);
        bundle.putInt(STEP_INDEX, clickedItemIndex);
        bundle.putString(RECIPE_TITLE, recipeName);

        RecipeStepDetailViewFragment fragment = new RecipeStepDetailViewFragment();
        fragment.setArguments(bundle);

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_two, fragment).addToBackStack(STEP_BACK_STACK)
                    .commit();
        } else {
            Intent intent = new Intent(getContext(), PlayerViewActitvity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}
