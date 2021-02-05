package com.example.amplifyappandroid;

import android.annotation.SuppressLint;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateEventMutation;
import com.amazonaws.amplify.generated.graphql.DeleteEventMutation;
import com.amazonaws.amplify.generated.graphql.GetEventQuery;
import com.amazonaws.amplify.generated.graphql.ListEventsQuery;
import com.amazonaws.amplify.generated.graphql.SubscribeToEventCommentsSubscription;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class AppSyncClientGQlSchemaOperationCalls {
    private AWSAppSyncClient mAWSAppSyncClient;
    private AppSyncSubscriptionCall<SubscribeToEventCommentsSubscription.Data> subscriptionWatcher;

    public AppSyncClientGQlSchemaOperationCalls(AWSAppSyncClient mAWSAppSyncClient){
        this.mAWSAppSyncClient = mAWSAppSyncClient;
    }

    // getEvent query to return results for event ID
    public void callGetEventQuery(String eventId){
        mAWSAppSyncClient.query(
                GetEventQuery.builder().id(eventId).build()).responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(new GraphQLCall.Callback<GetEventQuery.Data>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(@Nonnull Response<GetEventQuery.Data> response) {
                        GetEventQuery.GetEvent event = response.data().getEvent();
                        Log.i("GetEvent results id: ", event.id());
                        Log.i("GetEvent results name: ", event.name());
                        Log.i("GetEvent results where: ", event.where());
                        Log.i("GetEvent results when: ", event.when());
                        Log.i("GetEvent results description: ", event.description());
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e("ERROR GetEvent QUERY", e.toString());
                        e.printStackTrace();
                    }
                });
    }

    // ListEvents call to return results of all stored events
    public void callListEventsQuery(){
        mAWSAppSyncClient.query(ListEventsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(new GraphQLCall.Callback<ListEventsQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ListEventsQuery.Data> response) {
                        Log.i("Results of ListEvents: ", response.data().listEvents().items().toString());
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e("ERROR ListEvents QUERY", e.toString());
                        e.printStackTrace();
                    }
                });
    }

    // CreateEvent Mutation
    public void callCreateEventMutation(){
        mAWSAppSyncClient.mutate(CreateEventMutation.builder()
                .name("MyAmplifyApp2")
                .where("AndroidStudio application execution, Realtime and Offline")
                .when("first on sep 11 2020")
                .description("test1 for mutating query AppSync")
                .build())
                .enqueue(new GraphQLCall.Callback<CreateEventMutation.Data>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(@Nonnull Response<CreateEventMutation.Data> response) {
                        Log.i("Results For CreateEventMutation", "Successfully Added Event");
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e("Error For CreateEventMutation", e.toString());
                    }
                });
    }

    // DeleteEvent Mutation
    public void callDeleteEventMutation(String eventId){
        mAWSAppSyncClient.mutate(DeleteEventMutation.builder()
                .id(eventId)
                .build())
                .enqueue(new GraphQLCall.Callback<DeleteEventMutation.Data>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(@Nonnull Response<DeleteEventMutation.Data> response) {
                        Log.i("Results For DeleteEventMutation", "Successfully Deleted Event");
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e("Error For DeleteEventMutation", e.toString());
                    }
                });
    }

    // Not working Subscription function code
    public void callSubscribeToEventCommentsSubscription(String eventId) {
        SubscribeToEventCommentsSubscription subscription = SubscribeToEventCommentsSubscription.builder().eventId(eventId).build();
        subscriptionWatcher = mAWSAppSyncClient.subscribe(subscription);
        subscriptionWatcher.execute(new AppSyncSubscriptionCall.Callback<SubscribeToEventCommentsSubscription.Data>() {
            @Override
            public void onResponse(@Nonnull Response<SubscribeToEventCommentsSubscription.Data> response) {
                Log.i("Subscription", response.data().toString());
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Failed Subscription", e.toString());
            }

            @Override
            public void onCompleted() {
                Log.i("Subscription", "Subscription completed sucessfully");
            }
        });
    }

}

