package pritam.project.com.cakemaker.retrofit;

import java.util.ArrayList;

import pritam.project.com.cakemaker.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Pritam on 11-01-2018.
 */

public interface RetrofitNetworkUtil {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
