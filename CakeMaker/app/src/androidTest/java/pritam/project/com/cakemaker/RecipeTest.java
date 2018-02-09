package pritam.project.com.cakemaker;

import android.os.Handler;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Pritam on 22-01-2018.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeTest {

    public static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new
            ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){

        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void recipeTest() {

        onView(withId(R.id.recipe_recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void recipeDetailTest() {

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(firstView(withId(R.id.steps_recycler_view)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(firstView(withId(R.id.simple_player_view))).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource(){
        if (mIdlingResource != null){
           IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    private <T> Matcher<T> firstView(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("should return first matching item");
            }
        };
    }
}
