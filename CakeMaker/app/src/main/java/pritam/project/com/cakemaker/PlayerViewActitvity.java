package pritam.project.com.cakemaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.fragments.RecipeStepDetailViewFragment;
import pritam.project.com.cakemaker.model.Steps;

/**
 * Created by Pritam on 27-01-2018.
 */

public class PlayerViewActitvity extends AppCompatActivity {

    public static final String TOTAL_RECIPES_BUNDLE = "total_recipes_bundle";
    public static final String TOTAL_STEPS = "total_steps";
    public static final String STEP_INDEX = "step_index";
    public static final String RECIPE_TITLE = "recipe_title";
    public static final String STEP_BACK_STACK = "step_back_stack";

    private String mRecipeNameTitle;
    private ArrayList<Steps> mStepsArrayList = new ArrayList<>();
    private int selectedIndex;

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_player_view);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {

            savedInstanceState = getIntent().getExtras();

            mStepsArrayList = savedInstanceState.getParcelableArrayList(TOTAL_STEPS);
            selectedIndex = savedInstanceState.getInt(STEP_INDEX);
            mRecipeNameTitle = savedInstanceState.getString(RECIPE_TITLE);

            FragmentManager fm = getSupportFragmentManager();
            RecipeStepDetailViewFragment fragment = new RecipeStepDetailViewFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_player_container, fragment)
                    .commit();

        } else {
            mRecipeNameTitle = savedInstanceState.getString(RECIPE_TITLE);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mRecipeNameTitle);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return;
                }
                PlayerViewActitvity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_TITLE, mRecipeNameTitle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
