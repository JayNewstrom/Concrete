package com.jaynewstrom.concretetesting;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concrete.ConcreteWall;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public final class ConcreteWallTest {

    @Test public void ensureContextCreatedFromWallCanBeUsedToInject() {
        ConcreteWall wall = Concrete.pourFoundation(new ContextTestModule(), true);
        Context context = wall.createContext(InstrumentationRegistry.getTargetContext());
        ContextTestTarget target = new ContextTestTarget();
        Concrete.inject(context, target);
        assertThat(target.injectedString).isSameAs("Context test string");
    }

    @Module(injects = ContextTestTarget.class) static final class ContextTestModule {

        @Provides String provideString() {
            return "Context test string";
        }
    }

    static final class ContextTestTarget {

        @Inject String injectedString;
    }

    @Test public void whenViewIsCreatedWithLayoutInflaterFromChildContextEnsureItCanBeInjectedWithContext() {
        ConcreteWall wall = Concrete.pourFoundation(new ContextTestModule(), true);
        ConcreteWall child = wall.stack(new ContextChildTestBlock());
        Context context = child.createContext(InstrumentationRegistry.getTargetContext());
        ExampleView exampleView = (ExampleView) LayoutInflater.from(context).inflate(R.layout.example, null);
        assertThat(exampleView.injectedChildString).isSameAs("I'm the child");
        assertThat(exampleView.injectedString).isSameAs("Context test string");
    }

    static final class ContextChildTestBlock implements ConcreteBlock {

        @Override public String name() {
            return getClass().getName();
        }

        @Override public Object daggerModule() {
            return new ContextChildTestModule();
        }
    }

    @Module(addsTo = ContextTestModule.class, injects = ExampleView.class) static final class ContextChildTestModule {

        @Provides @Named("child") String provideChildString() {
            return "I'm the child";
        }
    }
}
