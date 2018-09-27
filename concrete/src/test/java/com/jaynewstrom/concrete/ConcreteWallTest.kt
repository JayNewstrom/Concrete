package com.jaynewstrom.concrete

import dagger.Component
import dagger.Module
import dagger.Provides
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicInteger
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
            concreteWall.component
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
        concreteWall.component.inject(target)
        assertThat(target.string).isEqualTo("Concrete")
    }

    @Test fun stackedWallInjectSetsVariable() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val childWall = foundation.stack(block)
        val childTarget = TestChildTarget()
        childWall.component.inject(childTarget)
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
        val block = spy(TestChildBlock(foundation.component))
        val childWall = foundation.stack(block)
        assertThat(foundation.stack(block)).isSameAs(childWall)
        verify<ConcreteBlock<TestChildComponent>>(block, times(1)).createComponent()
    }

    @Test fun ensureStackCalledWithTheSameBlockNameCallsInitializationAction() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val initializationActionCalledCount = AtomicInteger(0)
        foundation.stack(block) {
            initializationActionCalledCount.incrementAndGet()
        }
        assertThat(initializationActionCalledCount.get()).isEqualTo(1)
        foundation.stack(block) {
            initializationActionCalledCount.incrementAndGet()
        }
        assertThat(initializationActionCalledCount.get()).isEqualTo(1)
    }

    @Test fun whenStackedThenDestroyedThenStackedAgainEnsureWallIsRecreated() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.component))
        val childWall = foundation.stack(block)
        assertThat(foundation.stack(block)).isSameAs(childWall)
        verify<ConcreteBlock<TestChildComponent>>(block, times(1)).createComponent()
        childWall.destroy()
        assertThat(foundation.stack(block)).isNotSameAs(childWall)
        verify<ConcreteBlock<TestChildComponent>>(block, times(2)).createComponent()
    }

    @Test fun whenWallIsDestroyedEnsureItCanBeGarbageCollected() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.component))
        val wallWeakReference = WeakReference(foundation.stack(block))
        wallWeakReference.get()!!.destroy()
        System.gc()
        assertThat<ConcreteWall<TestChildComponent>>(wallWeakReference.get()).isNull()
    }

    @Test fun whenChildIsDestroyedEnsureTheParentIsStillUsable() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.component))
        val childWall = foundation.stack(block)
        childWall.destroy()
        val target = TestTarget()
        foundation.component.inject(target)
        assertThat(target.string).isEqualTo("Concrete")
    }

    @Test fun whenParentIsDestroyedEnsureChildrenAreAlsoDestroyed() {
        val foundation = testComponentWall()
        val block = spy(TestChildBlock(foundation.component))
        val childWall = foundation.stack(block)
        foundation.destroy()
        try {
            childWall.component
            fail()
        } catch (exception: IllegalStateException) {
            assertThat(exception).hasMessage("Concrete wall has been destroyed.")
        }
    }

    @Test fun whenWallIsDestroyedEnsureDestructionActionsAreCalled() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val wall = foundation.stack(block)
        val destructionActionCalledCount = AtomicInteger(0)
        wall.addDestructionAction {
            destructionActionCalledCount.incrementAndGet()
        }
        wall.destroy()
        assertThat(destructionActionCalledCount.get()).isEqualTo(1)
    }

    @Test fun whenDestructionActionIsRemovedEnsureDestructionActionsAreNotCalled() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val wall = foundation.stack(block)
        val destructionActionCalledCount = AtomicInteger(0)
        val destructionAction: (TestChildComponent) -> Unit = { destructionActionCalledCount.incrementAndGet() }
        wall.addDestructionAction(destructionAction)
        wall.removeDestructionAction(destructionAction)
        wall.destroy()
        assertThat(destructionActionCalledCount.get()).isEqualTo(0)
    }

    @Test fun whenWallIsDestroyedEnsureDestructionActionsAreCalledExactlyOnce() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val wall = foundation.stack(block)
        val destructionActionCalledCount = AtomicInteger(0)
        val destructionAction: (TestChildComponent) -> Unit = { destructionActionCalledCount.incrementAndGet() }
        wall.addDestructionAction(destructionAction)
        wall.destroy()
        wall.destroy() // No op, the wall is already destroyed.
        assertThat(destructionActionCalledCount.get()).isEqualTo(1)
    }

    @Test fun componentOfTypeIncludingParentsReturnsCurrentComponent() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val wall = foundation.stack(block)
        val component = wall.componentOfTypeIncludingParents(TestChildComponent::class.java)
        assertThat(component).isNotNull
        assertThat(component).isInstanceOf(TestChildComponent::class.java)
        assertThat(component).isEqualTo(wall.component)
    }

    @Test fun componentOfTypeIncludingParentsReturnsParentComponent() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val wall = foundation.stack(block)
        val component = wall.componentOfTypeIncludingParents(TestComponent::class.java)
        assertThat(component).isNotNull
        assertThat(component).isInstanceOf(TestComponent::class.java)
        assertThat(component).isNotInstanceOf(TestChildComponent::class.java)
    }

    @Test fun componentOfTypeIncludingParentsThrowsOnUnknownComponent() {
        val foundation = testComponentWall()
        val block = TestChildBlock(foundation.component)
        val wall = foundation.stack(block)
        try {
            wall.componentOfTypeIncludingParents(ConcreteWallTest::class.java)
            fail()
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining("context is not associated with a wall having a component implementing")
        }
    }
}
