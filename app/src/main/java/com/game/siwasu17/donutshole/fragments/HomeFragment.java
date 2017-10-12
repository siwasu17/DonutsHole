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

import com.game.siwasu17.donutshole.HueAdapter;
import com.game.siwasu17.donutshole.ImageDetailActivity;
import com.game.siwasu17.donutshole.ImageRepository;
import com.game.siwasu17.donutshole.MainActivity;
import com.game.siwasu17.donutshole.R;
import com.game.siwasu17.donutshole.ServiceFactory;
import com.game.siwasu17.donutshole.models.ImageEntry;

/*
import com.game.siwasu17.donutshole.models.ImageEntry_Selector;
import com.game.siwasu17.donutshole.models.OrmaDatabase;
import com.github.gfx.android.orma.AccessThreadConstraint;
import com.github.gfx.android.orma.BuildConfig;
import com.github.gfx.android.orma.Inserter;
*/
import com.game.siwasu17.donutshole.services.TiqavService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private HueAdapter mHueAdapter;
    private List<ImageEntry> mImageEntryList = new ArrayList<>();

    SearchView mSearchView;

    boolean searchWordChanged = false;
    //検索ワード
    String searchWord;

    public static final String IMAGE_CACHE_KEY = "IMAGE_CACHE_KEY";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View currentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(currentView, savedInstanceState);

        mGridView = (GridView) currentView.findViewById(R.id.gridview);

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
                        callTiqavService();
                    }
                } else {
                    loading = false;
                }

            }
        });


        //詳細画面への遷移
        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            ImageEntry imageEntry = mImageEntryList.get(position);
            System.out.println(
                    MessageFormat.format("pos: {0}, id: {1}, ImageID: {2}",
                            position, id, imageEntry.id)
            );

            //FIXME: とりあえずお試しコード
            ImageRepository imageRepository = new ImageRepository(getActivity());
            System.out.println("FAVED: " + imageRepository.getFavoriteImages());


            //Bitmap取得のためのcallback
            Target mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    System.out.println("Bitmap get complete");

                    File cachePath = new File(getActivity().getApplicationContext().getCacheDir(), "images");
                    cachePath.mkdirs();
                    File filePath = new File(cachePath, imageEntry.id + ".jpg");

                    try (FileOutputStream stream = new FileOutputStream(filePath)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Bitmap save: " + filePath.getAbsolutePath());

                    //Intent intent = new Intent(MainActivity.this, ImageDetailActivity.class);
                    Intent intent = new Intent(getContext(), ImageDetailActivity.class);
                    //キャッシュファイルのパスを送信
                    intent.putExtra(IMAGE_CACHE_KEY, filePath.toString());
                    startActivity(intent);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    System.out.println("Bitmap load failed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            //実画像を取得して、画像詳細画面へ遷移
            //callbackが重なるためこんな作りになってる
            Picasso.with(getContext())
                    .load(imageEntry.getRealUrl())
                    .into(mTarget);

        });

        //画像配列とそのアダプタを生成
        mHueAdapter = new HueAdapter(getContext(), mImageEntryList);
        //初期画像のロード
        callTiqavService();
    }


    /**
     * TiqavのAPIを叩いてGridViewに画像を反映させる
     */
    private void callTiqavService() {
        //Interfaceから実装を取得
        /*
        TiqavService tiqavService = ServiceFactory.createTiqavService();
        Observable<ImageEntry[]> apiCall;
        if (null != searchWord && !searchWord.isEmpty()) {
            if (searchWordChanged) {
                mImageEntryList.clear();
                mGridView.setAdapter(null);
                apiCall = tiqavService.search(searchWord);
                searchWordChanged = false;
            } else {
                //do nothing
                return;
            }
        } else {
            //TODO: searchNewestして、page番号をつけたらページネーションできるかも
            apiCall = tiqavService.searchRandom();
        }
        */
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();

        //TODO:これもFragmentに紐付けていいかも
        ImageRepository imageRepository = new ImageRepository(getActivity());
        imageRepository.subscribeRandomImages(
                imgs -> {
                    System.out.println(imgs);

                    //リストに画像要素を追加していく
                    // adapterで関連付けられているので要素追加するだけでOK
                    mImageEntryList.addAll(Arrays.asList(imgs));
                    if (null == mGridView.getAdapter()) {
                        //初回だけGridViewにアダプタを関連付け
                        mGridView.setAdapter(mHueAdapter);
                    }

                    mGridView.invalidate();
                });
    }
}
