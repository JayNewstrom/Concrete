package com.jaynewstrom.concrete;

import android.annotation.SuppressLint;
import android.content.Context;

import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ConcreteTest {

    @Test public void findWallThrowsWhenContextDoesNotReturnWall() {
        Context context = mock(Context.class);
        try {
            Concrete.findWall(context);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining("Cannot find wall");
        }
    }

    @Test public void findWallReturnsFoundation() {
        Context context = mock(Context.class);
        ConcreteWall<FindWallTestComponent> foundation = pourFoundation(context, DaggerConcreteTest_FindWallTestComponent.create());
        assertThat(Concrete.<ConcreteWall<FindWallTestComponent>>findWall(context)).isSameAs(foundation);
    }

    @Component(modules = FindWallTestModule.class)
    interface FindWallTestComponent {
    }

    @Module
    static final class FindWallTestModule {
    }

    @Test public void injectSetsFieldsInInjectedObject() {
        Context context = mock(Context.class);
        pourFoundation(context, DaggerConcreteTest_InjectTestComponent.create());
        InjectTest injectTest = new InjectTest();
        Concrete.<InjectTestComponent>getComponent(context).inject(injectTest);
        assertThat(injectTest.injectedString).isEqualTo("hello world");
    }

    @Component(modules = InjectTestModule.class)
    interface InjectTestComponent {
        void inject(InjectTest injectTest);
    }

    @Module
    static final class InjectTestModule {
        @Provides @Named("injectedString") String provideString() {
            return "hello world";
        }
    }

    static final class InjectTest {
        @Inject @Named("injectedString") String injectedString;
    }

    @SuppressWarnings("ResourceType") @SuppressLint("WrongConstant")
    private static <C> ConcreteWall<C> pourFoundation(Context contextToMock, C component) {
        ConcreteWall<C> foundation = Concrete.pourFoundation(component);
        when(contextToMock.getSystemService(Concrete.CONCRETE_SERVICE)).thenReturn(foundation);
        return foundation;
    }
}
