package com.jaynewstrom.concretetesting;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public final class ConcreteWallTest {
    @Test public void ensureContextCreatedFromWallCanBeUsedToInject() {
        ConcreteWall<ContextTestComponent> wall = Concrete.pourFoundation(DaggerContextTestComponent.create());
        Context context = wall.createContext(InstrumentationRegistry.getTargetContext());
        ContextTestTarget target = new ContextTestTarget();
        Concrete.<ContextTestComponent>getComponent(context).inject(target);
        assertThat(target.injectedString).isSameAs("Context test string");
    }

    @Test public void whenViewIsCreatedWithLayoutInflaterFromChildContextEnsureItCanBeInjectedWithContext() {
        ConcreteWall<ContextTestComponent> wall = Concrete.pourFoundation(DaggerContextTestComponent.create());
        ConcreteWall<ContextChildTestComponent> child = wall.stack(new ContextChildTestBlock(wall.getComponent()));
        Context context = child.createContext(InstrumentationRegistry.getTargetContext());
        ExampleView exampleView = (ExampleView) LayoutInflater.from(context).inflate(R.layout.example, null);
        assertThat(exampleView.injectedChildString).isSameAs("I'm the child");
        assertThat(exampleView.injectedString).isSameAs("Context test string");
    }
}
