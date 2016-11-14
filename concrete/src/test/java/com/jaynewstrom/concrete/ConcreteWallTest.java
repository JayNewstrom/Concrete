package com.jaynewstrom.concrete;

import org.junit.Test;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class ConcreteWallTest {

    private static ConcreteWall<TestComponent> testComponentWall() {
        return Concrete.pourFoundation(DaggerConcreteWallTest_TestComponent.create());
    }

    @Test public void ensureDestroyedWallThrowsWhenCallingGetComponent() {
        ConcreteWall<TestComponent> concreteWall = testComponentWall();
        concreteWall.destroy();
        try {
            concreteWall.getComponent();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.");
        }
    }

    @Component(modules = TestModule.class)
    interface TestComponent {
        void inject(TestTarget target);

        String getString();
    }

    @Module
    static final class TestModule {

        @Provides String provideString() {
            return "Concrete";
        }
    }

    static final class TestTarget {

        @Inject String string;
    }

    @Test public void ensureWallInjectSetsVariable() {
        ConcreteWall<TestComponent> concreteWall = testComponentWall();
        TestTarget target = new TestTarget();
        concreteWall.getComponent().inject(target);
        assertThat(target.string).isEqualTo("Concrete");
    }

    @Test public void stackedWallInjectSetsVariable() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = new TestChildBlock(foundation.getComponent());
        ConcreteWall<TestChildComponent> childWall = foundation.stack(block);
        TestChildTarget childTarget = new TestChildTarget();
        childWall.getComponent().inject(childTarget);
        assertThat(childTarget.childString).isEqualTo("Concrete-Child");
    }

    static class TestChildBlock implements ConcreteBlock<TestChildComponent> {

        private final TestComponent testComponent;

        TestChildBlock(TestComponent testComponent) {
            this.testComponent = testComponent;
        }

        @Override public String name() {
            return "Stacked";
        }

        @Override public TestChildComponent createComponent() {
            return DaggerConcreteWallTest_TestChildComponent.builder().testComponent(testComponent).build();
        }
    }

    @Component(dependencies = TestComponent.class, modules = TestChildModule.class)
    interface TestChildComponent {
        void inject(TestChildTarget target);
    }

    @Module
    static final class TestChildModule {

        @Provides @Named("child") String provideChildString(String parent) {
            return parent + "-Child";
        }
    }

    static final class TestChildTarget {

        @Inject @Named("child") String childString;
    }

    @Test public void ensureStackCalledWithTheSameBlockNameReturnsSameWall() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = spy(new TestChildBlock(foundation.getComponent()));
        ConcreteWall<TestChildComponent> childWall = foundation.stack(block);
        assertThat(foundation.stack(block)).isSameAs(childWall);
        verify(block, times(1)).createComponent();
    }

    @Test public void whenStackedThenDestroyedThenStackedAgainEnsureWallIsRecreated() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = spy(new TestChildBlock(foundation.getComponent()));
        ConcreteWall<TestChildComponent> childWall = foundation.stack(block);
        assertThat(foundation.stack(block)).isSameAs(childWall);
        verify(block, times(1)).createComponent();
        childWall.destroy();
        assertThat(foundation.stack(block)).isNotSameAs(childWall);
        verify(block, times(2)).createComponent();
    }

    @Test public void whenWallIsDestroyedEnsureItCanBeGarbageCollected() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = spy(new TestChildBlock(foundation.getComponent()));
        WeakReference<ConcreteWall<TestChildComponent>> wallWeakReference = new WeakReference<>(foundation.stack(block));
        wallWeakReference.get().destroy();
        System.gc();
        assertThat(wallWeakReference.get()).isNull();
    }

    @Test public void whenChildIsDestroyedEnsureTheParentIsStillUsable() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = spy(new TestChildBlock(foundation.getComponent()));
        ConcreteWall<TestChildComponent> childWall = foundation.stack(block);
        childWall.destroy();
        TestTarget target = new TestTarget();
        foundation.getComponent().inject(target);
        assertThat(target.string).isEqualTo("Concrete");
    }

    @Test public void whenParentIsDestroyedEnsureChildrenAreAlsoDestroyed() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = spy(new TestChildBlock(foundation.getComponent()));
        ConcreteWall<TestChildComponent> childWall = foundation.stack(block);
        foundation.destroy();
        try {
            childWall.getComponent();
            fail();
        } catch (IllegalStateException exception) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.");
        }
    }

    @Test public void throwsWhenComponentIsNullWhenPouringFoundation() {
        try {
            Concrete.pourFoundation(null);
            fail();
        } catch (NullPointerException exception) {
            assertThat(exception).hasMessage("component == null");
        }
    }

    @Test public void throwsWhenBlockIsNullWhenStacking() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        try {
            foundation.stack(null);
            fail();
        } catch (NullPointerException exception) {
            assertThat(exception).hasMessage(null);
        }
    }

    static class InvalidTestChildBlock extends TestChildBlock {
        private final boolean returnNullForName;
        private final boolean returnNullForComponent;

        InvalidTestChildBlock(TestComponent testComponent, boolean returnNullForName, boolean returnNullForComponent) {
            super(testComponent);
            this.returnNullForName = returnNullForName;
            this.returnNullForComponent = returnNullForComponent;
        }

        @Override public String name() {
            if (returnNullForName) {
                return null;
            }
            return super.name();
        }

        @Override public TestChildComponent createComponent() {
            if (returnNullForComponent) {
                return null;
            }
            return super.createComponent();
        }
    }

    @Test public void throwsWhenBlockNameIsNullWhenStacking() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = new InvalidTestChildBlock(foundation.getComponent(), true, false);
        try {
            foundation.stack(block);
            fail();
        } catch (NullPointerException exception) {
            assertThat(exception).hasMessage("block.name() == null");
        }
    }

    @Test public void throwsWhenBlockComponentIsNullWhenStacking() {
        ConcreteWall<TestComponent> foundation = testComponentWall();
        ConcreteBlock<TestChildComponent> block = new InvalidTestChildBlock(foundation.getComponent(), false, true);
        try {
            foundation.stack(block);
            fail();
        } catch (NullPointerException exception) {
            assertThat(exception).hasMessage("block.createComponent() == null");
        }
    }
}
