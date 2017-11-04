package com.game.siwasu17.donutshole.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.game.siwasu17.donutshole.ImageDetailActivity;
import com.game.siwasu17.donutshole.ServiceFactory;
import com.game.siwasu17.donutshole.TiqavImageAdapter;
import com.game.siwasu17.donutshole.TiqavImageRepository;
import com.game.siwasu17.donutshole.R;
import com.game.siwasu17.donutshole.models.TiqavImageEntry;
import com.game.siwasu17.donutshole.services.TiqavService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private GridView mGridView;
    private TiqavImageAdapter mTiqavImageAdapter;

    SearchView mSearchView;

    boolean searchWordChanged = false;
    //検索ワード
    String searchWord;

    public static final String IMAGE_ENTRY_KEY = "IMAGE_ENTRY_KEY";

    public static final String ARG_MODE = "ARG_MODE";

    public static final String MODE_HOME = "MODE_HOME";
    public static final String MODE_FAV = "MODE_FAV";


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String mode) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }


    private boolean setSearchWord(String searchWord) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setTitle(searchWord);
        actionBar.setDisplayShowTitleEnabled(true);
        if (searchWord != null && !searchWord.equals("")) {
            // searchWordがあることを確認
            this.searchWord = searchWord;
            this.searchWordChanged = true;
        }
        // 虫眼鏡アイコンを隠す
        mSearchView.setIconified(false);
        // SearchViewを隠す
        mSearchView.onActionViewCollapsed();
        // Focusを外す
        mSearchView.clearFocus();
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View currentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(currentView, savedInstanceState);

        mGridView = (GridView) currentView.findViewById(R.id.gridview);

        //詳細画面への遷移
        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), ImageDetailActivity.class);
            //キャッシュファイルのパスを送信
            TiqavImageEntry clickedImageEntry = (TiqavImageEntry) parent.getAdapter().getItem((int) id);
            intent.putExtra(IMAGE_ENTRY_KEY, clickedImageEntry);
            startActivity(intent);
        });

        mTiqavImageAdapter = new TiqavImageAdapter(getContext());

        Bundle args = this.getArguments();
        //TODO: 汎用化できそうならクラス分ける
        switch(args.getString(ARG_MODE)){
            case MODE_HOME:
                mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    private boolean loading = true;

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (totalItemCount == (firstVisibleItem + visibleItemCount)) {
                            if (!loading) {
                                int pos = absListView.getFirstVisiblePosition();
                                //ロード中でなければロード
                                System.out.println("Load! " + pos);

                                loading = true;
                                appendRandomImages();
                            }
                        } else {
                            loading = false;
                        }

                    }
                });

                //初期画像のロード
                appendRandomImages();
                break;
            case MODE_FAV:
                showFavedImages();
                break;
            default:
                throw new RuntimeException("Unknown mode");
        }
    }


    /**
     * TiqavのAPIを叩いてGridViewに画像を反映させる
     */
    private void appendRandomImages() {
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();

        TiqavService tiqavService = ServiceFactory.createTiqavService();
        tiqavService.searchRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imgs -> {
                    mTiqavImageAdapter.appendElements(Arrays.asList(imgs));
                    if (null == mGridView.getAdapter()) {
                        //初回だけGridViewにアダプタを関連付け
                        mGridView.setAdapter(mTiqavImageAdapter);
                    }
                    mGridView.invalidate();
                });
    }


    private void showFavedImages(){
        //お気に入り画像を取得して登録
        List<TiqavImageEntry> entryList = TiqavImageRepository.getInstance(this.getContext()).getFavoriteImages();
        mTiqavImageAdapter.appendElements(entryList);
        mGridView.setAdapter(mTiqavImageAdapter);
        mGridView.invalidate();
    }
}
