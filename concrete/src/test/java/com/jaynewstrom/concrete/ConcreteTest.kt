package com.jaynewstrom.concrete

import android.annotation.SuppressLint
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.inject.Inject
import javax.inject.Named

class ConcreteTest {
    @Test fun findWallThrowsWhenContextDoesNotReturnWall() {
        val context = mock(Context::class.java)
        try {
            Concrete.findWall<ConcreteWall<*>>(context)
            fail()
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining("Cannot find wall")
        }
    }

    @Test fun findWallReturnsFoundation() {
        val context = mock(Context::class.java)
        val foundation = pourFoundation(context, DaggerConcreteTest_FindWallTestComponent.create())
        assertThat(Concrete.findWall<ConcreteWall<FindWallTestComponent>>(context)).isSameAs(foundation)
    }

    @Component
    internal interface FindWallTestComponent

    @Test fun injectSetsFieldsInInjectedObject() {
        val context = mock(Context::class.java)
        pourFoundation(context, DaggerConcreteTest_InjectTestComponent.create())
        val injectTest = InjectTest()
        Concrete.getComponent<InjectTestComponent>(context).inject(injectTest)
        assertThat(injectTest.injectedString).isEqualTo("hello world")
    }

    @Component(modules = [InjectTestModule::class])
    internal interface InjectTestComponent {
        fun inject(injectTest: InjectTest)
    }

    @Module
    internal class InjectTestModule {
        @Provides @Named("injectedString") fun provideString(): String {
            return "hello world"
        }
    }

    internal class InjectTest {
        @Inject @field:Named("injectedString") lateinit var injectedString: String
    }

    @SuppressLint("WrongConstant")
    private fun <C> pourFoundation(contextToMock: Context, component: C): ConcreteWall<C> {
        val foundation = Concrete.pourFoundation(component)
        `when`(contextToMock.getSystemService(Concrete::class.java.name)).thenReturn(foundation)
        return foundation
    }
}
