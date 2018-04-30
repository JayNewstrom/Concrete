package com.jaynewstrom.concrete

import dagger.Component
import dagger.Module
import dagger.Provides
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Named

class ConcreteWallTest {
    private fun testComponentWall(): ConcreteWall<TestComponent> {
        return Concrete.pourFoundation(DaggerConcreteWallTest_TestComponent.create())
    }

    @Test fun ensureDestroyedWallThrowsWhenCallingComponent() {
        val concreteWall = testComponentWall()
        concreteWall.destroy()
        try {
            concreteWall.getComponent()
            fail()
        } catch (exception: IllegalStateException) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.")
        }
    }

    @Component(modules = [TestModule::class])
    internal interface TestComponent {
        val string: String
        fun inject(target: TestTarget)
    }

    @Module
    internal class TestModule {
        @Provides fun provideString(): String = "Concrete"
    }

    internal class TestTarget {
        @Inject lateinit var string: String
    }

    @Test fun ensureWallInjectSetsVariable() {
        val concreteWall = testComponentWall()
        val target = TestTarget()
        concreteWall.getComponent().inject(target)
        assertThat(target.string).isEqualTo("Concrete")
    }

    @Test fun stackedWallInjectSetsVariable() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.getComponent())
        val childWall = foundation.stack(block)
        val childTarget = TestChildTarget()
        childWall.getComponent().inject(childTarget)
        assertThat(childTarget.childString).isEqualTo("Concrete-Child")
    }

    internal open class TestChildBlock(private val testComponent: TestComponent) : ConcreteBlock<TestChildComponent> {
        override fun name(): String = "Stacked"

        override fun createComponent(): TestChildComponent {
            return DaggerConcreteWallTest_TestChildComponent.builder().testComponent(testComponent).build()
        }
    }

    @Component(dependencies = [TestComponent::class], modules = [TestChildModule::class])
    internal interface TestChildComponent {
        fun inject(target: TestChildTarget)
    }

    @Module
    internal class TestChildModule {
        @Provides @Named("child") fun provideChildString(parent: String): String = "$parent-Child"
    }

    internal class TestChildTarget {
        @Inject @field:Named("child") lateinit var childString: String
    }

    @Test fun ensureStackCalledWithTheSameBlockNameReturnsSameWall() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.getComponent()))
        val childWall = foundation.stack(block)
        assertThat(foundation.stack(block)).isSameAs(childWall)
        verify<ConcreteBlock<TestChildComponent>>(block, times(1)).createComponent()
    }

    @Test fun ensureStackCalledWithTheSameBlockNameCallsInitializationAction() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.getComponent())
        val initializationAction = kotlinMock<ConcreteWallInitializationAction<TestChildComponent>>()
        foundation.stack(block, initializationAction)
        verify<ConcreteWallInitializationAction<TestChildComponent>>(initializationAction)
            .onWallInitialized(kotlinAny())
        foundation.stack(block, initializationAction)
        verify<ConcreteWallInitializationAction<TestChildComponent>>(initializationAction)
            .onWallInitialized(kotlinAny())
    }

    @Test fun whenStackedThenDestroyedThenStackedAgainEnsureWallIsRecreated() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.getComponent()))
        val childWall = foundation.stack(block)
        assertThat(foundation.stack(block)).isSameAs(childWall)
        verify<ConcreteBlock<TestChildComponent>>(block, times(1)).createComponent()
        childWall.destroy()
        assertThat(foundation.stack(block)).isNotSameAs(childWall)
        verify<ConcreteBlock<TestChildComponent>>(block, times(2)).createComponent()
    }

    @Test fun whenWallIsDestroyedEnsureItCanBeGarbageCollected() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.getComponent()))
        val wallWeakReference = WeakReference(foundation.stack(block))
        wallWeakReference.get()!!.destroy()
        System.gc()
        assertThat<ConcreteWall<TestChildComponent>>(wallWeakReference.get()).isNull()
    }

    @Test fun whenChildIsDestroyedEnsureTheParentIsStillUsable() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.getComponent()))
        val childWall = foundation.stack(block)
        childWall.destroy()
        val target = TestTarget()
        foundation.getComponent().inject(target)
        assertThat(target.string).isEqualTo("Concrete")
    }

    @Test fun whenParentIsDestroyedEnsureChildrenAreAlsoDestroyed() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.getComponent()))
        val childWall = foundation.stack(block)
        foundation.destroy()
        try {
            childWall.getComponent()
            fail()
        } catch (exception: IllegalStateException) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.")
        }
    }

    @Test fun whenWallIsDestroyedEnsureDestructionActionsAreCalled() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.getComponent())
        val wall = foundation.stack(block)
        val destructionAction = kotlinMock<ConcreteWallDestructionAction<ConcreteWallTest.TestChildComponent>>()
        wall.addDestructionAction(destructionAction)
        wall.destroy()
        verify<ConcreteWallDestructionAction<TestChildComponent>>(destructionAction).onWallDestroyed(kotlinAny())
    }

    @Test fun whenDestructionActionIsRemovedEnsureDestructionActionsAreNotCalled() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.getComponent())
        val wall = foundation.stack(block)
        val destructionAction = kotlinMock<ConcreteWallDestructionAction<ConcreteWallTest.TestChildComponent>>()
        wall.addDestructionAction(destructionAction)
        wall.removeDestructionAction(destructionAction)
        wall.destroy()
        verify<ConcreteWallDestructionAction<TestChildComponent>>(destructionAction, never())
            .onWallDestroyed(kotlinAny())
    }

    @Test fun whenWallIsDestroyedEnsureDestructionActionsAreCalledExactlyOnce() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.getComponent())
        val wall = foundation.stack(block)
        val destructionAction = kotlinMock<ConcreteWallDestructionAction<ConcreteWallTest.TestChildComponent>>()
        wall.addDestructionAction(destructionAction)
        wall.destroy()
        wall.destroy() // No op, the wall is already destroyed.
        verify<ConcreteWallDestructionAction<TestChildComponent>>(destructionAction).onWallDestroyed(kotlinAny())
    }
}
