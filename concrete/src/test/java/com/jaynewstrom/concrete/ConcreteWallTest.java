package com.jaynewstrom.concrete;

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ConcreteWallTest {

    @Test public void ensureInvalidGraphFailsValidationWhenCreatingWall() {
        try {
            Concrete.pourFoundation(new InvalidFoundationModule(), true);
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception).hasMessageStartingWith("Errors creating object graph");
        }
    }

    @Module(complete = false, injects = InvalidModuleTarget.class) static final class InvalidFoundationModule {

        @Provides String provideUnusedString(Date thisDateIsNotProvidedAnywhere) {
            return "This will never run" + thisDateIsNotProvidedAnywhere;
        }
    }

    @Test public void ensureInvalidGraphDoesNotFailUntilUsedToInject() {
        ConcreteWall concreteWall = Concrete.pourFoundation(new InvalidFoundationModule(), false);
        try {
            InvalidModuleTarget target = new InvalidModuleTarget();
            concreteWall.inject(target);
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception).hasMessageStartingWith("Errors creating object graph");
        }
    }

    static final class InvalidModuleTarget {

        @Inject String string;
    }

    @Test public void ensureDestroyedWallThrowsWhenUsedToInject() {
        ConcreteWall concreteWall = Concrete.pourFoundation(new ValidTestModule(), true);
        concreteWall.destroy();
        ValidTestTarget target = new ValidTestTarget();
        try {
            concreteWall.inject(target);
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.");
        }
    }

    @Module(injects = ValidTestTarget.class)
    static final class ValidTestModule {

        @Provides String provideString() {
            return "Concrete";
        }
    }

    static final class ValidTestTarget {

        @Inject String string;
    }

    @Test public void ensureWallInjectSetsVariable() {
        ConcreteWall concreteWall = Concrete.pourFoundation(new ValidTestModule(), true);
        ValidTestTarget target = new ValidTestTarget();
        concreteWall.inject(target);
        assertThat(target.string).isSameAs("Concrete");
    }

    @Test public void stackedWallInjectSetsVariable() {
        ConcreteWall foundation = Concrete.pourFoundation(new ValidTestModule(), true);
        ConcreteBlock block = mock(ConcreteBlock.class);
        when(block.name()).thenReturn("Stacked");
        when(block.daggerModule()).thenReturn(new ValidTestChildModule());
        ConcreteWall childWall = foundation.stack(block);
        ValidTestChildTarget childTarget = new ValidTestChildTarget();
        childWall.inject(childTarget);
        assertThat(childTarget.childString).matches("Concrete-Child");
    }

    @Module(injects = ValidTestChildTarget.class, addsTo = ValidTestModule.class) static final class ValidTestChildModule {

        @Provides @Named("child") String provideChildString(String parent) {
            return parent + "-Child";
        }
    }

    static final class ValidTestChildTarget {

        @Inject @Named("child") String childString;
    }

    @Test public void ensureStackCalledWithTheSameBlockNameReturnsSameWall() {
        ConcreteWall foundation = Concrete.pourFoundation(new ValidTestModule(), true);
        ConcreteBlock block = mock(ConcreteBlock.class);
        when(block.name()).thenReturn("Stacked");
        when(block.daggerModule()).thenReturn(new ValidTestChildModule());
        ConcreteWall childWall = foundation.stack(block);
        assertThat(foundation.stack(block)).isSameAs(childWall);
        verify(block, times(1)).daggerModule();
    }

    @Test public void whenStackedThenDestroyedThenStackedAgainEnsureWallIsRecreated() {
        ConcreteWall foundation = Concrete.pourFoundation(new ValidTestModule(), true);
        ConcreteBlock block = mock(ConcreteBlock.class);
        when(block.name()).thenReturn("Stacked");
        when(block.daggerModule()).thenReturn(new ValidTestChildModule());
        ConcreteWall childWall = foundation.stack(block);
        assertThat(foundation.stack(block)).isSameAs(childWall);
        verify(block, times(1)).daggerModule();
        childWall.destroy();
        assertThat(foundation.stack(block)).isNotSameAs(childWall);
        verify(block, times(2)).daggerModule();
    }

    @Test public void whenWallIsDestroyedEnsureItCanBeGarbageCollected() {
        ConcreteWall foundation = Concrete.pourFoundation(new ValidTestModule(), true);
        ConcreteBlock block = mock(ConcreteBlock.class);
        when(block.name()).thenReturn("Stacked");
        when(block.daggerModule()).thenReturn(new ValidTestChildModule());
        WeakReference<ConcreteWall> wallWeakReference = new WeakReference<>(foundation.stack(block));
        wallWeakReference.get().destroy();
        System.gc();
        assertThat(wallWeakReference.get()).isNull();
    }

    @Test public void whenChildIsDestroyedEnsureTheParentIsStillUsable() {
        ConcreteWall foundation = Concrete.pourFoundation(new ValidTestModule(), true);
        ConcreteBlock block = mock(ConcreteBlock.class);
        when(block.name()).thenReturn("Stacked");
        when(block.daggerModule()).thenReturn(new ValidTestChildModule());
        ConcreteWall childWall = foundation.stack(block);
        childWall.destroy();
        ValidTestTarget target = new ValidTestTarget();
        foundation.inject(target);
        assertThat(target.string).isSameAs("Concrete");
    }

    @Test public void whenParentIsDestroyedEnsureChildrenAreAlsoDestroyed() {
        ConcreteWall foundation = Concrete.pourFoundation(new ValidTestModule(), true);
        ConcreteBlock block = mock(ConcreteBlock.class);
        when(block.name()).thenReturn("Stacked");
        when(block.daggerModule()).thenReturn(new ValidTestChildModule());
        ConcreteBlock blockAgain = mock(ConcreteBlock.class);
        ConcreteWall childWall = foundation.stack(block);
        when(blockAgain.name()).thenReturn("StackedAgain");
        when(blockAgain.daggerModule()).thenReturn(new ValidTestChildModule());
        foundation.stack(blockAgain);
        foundation.destroy();
        try {
            ValidTestChildTarget childTarget = new ValidTestChildTarget();
            childWall.inject(childTarget);
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.");
        }
    }
}
