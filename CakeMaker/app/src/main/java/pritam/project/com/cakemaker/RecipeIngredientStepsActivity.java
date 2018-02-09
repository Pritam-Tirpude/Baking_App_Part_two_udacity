package pritam.project.com.cakemaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.model.Recipe;
import pritam.project.com.cakemaker.model.Steps;

public class RecipeIngredientStepsActivity extends AppCompatActivity {
    public static final String TOTAL_RECIPES_BUNDLE = "total_recipes_bundle";
    public static final String TOTAL_STEPS = "total_steps";
    public static final String STEP_INDEX = "step_index";
    public static final String RECIPE_TITLE = "recipe_title";
    public static final String STEP_BACK_STACK = "step_back_stack";

    private ArrayList<Recipe> mRecipesList;
    private List<Steps> mStepsList;
    private String recipeName;

    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredient_steps);
        ButterKnife.bind(this);

        savedInstanceState = getIntent().getExtras();

        if (savedInstanceState != null) {
            mRecipesList = new ArrayList<>();
            mRecipesList = savedInstanceState.getParcelableArrayList(TOTAL_RECIPES_BUNDLE);
            mStepsList = savedInstanceState.getParcelableArrayList(TOTAL_STEPS);
            recipeName = mRecipesList.get(0).getName();
        } else {
            recipeName = savedInstanceState.getString(RECIPE_TITLE);
        }

        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
                RecipeIngredientStepsActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TOTAL_RECIPES_BUNDLE, mRecipesList);
        outState.putString(RECIPE_TITLE, recipeName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
