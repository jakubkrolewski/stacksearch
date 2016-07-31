package pl.jkrolewski.stacksearch.rxhooks;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.internal.schedulers.ImmediateScheduler;
import rx.plugins.RxJavaHooks;
import rx.schedulers.TestScheduler;

public class TestRxJavaSchedulersHookManager {

    private static final TestScheduler TEST_SCHEDULER = new TestScheduler();

    public static void installImmediateScheduler() {
        installScheduler(ImmediateScheduler.INSTANCE);
    }

    public static TestScheduler getTestScheduler() {
        return TEST_SCHEDULER;
    }

    private static void installScheduler(Scheduler newScheduler) {
        resetHooks();
        RxJavaHooks.setOnIOScheduler(scheduler -> newScheduler);
        RxJavaHooks.setOnNewThreadScheduler(scheduler -> newScheduler);
        // using immediate here will cause the main thread to block
        RxJavaHooks.setOnComputationScheduler(scheduler -> TEST_SCHEDULER);

        RxAndroidPlugins.getInstance().registerSchedulersHook(new TestRxAndroidSchedulersHook(newScheduler));
    }

    private static void resetHooks() {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();
    }

    private static class TestRxAndroidSchedulersHook extends RxAndroidSchedulersHook {

        private final Scheduler scheduler;

        private TestRxAndroidSchedulersHook(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public Scheduler getMainThreadScheduler() {
            return scheduler;
        }
    }
}