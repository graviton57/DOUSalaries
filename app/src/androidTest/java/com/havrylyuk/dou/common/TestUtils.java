package com.havrylyuk.dou.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.havrylyuk.dou.data.local.db.DouDbHelper;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.io.File;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by Igor Havrylyuk on 22.09.2017.
 */

public class TestUtils {

    public static <VH extends RecyclerView.ViewHolder> ViewAction actionOnItemViewAtPosition(
            int position, @IdRes int viewId, ViewAction viewAction) {
        return new ActionOnItemViewAtPositionViewAction(position, viewId, viewAction);
    }

    private static final class ActionOnItemViewAtPositionViewAction<VH extends RecyclerView.ViewHolder>
            implements ViewAction {

        private final int position;
        private final ViewAction viewAction;
        private final int viewId;

        private ActionOnItemViewAtPositionViewAction(int position, @IdRes int viewId,
                                                     ViewAction viewAction) {
            this.position = position;
            this.viewAction = viewAction;
            this.viewId = viewId;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[] {
                    ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()
            });
        }

        public String getDescription() {
            return "actionOnItemAtPosition performing ViewAction: "
                    + this.viewAction.getDescription()
                    + " on item at position: "
                    + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            (new ScrollToPositionViewAction(this.position)).perform(uiController, view);
            uiController.loopMainThreadUntilIdle();

            View targetView = recyclerView.getChildAt(this.position).findViewById(this.viewId);

            if (targetView == null) {
                throw (new PerformException.Builder()).withActionDescription(this.toString())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new IllegalStateException(
                                "No view with id "
                                        + this.viewId
                                        + " found at position: "
                                        + this.position)
                        )
                        .build();
            }
            else {
                this.viewAction.perform(uiController, targetView);
            }
        }
    }

    private static final class ScrollToPositionViewAction implements ViewAction {

        private final int position;

        private ScrollToPositionViewAction(int position) {
            this.position = position;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[] {
                    ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()
            });
        }

        public String getDescription() {
            return "scroll RecyclerView to position: " + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.scrollToPosition(this.position);
        }
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static String fixDatabasePatch(Context context){
        File f = context.getDatabasePath(DouDbHelper.DATABASE_NAME);
        f.getParentFile().mkdirs();
        return f.getPath();
    }

    private static void rotateToLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private static void rotateToPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void rotateOrientation(Activity activity) {
        int currentOrientation = activity.getResources().getConfiguration().orientation;

        switch (currentOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                rotateToPortrait(activity);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                rotateToLandscape(activity);
                break;
            default:
                rotateToLandscape(activity);
        }
    }

    /**
     * Returns the content description for the navigation button view in the toolbar.
     */
    public static String getToolbarNavigationContentDescription(
            @NonNull Activity activity, @IdRes int toolbar1) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbar1);
        if (toolbar != null) {
            return (String) toolbar.getNavigationContentDescription();
        } else {
            throw new RuntimeException("No toolbar found.");
        }
    }

    /**
     * Gets an Activity in the RESUMED stage.
     * <p>
     * This method should never be called from the Main thread. In certain situations there might
     * be more than one Activities in RESUMED stage, but only one is returned.
     * See {@link ActivityLifecycleMonitor}.
     */
    public static Activity getCurrentActivity() throws IllegalStateException {
        // The array is just to wrap the Activity and be able to access it from the Runnable.
        final Activity[] resumedActivity = new Activity[1];

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    resumedActivity[0] = (Activity) resumedActivities.iterator().next();
                } else {
                    throw new IllegalStateException("No Activity in stage RESUMED");
                }
            }
        });
        return resumedActivity[0];
    }
}
