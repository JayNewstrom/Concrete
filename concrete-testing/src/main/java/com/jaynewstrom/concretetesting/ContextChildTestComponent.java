package com.jaynewstrom.concretetesting;

import dagger.Component;

@ForChild
@Component(dependencies = ContextTestComponent.class, modules = ContextChildTestModule.class)
interface ContextChildTestComponent {
    void inject(ExampleView exampleView);
}
