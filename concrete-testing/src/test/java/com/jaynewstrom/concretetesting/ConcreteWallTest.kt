package com.jaynewstrom.concretetesting

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.jaynewstrom.concrete.Concrete
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConcreteWallTest {
    @Test fun ensureContextCreatedFromWallCanBeUsedToInject() {
        val wall = Concrete.pourFoundation(DaggerContextTestComponent.create())
        val context = wall.createContext(ApplicationProvider.getApplicationContext())
        val target = ContextTestTarget()
        Concrete.getComponent<ContextTestComponent>(context).inject(target)
        assertThat(target.injectedString).isSameAs("Context test string")
    }

    @Test fun whenViewIsCreatedWithLayoutInflaterFromChildContextEnsureItCanBeInjectedWithContext() {
        val wall = Concrete.pourFoundation(DaggerContextTestComponent.create())
        val child = wall.stack(ContextChildTestBlock(wall.component))
        val context = child.createContext(ApplicationProvider.getApplicationContext())
        val exampleView = LayoutInflater.from(context).inflate(R.layout.example, null) as ExampleView
        assertThat(exampleView.injectedChildString).isSameAs("I'm the child")
        assertThat(exampleView.injectedString).isSameAs("Context test string")
    }
}
