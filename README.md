Concrete
=========

What problems does Concrete solve?
-------
* Organize dependencies in a way that makes sense in an Android Application
* Manage singletons that can outlive the activity lifecycle

Description of (high level) functionality
-------

Concrete helps you build a wall (two dimensional). The foundational wall is poured in your Android Application subclass.
Each block you stack on the wall adds to the available dependencies the wall provides.
There is only ever one wall, the dependencies you have access to comes from the point in the wall you have access to (using Context#getSystemService).

If you only have access to the foundational wall (Application Context), you only have access to the dependencies provided in the foundational wall.
If you have access to the login activity (Activity Context) then you have access to the dependencies provided in the Login Activity's wall, as well as the foundational wall.

An example of how this would be useful is retrofit services.
Your RestAdapter instance should be cached across all services and your app, which would place it in the foundation.
The specific service interfaces should be cached as singletons, but it doesn't make sense to have your messaging service cached when viewing your photos.

What makes Concrete unique?
-------

This is different from Mortar in that there is no notion of listeners for the activity lifecycle events.

This is similar to u2020's `Injector` but promotes the idea of having an `ObjectGraph` outlive the activity lifecycle.

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

