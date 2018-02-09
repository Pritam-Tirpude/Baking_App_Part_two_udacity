package pritam.project.com.cakemaker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pritam.project.com.cakemaker.fragments.RecipeFragment;
import pritam.project.com.cakemaker.idlingresource.SimpleIdlingResource;

public class RecipeActivity extends AppCompatActivity {

    private boolean isConnected;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @BindView(R.id.empty_text_view)
    TextView mEmptyTextView;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected) {

            FragmentManager fm = getSupportFragmentManager();

            RecipeFragment fragment = new RecipeFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_recipe_container, fragment)
                    .commit();
        } else {
            mEmptyTextView.setText(getResources().getString(R.string.empty_text_label));
        }


        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        getIdlingResource();
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
