package kr.nearbyme.nbm.Writereview;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.nearbyme.nbm.R;
import kr.nearbyme.nbm.Review.ReviewDetailActivity;
import kr.nearbyme.nbm.data.Designer;
import kr.nearbyme.nbm.data.ItemDataList;
import kr.nearbyme.nbm.data.Shop;
import kr.nearbyme.nbm.data.ShopDsnrResult;
import kr.nearbyme.nbm.data.ShopNameResult;
import kr.nearbyme.nbm.data.WriteResult;
import kr.nearbyme.nbm.manager.NetworkManager;
import kr.nearbyme.nbm.manager.PropertyManager;
import okhttp3.Request;

//import kr.nearbyme.nbm.Writereview.ItemData;

/**
 * A simple {@link Fragment} subclass.
 */
public class WriteReviewFragment extends Fragment{
    EditText storeNameView, designerNameView;
    RatingBar ratingBar;
    Button buttonTag, buttonPost;
    TextView filterTagsView;
    EditText contentView;
    ImageView ImageUploadView;

    ScrollView scrollView;

//    Write mData;
    private String shop_id;
    private String dsnr_id;
    private double post_score;
    private String post_content;
    List<String> post_filters = new ArrayList<String>();

    ItemData selectItem = null;
    ItemData selectItem_dsnr = null;

    List<String> shop_ids = new ArrayList<>();
    List<String> shop_names = new ArrayList<>();
    List<String> dsnr_names = new ArrayList<>();

    List<ItemData> shopList = new ArrayList<>();

    /*private void getShopName(){
        NetworkManager.getInstance().getShopNameList(new NetworkManager.OnResultListener<ShopNameResult>() {
            @Override
            public void onSuccess(Request request, ShopNameResult result) {
                ItemData temp = new ItemData();
                for(Shop input: result.result) {
//                    shop_ids.add(input.getShop_id());
//                    shop_names.add(input.getShop_name());

                    temp.id = input.getShop_id();
                    temp.name = input.getShop_name();
                    shopList.add(temp);

                    //AutoCompleteTExtView 먼저 구현
                }
//                Log.i("log_kwon", shop_names.size() + "");

            }

            @Override
            public void onFail(Request request, IOException exception) {

            }
        });

    }*/

/*    private void getDsnr(){

        NetworkManager.getInstance().getShopDsnrlist(shop_id, new NetworkManager.OnResultListener<ShopDsnrResult>() {
            @Override
            public void onSuccess(Request request, ShopDsnrResult result) {

                for(Designer input: result.result) {
                    dsnr_names.add(input.getDsnr_name());
                }

            }

            @Override
            public void onFail(Request request, IOException exception) {
                Toast.makeText(getContext(), "exception : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }*/



    private static final int RC_GALLERY = 1;
    private static final int RC_CAMERA = 2;

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraCaptureFile());
        startActivityForResult(intent, RC_CAMERA);
    }


    File mCameraCaptureFile;

    private Uri getCameraCaptureFile() {
        File dir = getContext().getExternalFilesDir("myphoto");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mCameraCaptureFile = new File(dir, "my_photo_"+System.currentTimeMillis()+".jpg");
        return Uri.fromFile(mCameraCaptureFile);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent, RC_GALLERY);
    }

    File mUploadFile = null;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mUploadFile != null) {
            outState.putString("uploadfile", mUploadFile.getAbsolutePath());
        }
        if (mCameraCaptureFile != null) {
            outState.putString("cameraFile", mCameraCaptureFile.getAbsolutePath());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(uri, projection, null, null, null);
                if (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    mUploadFile = new File(path);
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(path, opts);
                    ImageUploadView.setImageBitmap(bm);
                }
            }
            return;
        }

        if (requestCode == RC_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {


//                Toast.makeText(MyApplication.getContext(), mCameraCaptureFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                File file = mCameraCaptureFile;
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inSampleSize = 2;
//                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                mUploadFile = file;
                Glide.with(this).load(mUploadFile).into(ImageUploadView);
//                ImageUploadView.setImageBitmap(bm);

            }
            return;
        }

//        onActivityResult(requestCode, resultCode, data); //
    }





    public WriteReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getShopName();
