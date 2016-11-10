package com.starsutdio.biglz.activity;

import android.app.Activity;
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
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.starsutdio.biglz.utils.BitmapUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private String acckey = "";
    private String [] delete = {"删除"};
    private HashMap<String,List<String>> image_map;
    private List<String> mList;


    private final static int SELECT_CODE = 1;
    private final static int TAKE_PHOTO = 2;
    private List<Bitmap> list;
    private UserPresenter mPresenter;
    private GridViewAdapter mAdapter;
    private Bitmap bitmap;
    private ListPopupWindow mListPopupWindow;
    private File mFile;
    private RequestBody mRequestBody;
    private MultipartBody.Part mPart;
    private List<MultipartBody.Part> mPartList;
    private String imagename;
    private final static String PATH = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);
        ButterKnife.bind(this);
        fab.setOnClickListener(this);

        mPresenter = new UserPresenter(this);
        list = new ArrayList<>();
        mList = new ArrayList<>();

        mAdapter = new GridViewAdapter(this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                if (i ==0){
                    showPopupWindow();
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> view, View view1, int i, long l) {
                if (i!=0){
                    deletePopupWindow(i);
                    mListPopupWindow.setAnchorView(view1);
                    mListPopupWindow.show();
                }
                return false;
            }
        });


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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent,TAKE_PHOTO);


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
        if (data!=null){
        if (requestCode == SELECT_CODE){

            String image_path = getImagepath(data.getData());
            mList.add(image_path);
            addImg(image_path);
            
        }
        if (requestCode == TAKE_PHOTO) {
            FileOutputStream fos = null;
            Bundle bundle = data.getExtras();
            Bitmap bitmap_tmp = (Bitmap) bundle.get("data");
            File file = new File(PATH);
            if (!file.exists()){
                file.mkdir();
            }
            imagename = PATH+System.currentTimeMillis()+".jpg";
            try {
                fos = new FileOutputStream(imagename);
                bitmap_tmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {


                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mList.add(imagename);
            addImg(imagename);
        }

        }else {
            Log.d("-------------","data null");
        }
    }

    public String getImagepath(Uri image_uri){
        String [] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(image_uri, filePathColumns,null,null,null);
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
        imgUpload();
        mPresenter.post(acckey,jsonObject);
        mPresenter.imgupload(mPartList);

    }
    public void addImg(String imgpath){
        bitmap = BitmapUtil.decodeBitmapFromFile(imgpath,100,100);
        if (!list.contains(bitmap)) {
            list.add(bitmap);
            mAdapter.setImageList(list);
        }

    }

    public void deleteImg(int position){
        list.remove(position-1);
        mAdapter.setImageList(list);
    }

    public void deletePopupWindow(final int position){
        mListPopupWindow = new ListPopupWindow(this);
        mListPopupWindow.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,delete));
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                deleteImg(position);
                mListPopupWindow.dismiss();
            }
        });
    }

    public void imgUpload(){
        mPartList = new ArrayList<>();
                for (int i=0;i<mList.size();i++){
                    mFile = new File(mList.get(i));
                    mRequestBody = RequestBody.create(MediaType.parse("Multipart/form-data"),mFile);
                    mPart = MultipartBody.Part.createFormData("imgupload[]",mFile.getName(),mRequestBody);
                    mPartList.add(mPart);
                }


    }

    @Override
    public void showSuccessDialog() {

    }

    @Override
    public void showFailureDialog() {

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
