package com.link2me.android.pdfviewer.ui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.link2me.android.common.BackPressHandler;
import com.link2me.android.common.Utils;
import com.link2me.android.common.Value;
import com.link2me.android.pdfviewer.R;
import com.link2me.android.pdfviewer.adapter.BindPdfViewListAdapter;
import com.link2me.android.pdfviewer.databinding.ActivityMainBinding;
import com.link2me.android.pdfviewer.model.PdfResult;
import com.link2me.android.pdfviewer.model.Pdf_Item;
import com.link2me.android.pdfviewer.network.RetrofitAdapter;
import com.link2me.android.pdfviewer.network.RetrofitService;
import com.link2me.android.pdfviewer.network.RetrofitUrl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BindPdfViewListAdapter.OnItemClickListener {
    private final String TAG = this.getClass().getSimpleName();
    Context mContext;

    private ActivityMainBinding binding;

    private ArrayList<Pdf_Item> pdfItemList = new ArrayList<>();
    private BindPdfViewListAdapter mAdapter;

    DownloadPdfFromURL dnPdf;
    private File outputFile;

    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        backPressHandler = new BackPressHandler(this); // 뒤로 가기 버튼 이벤트
        mContext = MainActivity.this;
        initView();
    }

    private void initView() {
        createPdfList(); // 서버 데이터 가져오기
        buildRecyclerView();
    }

    private void createPdfList() {
        String keyword = Value.encryptAES(Value.URLkey());
        RetrofitService service = RetrofitAdapter.getClient().create(RetrofitService.class);
        Call<PdfResult> call = service.getPdfData(keyword,"");
        call.enqueue(new Callback<PdfResult>() {
            @Override
            public void onResponse(Call<PdfResult> call, Response<PdfResult> response) {
                if(response.body().getStatus().contains("success")){
                    pdfItemList.clear(); // 서버에서 가져온 데이터 초기화

                    for(Pdf_Item item: response.body().getPdfinfo()){
                        pdfItemList.add(item);
                    }

                    // runOnUiThread()를 호출하여 실시간 갱신한다.
                    runOnUiThread(() -> {
                        // 갱신된 데이터 내역을 어댑터에 알려줌
                        mAdapter.notifyDataSetChanged();
                    });
                } else {
                    Utils.showAlert(mContext,response.body().getStatus(),response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<PdfResult> call, Throwable t) {

            }
        });
    }

    private void buildRecyclerView(){
        binding.pdfListview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mAdapter = new BindPdfViewListAdapter(mContext,pdfItemList); // 객체 생성

        DividerItemDecoration decoration = new DividerItemDecoration(mContext,manager.getOrientation());
        binding.pdfListview.addItemDecoration(decoration);
        binding.pdfListview.setLayoutManager(manager);
        binding.pdfListview.setAdapter(mAdapter);

        mAdapter.setOnItemSelectClickListener(this);
    }

    @Override
    public void onItemClicked(View view, Pdf_Item item, int position) {
//        Log.d(TAG, RetrofitUrl.BASE_URL+item.getPdfurl());
        Toast.makeText(getApplicationContext(), "잠시 기다리시면 "+item.getTitle()+" PDF 파일 열람이 가능합니다", Toast.LENGTH_LONG).show();
        String PDFUrl = RetrofitUrl.BASE_URL+item.getPdfurl();
        downloadPDF(PDFUrl);
    }

    private void downloadPDF(String fileUrl) {
        // 백그라운드 객체를 만들어 주어야 다운로드 취소가 제대로 동작됨
        dnPdf = new DownloadPdfFromURL();
        dnPdf.execute(fileUrl);
    }

    class DownloadPdfFromURL extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            int count;
            int lenghtOfFile = 0;
            InputStream input = null;
            OutputStream fos = null;

            File filePath = new File(Environment.getExternalStorageDirectory() + "/download");
            outputFile = new File(filePath, "tempPDF.pdf");
            if (outputFile.exists()) { // 기존 파일 존재시 삭제하고 다운로드
                outputFile.delete();
            }

            try {
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                lenghtOfFile = connection.getContentLength(); // 파일 크기를 가져옴

                input = new BufferedInputStream(url.openStream());
                fos = new FileOutputStream(outputFile);
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                    }
                    total = total + count;
                    if (lenghtOfFile > 0) { // 파일 총 크기가 0 보다 크면
                        publishProgress((int) (total * 100 / lenghtOfFile));
                    }
                    fos.write(data, 0, count); // 파일에 데이터를 기록
                }

                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if (result == null) {
                // 미디어 스캐닝
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{outputFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });

                // 다운로드한 파일 실행하여 업그레이드 진행하는 코드
                if (Build.VERSION.SDK_INT >= 24) {
                    openPDF(outputFile);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri apkUri = Uri.fromFile(outputFile);
                    intent.setDataAndType(apkUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }

            } else {
                Toast.makeText(getApplicationContext(), "다운로드 에러", Toast.LENGTH_LONG).show();
            }
        }
    }

    void openPDF(File file) {
        Uri fileUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileprovider",file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed();
    }
}