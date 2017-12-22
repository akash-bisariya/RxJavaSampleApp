package app.android.com.myrxjavaapplication;

import android.arch.lifecycle.Lifecycle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.create;

public class MainActivity extends RxActivity {
    Disposable disposable;
    EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Akash");
        arrayList.add("Ankit");
        arrayList.add("Krishna");
        arrayList.add("Hutendra");
        arrayList.add("Rajat");

        etSearch=findViewById(R.id.et_search);
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "From Callable";
            }
        }).subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d("Callable onSubscribe "+s,"From Callable onSubscribe");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Callable onSubscribe","From Callable onSubscribe");
            }

            @Override
            public void onComplete() {
                Log.d("Callable onSubscribe","From Callable onSubscribe");
            }
        });
        disposable =Observable.fromIterable(arrayList)
                .compose(this.<String>bindToLifecycle())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("donOnNext",Thread.currentThread().getName()+s+"changeMe");
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return ""+s;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Log.d("onSubscribe"+s,"onSubscribe");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onSubscribe","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("onSubscribe","onSubscribe");
                    }
                });



        Observable<String> observable=Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                e.onNext("onNext A Called");
                e.onNext("onNext B Called");
                e.onComplete();
            }
        });

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("Got It onSubscribe","Got It onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("Got It onNext",s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Got It onError",e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Got It onComplete","Got It onComplete");
                    }
                });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
