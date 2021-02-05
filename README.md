# AmplifyAppAndroid

## versions used in building the App
* Android Studio 4.0.1
* Android Gradle Plugin Version used: 4.0.1
* Gradle Version: 6.1.1
* amplify CLI version used: 4.36.2
* npm version used: 6.14.4
* node version used: v14.3.0

## App also tested on following updated versions:
* Android Studio 4.1.2
* Android Gradle Plugin Version used: 4.1.2
* Gradle Version: 6.5
* amplify CLI version used: 4.41.2
* npm version used: 6.14.4
* node version used: v14.3.0

## Steps to build the App using AppSync client to call the AppSync GQL Schema operations :
1. Create a Project with Empty Activity following https://docs.amplify.aws/start/getting-started/setup/q/integration/android#create-a-new-android-application
2. Change the Project level build.gradle file to as follows:

```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.1"
        classpath 'com.amazonaws:aws-android-sdk-appsync-gradle-plugin:3.1.0'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

3. Change the App level build.gradle file as follows:

```
apply plugin: 'com.android.application'
apply plugin: 'com.amazonaws.appsync' // REQUIRED

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.3"

    defaultConfig {
        applicationId "com.example.myamplifyappandroid"
        minSdkVersion 21
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        // Enable multidex support (if supporting min SDK < 21)
        multiDexEnabled true
    }

    compileOptions {
        // Support for Java 8 features
        coreLibraryDesugaringEnabled true
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.1'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    // Amplify core dependency
    implementation 'com.amplifyframework:core:1.3.1'

    // Multidex dependency (if supporting min SDK < 21)
    implementation 'androidx.multidex:multidex:2.0.1'

    // Support for Java 8 features
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.0.10'
    //for appsync
    implementation 'com.amazonaws:aws-android-sdk-appsync:3.1.0'
    implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0'
    implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
    //for pinpoint
    implementation 'com.amplifyframework:aws-analytics-pinpoint:1.3.1'
    implementation 'com.amplifyframework:aws-auth-cognito:1.3.1'
}
```

4. sync now to see "CONFIGURE SUCCESSFULL" message in the Build logs which indicates gradle is successfully synced into the APP.

5. execute below command and follow the steps in main directory of the Android Application to create the Amplify project in the AWS Cloud and connect to it.
>>> amplify init

6. Modify the "MainActivity.java" file as follows to initialize amplify successfully from the code.

```
package com.example.amplifyappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing amplify first
        try {

            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }
}
```

7. Build and Run the project to see the Build Successfull and "Initialized Amplify" printing on the "Logcat".

8. execute "amplify add codegen --apiId xxx" to add the existing AppSync API into the project, or execute "amplify add api" in the Android studio project main folder and follow the steps in https://docs.amplify.aws/lib/graphqlapi/getting-started/q/platform/android#configure-api to create a new AppSync APP in the project.

9. The above command will create the "app/src/main/graphql/" folder which contains the GraphQL schema and also contains queries, mutations, subscriptions as "app/src/main/graphql/com/amazonaws/amplify/generated/graphql/*.graphql" files with there generated class files as "build/generated/source/appsync/com/amazonaws/amplify/generated/graphql/*.java" which will be used to make calls to the API from the android project.

10. The files "app/src/main/res/raw/amplifyconfiguration.json" and "app/src/main/res/raw/awsconfiguration.json" contains the AWS resources such as AppSync API, Cognito Auth, Pinpoint application endpoints which Amplify uses to connect in AWS account.

11. From the AppSync Console using the Wizard I have created the "Event App" API which is the sample complete API which has resolvers attached to two dynamoDb Tables which were created in the process. This API ID was used in the Android project by executing the command in step-9.

12. Modified the MainActivity.java to create the AppSync Client object with:

```
        //creating AppSync client to connect and execute queries on AppSync API
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                // If you are using complex objects (S3) then uncomment
                //.s3ObjectManager(new S3ObjectManagerImplementation(new AmazonS3Client(AWSMobileClient.getInstance())))
                .build();as follows with the change in Package as required which consists of appsync queires, mutations, subscription functions which can be invoked using "AWSAppSyncClient":
```

13. To execute the GraphQPL operations of "Event APP" AppSync API, create "executeAppSyncAPISchemaOperations();" method in the "MainActivity.java" file.

14. "AppSyncClientGQlSchemaOperationCalls.java" class file has methods which can be modified depending on the schema of the backend API on AWS AppSync.

15. "amplifyconfiguration.json" and "awsconfiguration.json" contains the AppSync API endpoint URL and API_key that Android App uses to communicate.

16. Modify AndroidManifest.xml file as follows to allow user to provide required permissions for the functioning of app:
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.amplifyappandroid">
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <service android:name="org.eclipse.paho.android.service.MqttService" />
    </application>

</manifest>
```

17. Sucessfully build and run the android app to see the output of the queries in the "Logcat".

## Steps Using Amplify client to call the AppSync GQL Schema operations
The minimum required amplify cli version is >=4.37.0 updating the Gradle files with Amplify framework tools and syncing automatically updates the amplify version if its <4.37.0

1. Modify the Project Gradle file as follows with "com.amplifyframework:amplify-tools" which will be useful to execute Amplify Client call "Amplify.API.mutate()" and import libraries such as " ModelMutation, GsonVariablesSerializer,..etc" https://docs.amplify.aws/lib/graphqlapi/getting-started/q/platform/android:

```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.1"
        classpath 'com.amazonaws:aws-android-sdk-appsync-gradle-plugin:3.1.0'
        //for amplify client to query AppSync API
        classpath 'com.amplifyframework:amplify-tools-gradle-plugin:1.0.2'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

//for amplify client to query AppSync API
apply plugin: 'com.amplifyframework.amplifytools'
```
2. Modify the app gradle file to add amplify-tools dependency as follows:
```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.0.1"
        classpath 'com.amazonaws:aws-android-sdk-appsync-gradle-plugin:3.1.0'
        //for amplify client to query AppSync API
        classpath 'com.amplifyframework:amplify-tools-gradle-plugin:1.0.2'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

//for amplify client to query AppSync API
apply plugin: 'com.amplifyframework.amplifytools'
```
3. Since i have added the API externally using ```Amplify add codegen --apiId``` as of now codegen doesn't recognize the model directly as per https://github.com/aws-amplify/amplify-codegen/issues/20 and https://github.com/kcwinner/cdk-appsync-transformer-demo to make use of Amplify Client for calling AppSync API such as "Amplify.API.mutate(), Amplify.API.query()". If the schema was added with @models as per https://docs.amplify.aws/lib/graphqlapi/getting-started/q/platform/android#configure-api the Amplify.API.mutate() can be used directly to recognize the generated classes. Skip from 4- if you haven't used ```Amplify add codegen --apiId``` 


## References:
1. https://docs.amplify.aws/lib/project-setup/create-application/q/platform/android#n1-create-a-new-project
2. https://github.com/awslabs/aws-mobile-appsync-sdk-android
3. https://docs.amplify.aws/sdk/api/graphql/q/platform/android#run-a-mutation
4. https://docs.amplify.aws/lib/project-setup/use-existing-resources/q/platform/android
5. https://www.geeksforgeeks.org/how-to-build-a-todo-android-application-with-aws-dynamodb-as-database/
6. https://www.npmjs.com/package/@aws-amplify/cli
7. https://docs.amplify.aws/lib/graphqlapi/concepts/q/platform/android#using-aws-appsync
