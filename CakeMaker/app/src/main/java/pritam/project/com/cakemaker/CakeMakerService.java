package pritam.project.com.cakemaker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Pritam on 15-01-2018.
 */

public class CakeMakerService extends IntentService {

    public static final String RECIPES = "RECIPES_LIST";

    public CakeMakerService() {
        super("CakeMakerService");
    }

    public static void startActionCakeMaker(Context context, ArrayList<String> cakeList) {
        Intent intent = new Intent(context, CakeMakerService.class);
        intent.putExtra(RECIPES, cakeList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<String> cakeRecipeList = intent.getExtras().getStringArrayList(RECIPES);
            handleCakeMaker(cakeRecipeList);
        }
    }

    private void handleCakeMaker(ArrayList<String> recipeList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(RECIPES, recipeList);
        sendBroadcast(intent);
    }
}
