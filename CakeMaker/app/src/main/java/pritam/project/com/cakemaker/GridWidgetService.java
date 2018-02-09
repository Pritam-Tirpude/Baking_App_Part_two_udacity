package pritam.project.com.cakemaker;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import static pritam.project.com.cakemaker.CakeMakerWidget.RECIPES;
import static pritam.project.com.cakemaker.CakeMakerWidget.ingredientsList;
import java.util.List;

/**
 * Created by Pritam on 16-01-2018.
 */

public class GridWidgetService extends RemoteViewsService{
    List<String> remoteViewStringList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteFactory(this.getApplicationContext(), intent);
    }

    class GridRemoteFactory implements RemoteViewsService.RemoteViewsFactory{

        Context mContext = null;

        public GridRemoteFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteViewStringList = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteViewStringList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_item);

            views.setTextViewText(R.id.widget_grid_view_item, remoteViewStringList.get(i));

            Intent fillInIntent = new Intent();

            views.setOnClickFillInIntent(R.id.widget_grid_view_item, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
