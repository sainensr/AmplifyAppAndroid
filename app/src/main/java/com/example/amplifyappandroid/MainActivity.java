package com.example.amplifyappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {

    private AWSAppSyncClient mAWSAppSyncClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing amplify
        try {
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        //creating AppSync client to connect and execute queries on AppSync API
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                // If you are using complex objects (S3) then uncomment
                //.s3ObjectManager(new S3ObjectManagerImplementation(new AmazonS3Client(AWSMobileClient.getInstance())))
                .build();

        executeAppSyncAPISchemaOperations();

    }

    //Executes all operations defined in the AppSync Event APP API
    public void executeAppSyncAPISchemaOperations(){
        AppSyncClientGQlSchemaOperationCalls mAppSyncClientGQlSchemaOperationCalls = new AppSyncClientGQlSchemaOperationCalls(mAWSAppSyncClient);
        mAppSyncClientGQlSchemaOperationCalls.callListEventsQuery();
        //mAppSyncClientGQlSchemaOperationCalls.callGetEventQuery("3ac79d17-b08e-49e3-8690-22cb9b5a41fd");
        //mAppSyncClientGQlSchemaOperationCalls.callCreateEventMutation();
        //mAppSyncClientGQlSchemaOperationCalls.callDeleteEventMutation("3ac79d17-b08e-49e3-8690-22cb9b5a41fd");
        //mAppSyncClientGQlSchemaOperationCalls.callSubscribeToEventCommentsSubscription("a127441e-d36d-49bc-928f-b836437b0450");

    }

}