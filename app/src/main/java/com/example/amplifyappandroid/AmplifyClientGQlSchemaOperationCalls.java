package com.example.amplifyappandroid;

import android.annotation.SuppressLint;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateEventMutation;
import com.amazonaws.amplify.generated.graphql.GetEventQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.SimpleGraphQLRequest;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.Collections;

import javax.annotation.Nonnull;

public class AmplifyClientGQlSchemaOperationCalls {

    // CreateEvent Mutation | This won't work as per bug https://docs.amplify.aws/lib/graphqlapi/getting-started/q/platform/android#configure-api and https://github.com/kcwinner/cdk-appsync-transformer-demo
//    public void callCreateEventMutation(){
//        CreateEventMutation createEventMutation = CreateEventMutation.builder()
//                .name("MyAmplifyApp2")
//                .where("AndroidStudio application execution, Realtime and Offline")
//                .when("first on sep 11 2020")
//                .description("test1 for mutating query AppSync")
//                .build();
//
//
//        Amplify.API.mutate(ModelMutation.create(CreateEventMutation),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().toString()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//
//        Amplify.API.query(
//                ModelQuery.get(GetEventQuery.class, id),
//                response -> Log.i("MyAmplifyApp", ((Event) response.getData()).getName()),
//                error -> Log.e("MyAmplifyApp", error.toString(), error)
//        );
//    }

}
