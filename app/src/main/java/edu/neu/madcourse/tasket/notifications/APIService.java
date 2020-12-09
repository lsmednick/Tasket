package edu.neu.madcourse.tasket.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA0Rl5tq0:APA91bFWZwd7fHtHLdmYB9KKWmcWd7PomuGuxq7mWTK6lGN2eR2NyjaagA1YPMHfHFCJwUw1Xsjko9Tdq_KfbDswgS3rBwhwiclgrXrCiXa_zc4xW0Nu4HW8smAY9ZFQgAL4yajJNv5y"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
