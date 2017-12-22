package app.android.com.rxjavasampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Disposable disposable;
    EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etSearch = findViewById(R.id.et_search);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Akash");
        arrayList.add("Ankit");
        arrayList.add("Krishna");
        arrayList.add("Hutendra");
        arrayList.add("Rajat");

        /*
        Creating Observable from "Observable.fromCallable"
         */
        disposable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "From Callable";
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("Callable onSubscribe " + s, "From Callable onSubscribe");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Callable onSubscribe", "From Callable onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Callable onSubscribe", "From Callable onSubscribe");
                    }
                });

        /*
        Creating Observable from "Observable.just"
         */
        disposable = Observable.just("just string")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("just onSubscribe" + s, "just onSubscribe");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("just onSubscribe", "just onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("just onSubscribe", "just onSubscribe");
                    }
                });

        /*
        Creating Observable from "Observable.fromIterable"
         */
        disposable = Observable.fromIterable(arrayList)
//                .compose(this.<String>bindToLifecycle())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("donOnNext", Thread.currentThread().getName() + s + "changeMe");
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return "" + s;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("onSubscribe" + s, "onSubscribe");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onSubscribe", "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("onSubscribe", "onSubscribe");
                    }
                });


         /*
        Creating Observable from "Observable.create"
         */
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext("onNext A Called");
                e.onNext("onNext B Called");
                e.onComplete();
            }
        });

        disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("Got It onNext", s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Got It onError", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Got It onComplete", "Got It onComplete");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
