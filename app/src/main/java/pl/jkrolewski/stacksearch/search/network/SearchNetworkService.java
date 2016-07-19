package pl.jkrolewski.stacksearch.search.network;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import pl.jkrolewski.stacksearch.search.model.SearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface SearchNetworkService {

    @NonNull
    @CheckResult
    @GET("/2.2/search")
    Observable<SearchResponse> findQuestions(@NonNull @Query("intitle") String phrase);
}