//        getDsnr();

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("uploadfile");
            if (!TextUtils.isEmpty(path)) {
                mUploadFile = new File(path);
            }
            path = savedInstanceState.getString("cameraFile");
            if (!TextUtils.isEmpty(path)) {
                mCameraCaptureFile = new File(path);
            }
        }

        mAdapter = new ArrayAdapter<ItemData>(getContext(), android.R.layout.simple_list_item_1);
    }

    private void initData() {
//        shop_id = storeNameView.getText().toString();
//        dsnr_id = designerNameView.getText().toString();
        shop_id = selectItem.id;
        dsnr_id = selectItem_dsnr.id;
        post_score = ratingBar.getRating();
        post_content = contentView.getText().toString();
        post_filters = PropertyManager.getInstance().getWritePostfilter();
        Log.i("log_kwon", "post_filters size: " + post_filters.size() );
        for(int i = 0; i < post_filters.size(); i ++){
            Log.i("log_kwon", "each post_filters[" + i + "]: " + post_filters.get(i));
        }


        NetworkManager.getInstance().getPostUpload(getContext(), shop_id, dsnr_id, post_score, post_content, post_filters, mUploadFile, new NetworkManager.OnResultListener<WriteResult>() {


            @Override
            public void onSuccess(Request request, WriteResult result) {
                Intent intent = new Intent(getContext(), ReviewDetailActivity.class);
                intent.putExtra(ReviewDetailActivity.EXTRA_REVIEW_ID, result.result.post_id);
                startActivity(intent);

            }

            @Override
            public void onFail(Request request, IOException exception) {
                Toast.makeText(getContext(), "server disconnected", Toast.LENGTH_SHORT).show();
            }
        });




    }



    ListPopupWindow popupWindow, popupWindow2;
    ArrayAdapter<ItemData> mAdapter;


    boolean isForced = false;
    boolean isForced2 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_review, container, false);

        /* get shop name list for writePost */
        popupWindow = new ListPopupWindow(getContext());
        scrollView = (ScrollView) view.findViewById(R.id.scrollView2);
        storeNameView = (EditText) view.findViewById(R.id.autoCompleteTextView_store);
        popupWindow.setAnchorView(storeNameView);
        popupWindow.setAdapter(mAdapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem = mAdapter.getItem(position);
                isForced = true;
                storeNameView.setText(selectItem.name);
                isForced = false;
                Toast.makeText(getContext(), "매장 이름:" + selectItem.name, Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                storeNameView.setFocusableInTouchMode(false);
            }
        });
        storeNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && !isForced) {

                    NetworkManager.getInstance().getShopNameList(text, new NetworkManager.OnResultListener<ItemDataList>() {
                        @Override
                        public void onSuccess(Request request, ItemDataList result) {
                            mAdapter.clear();
                            mAdapter.addAll(result.result);
                            if (mAdapter.getCount() > 0) {
                                popupWindow.show();
                            }
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {

                        }
                    });
                } else {
                    mAdapter.clear();
                    popupWindow.dismiss();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /* get designer list for writePost */
        designerNameView = (EditText) view.findViewById(R.id.autoCompleteTextView_dsnr);
        popupWindow2 = new ListPopupWindow(getContext());
        popupWindow2.setAnchorView(designerNameView);
        popupWindow2.setAdapter(mAdapter);

        popupWindow2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem_dsnr = mAdapter.getItem(position);
                isForced2 = true;
                designerNameView.setText(selectItem_dsnr.name);
                isForced2 = false;

            }
        });

        designerNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString();

                if (!TextUtils.isEmpty(keyword)  && !isForced2) {

                    NetworkManager.getInstance().getDsnrNameList(selectItem.id, keyword, new NetworkManager.OnResultListener<ItemDataList>() {
                        @Override
                        public void onSuccess(Request request, ItemDataList result) {
                            mAdapter.clear();
                            mAdapter.addAll(result.result);
                            if (mAdapter.getCount() > 0) {
                                popupWindow2.show();
                            }
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {

                        }
                    });
                } else {
                    mAdapter.clear();
                    popupWindow2.dismiss();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_review);
        buttonTag = (Button) view.findViewById(R.id.btn_keyword);
        filterTagsView = (TextView) view.findViewById(R.id.text_keywords);
        contentView = (EditText) view.findViewById(R.id.edit_review);
        ImageUploadView = (ImageView) view.findViewById(R.id.image_upload);
        buttonPost = (Button) view.findViewById(R.id.btn_post);

        if (mUploadFile != null) {
            Glide.with(this).load(mUploadFile).into(ImageUploadView);
        }

//        ArrayAdapter<String> autoShopNames = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, shop_names);
//        storeNameView.setAdapter(autoShopNames);
//
//        storeNameView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
//            }
//        });

/*        designerNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.i("log_kwon", )
            }
        });*/


        ImageUploadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("사진올리기");
                builder.setPositiveButton("앨범에서 선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImageFromGallery();

                    }
                });
                builder.setNegativeButton("카메라 촬영", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImageFromCamera();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initData();
                storeNameView.setText("");
                designerNameView.setText("");
                ratingBar.setRating(0.0f);
                contentView.setText("");
                ImageUploadView.setImageResource(R.drawable.common_google_signin_btn_icon_light_pressed);


            }
        });

        buttonTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WritePostKeywordFragment f = new WritePostKeywordFragment();
                //f.setWritePostKeyWordDoneClickListener(WriteReviewFragment.this);
                f.show(getActivity().getSupportFragmentManager(), "create");

//                Log.d("키워드 눌림", PropertyManager.getInstance().getWritePostfilter().get(0));

            }
        });



        return view;
    }


//    @Override
//    public void onWritePostKeyWordDoneClick(List<String> keyFilters) {
//        PropertyManager.getInstance().setWritePostfilter(keyFilters);
//
//    }

//    @Override
//    public void onNotifyUpdate() {
//        initData();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        ((MainActivity)getActivity()).removeOnNotifyUpdateListener(this);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((MainActivity)getActivity()).addOnOnNotifyUpdateListener(this);
//        post_filters = PropertyManager.getInstance().getWritePostfilter();
//
//        initData();
//    }
}
