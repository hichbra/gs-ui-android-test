# GraphStream -- Android Viewer Test

This repository contains some usable examples of [gs-ui-android](https://github.com/graphstream/gs-ui-android) viewer.

## Install UI

`gs-ui-android` is a plugin to the `gs-core` main project.

The release comes with a pre-packaged aar file named `gs-ui-android.aar` that contains the GraphStream viewer classes. It depends on the root project `gs-core`.

 For using Graphstream, your project <b>must</b> run with java 8

 Add the .aar in your project following these instructions : https://developer.android.com/studio/projects/android-library.html#AddDependency

 You can download GraphStream on the github releases pages:

- [gs-core](https://github.com/graphstream/gs-core/releases)
- [gs-ui-android](https://github.com/graphstream/gs-ui-android/releases)


Gradle users, you may include `gs-core` and `gs-ui-android` as a dependency to your project using <https://jitpack.io>.
Simply add the `jitpack` repository to the `pom.xml`:

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

then, add the `gs-core` and `gs-ui-android` to your dependencies:

```xml
dependencies {
    api 'com.github.graphstream:gs-ui-android:2.0-alpha'
    api 'com.github.graphstream:gs-core:2.0-alpha'
}
```

You can use any version of `gs-core` and `gs-ui-android` you need, provided they are the same. Simply specify the desired version in the `<version>` tag. The version can be a git tag name (e.g. `2.0-alpha`), a commit number, or a branch name followed by `-SNAPSHOT` (e.g. `dev-SNAPSHOT`). More details on the [possible versions on jitpack](https://jitpack.io/#graphstream/gs-core).

## Configure UI

For the convenience of the users, a default Android Fragment (`org.graphstream.ui.android_viewer.util.DefaultFragment`) is provided. The Fragment can be used like so:

```java
public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
    if (savedInstanceState == null) {
        FragmentManager fm = getFragmentManager();

        // find fragment or create him
        fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");
        if (null == fragment) {
            fragment = new DefaultFragment();
            fragment.init(graph, autoLayout);
        }

        // Add the fragment in the layout and commit
        FragmentTransaction ft = fm.beginTransaction() ;
        ft.add(CONTENT_VIEW_ID, fragment).commit();
    }
}
```
