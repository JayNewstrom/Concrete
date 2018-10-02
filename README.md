Concrete
========

Concrete creates and caches Dagger 2 components in Android applications. 
Concrete manages scoped components that outlive the activity lifecycle.
It can be useful to have the same component after a configuration change (rotation).

Description of (high level) functionality
-----------------------------------------

Concrete helps you build a wall (two dimensional). The foundational wall is poured in your Android Application subclass.
A `ConcreteBlock` is a lightweight configuration object that is stacked onto a wall. 
When stacking a block, the wall you are stacking onto uses the blocks `name` to see if a child wall already exists and returns it, or creates a new wall with the blocks `createComponent` method when the wall doesn't already exist. 

Concrete uses `Context#getSystemService` to give you access to a component anytime you have access to context. 
If you only have access to the foundational wall (Application Context), you only have access to the dependencies provided in the foundational wall.
If you have access to the login activity (Activity Context) then you have access to the dependencies provided in the Login Activity's wall, (which generally has access to the foundational walls dependencies, depending on your component definition).

An example of how this would be useful is retrofit services.
Your Retrofit instance should be cached across all services and your app, which would place it in the foundation.
The specific service interfaces should be cached as scoped singletons, but it doesn't make sense to have your messaging service cached when viewing your photos.

Wall Example
------------

    | ------------ Screen 1 Wall ------------ | ------------ Screen 2 Wall ------------ |
    | ---------------------------------- Activity Wall ---------------------------------- |
    | --------------------------------------- Application Wall --------------------------------------- |
    
Lifecycle Examples 
------------------
F - Framework event
C - Concrete call
M - More information

* F - Application Starts
* C - Foundation poured (application wall) using `Concrete.pourFoundation(...);`
    * F - Activity Starts
    * M - application wall found using `Concrete.findWall(getApplicationContext());`
    * C - `ConcreteBlock` stacked onto application wall (foundation)
        * M - new wall is created using the blocks `createComponent` method
* F - Configuration change (rotate)
    * F - Activity Starts
    * C - `ConcreteBlock` stacked onto application wall (foundation)
        * M - cached wall is returned using the blocks `name` method 


Setup
-----

```groovy
dependencies {
    compile 'com.jaynewstrom:concrete:0.13.2'
}
```

License
-------

    Copyright 2015 Jay Newstrom

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

