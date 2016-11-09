package com.starsutdio.biglz.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.starsutdio.biglz.R;
import com.starsutdio.biglz.activity.adapter.GridViewAdapter;
import com.starsutdio.biglz.activity.adapter.PopupWindowListAdapter;
import com.starsutdio.biglz.contract.UserContract;
import com.starsutdio.biglz.model.http.BigLZApi;
import com.starsutdio.biglz.presenter.UserPresenter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class WriteArticleActivity extends AppCompatActivity implements View.OnClickListener ,UserContract.View {

    @BindView(R.id.title_edit)
    EditText title_edit;
    @BindView(R.id.content_edit)
    EditText content_edit;
    @BindView(R.id.photos_fab)
    FloatingActionButton fab;
    @BindView(R.id.gridView)
    GridView gridView;

    private String title,content;
    private String uid,createTime;
    private String acckey;
    private HashMap<String,List<String>> image_map;
    private List<String> parentList;
    private List<String> image_path;
    private List<String> top_image_list;
    private Map<String,RequestBody> map;
    private List<MultipartBody.Part> mList;
    private final static int SELECT_CODE = 1;
    private List<Bitmap> list;
    private UserPresenter mPresenter;
    private GridViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);
        ButterKnife.bind(this);
        fab.setOnClickListener(this);

//        String base_url = "http://10.0.0.15:8081/api/";
//        File file = new File("/storage/emulated/0/Download/u=4155302816,1201715785&fm=21&gp=0.jpg");
//        File file1 = new File("/storage/emulated/0/Download/503008.png");
//        if (file1==null) Log.d("------------","file null");
//        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"),file1);
//        MultipartBody.Part body1 = MultipartBody.Part.createFormData("imgupload[]",file1.getName(),requestFile1);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("imgupload[]",file.getName(),requestFile);
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("imgupload",file.getName(),requestFile)
//                .addFormDataPart("imgupload",file1.getName(),requestFile1);
////        map.put("imgupload"+0+"\"; filename=\""+file.getName(),requestFile);
////        map.put("imgupload"+1+"\"; filename=\""+file1.getName(),requestFile1);
//        mList.add(body1);
//        mList.add(body);
        mPresenter = new UserPresenter(this);
        list = new ArrayList<>();

        mAdapter = new GridViewAdapter(this,list);
        gridView.setAdapter(mAdapter);


    }
//        image_path = new ArrayList<>();
//        image_map = new HashMap<>();
//        top_image_list = new ArrayList<>();
//        parentList = new ArrayList<>();
//        getImageFromLocal();
//    }
//
//        mAdapter = new PopupWindowListAdapter(this,parentList,top_image_list);
////        getImageFromLocal();


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.photos_fab:
                postPassage();

                break;

        }
    }


    void showPopupWindow(){
        View popup_view = LayoutInflater.from(this).inflate(R.layout.popup_photos,null,false);
        View root_view = LayoutInflater.from(this).inflate(R.layout.activity_write_article,null,false);
        final PopupWindow popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setContentView(popup_view);

        popupWindow.showAtLocation(root_view, Gravity.BOTTOM,0,0);
        TextView fromPhone = (TextView) popup_view.findViewById(R.id.fromPhone);
        TextView takephoto = (TextView) popup_view.findViewById(R.id.takephotos);
        TextView cancle = (TextView) popup_view.findViewById(R.id.cancle);

        fromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,SELECT_CODE);


            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CODE){
            String image_path = getImagepath(data.getData());
            addImg(image_path);
            
        }
    }

    public String getImagepath(Uri image_uri){
        Cursor cursor = getContentResolver().query(image_uri,null,null,null,null);
        cursor.moveToFirst();
        String image_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return image_path;
    }
    public void postPassage(){
        title = title_edit.getText().toString();
        content = content_edit.getText().toString();
        createTime = System.currentTimeMillis()+"";
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject detail = new JsonObject();
        jsonObject.addProperty("uid",uid);
        jsonObject.addProperty("creatTime",createTime);
        jsonObject.addProperty("content",content);
        jsonObject.add("imglist",jsonArray);
        jsonObject.add("detail",detail);
        mPresenter.post(acckey,jsonObject);
    }
    public void addImg(String imgpath){

    }


    //    void getImageFromLocal(){
//                int position =0;
//                Uri image_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                String sdPath = Environment.getExternalStorageDirectory().toString();
//
//                ContentResolver contentResolver = WriteArticleActivity.this.getContentResolver();
//                Cursor cursor = contentResolver.query
//                        (image_uri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                        + MediaStore.Images.Media.MIME_TYPE + "=?",
//                                new String[]{"image/jpeg", "image/png"},
//                                MediaStore.Images.Media.DATE_MODIFIED);
//
//                if (cursor == null) {
//                    Log.d("------------", "no cursor");
//                }
//                while (cursor.moveToNext()) {
//                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                    String parentName = new File(path).getParentFile().getName();
//                    Log.d("--------------",path);
//                    if (!parentList.contains(parentName)){
//                        parentList.add(parentName);
//                    }
//                    if (!image_map.containsKey(parentName)){
//                        image_map.put(parentName,image_path);
//
//                    }
//                    image_map.get(parentName).add(path);
//                    if (!top_image_list.contains(image_map.get(parentName).get(0))){
//                        top_image_list.add(image_map.get(parentName).get(0));
//                    }
//                }
//                Log.d("--------------",top_image_list.size()+"");
//
//
//    }

    //    void showPhotosPopupWindow(){
//        View view = LayoutInflater.from(this).inflate(R.layout.popup_photos_list,null,false);
//        View root_view = LayoutInflater.from(this).inflate(R.layout.activity_write_article,null,false);
//        ListView listview = (ListView) view.findViewById(R.id.photos_list);
//
//        listview.setAdapter(mAdapter);
//        PopupWindow popupwindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
//        popupwindow.setContentView(view);
//        popupwindow.showAtLocation(root_view,Gravity.BOTTOM,0,0);
//    }
}
