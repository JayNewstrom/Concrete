package com.jaynewstrom.concrete;

import android.annotation.SuppressLint;
import android.content.Context;

import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

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
        ConcreteWall foundation = pourFoundationWithMockContext(context, new FindWallTestModule());
        assertThat(Concrete.findWall(context)).isSameAs(foundation);
    }

    @Module static final class FindWallTestModule {

    }

    @Test public void injectSetsFieldsInInjectedObject() {
        Context context = mock(Context.class);
        pourFoundationWithMockContext(context, new InjectTestModule());
        InjectTest injectTest = new InjectTest();
        Concrete.inject(context, injectTest);
        assertThat(injectTest.injectedString).isEqualTo("hello world");
    }

    @Module(injects = InjectTest.class) static final class InjectTestModule {

        @Provides @Named("injectedString") String provideString() {
            return "hello world";
        }
    }

    static final class InjectTest {

        @Inject @Named("injectedString") String injectedString;
    }

    @SuppressWarnings("ResourceType") @SuppressLint("WrongConstant")
    static ConcreteWall pourFoundationWithMockContext(Context contextToMock, Object daggerModule) {
        ConcreteWall foundation = Concrete.pourFoundation(daggerModule, true);
        when(contextToMock.getSystemService(Concrete.CONCRETE_SERVICE)).thenReturn(foundation);
        return foundation;
    }
}