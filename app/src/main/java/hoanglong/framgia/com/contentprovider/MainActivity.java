package hoanglong.framgia.com.contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    private ArrayList<String> mListMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(getApplicationContext()
                , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this
                        , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this
                        , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        } else {
            doStuff();
        }
    }

    public void getMusic() {
        ContentResolver mResolver = getContentResolver();
        Uri mSongUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor mCursor = mResolver.query(mSongUri, null, null, null, null);
        if (mCursor != null && mCursor.moveToFirst()) {
            int mSongTitle = mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int mSongArtist = mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            do {
                String mCurrentTitle = mCursor.getString(mSongTitle);
                String mCurrentArtist = mCursor.getString(mSongArtist);
                StringBuilder mStringBuilder = new StringBuilder();
                mStringBuilder.append(mCurrentTitle).append("\t").append(mCurrentArtist);
                mListMusic.add(mStringBuilder.toString());
            } while (mCursor.moveToNext());
        }
    }

    private void doStuff() {
        ListView mListView = findViewById(R.id.list_view_music);
        mListMusic = new ArrayList<>();
        getMusic();
        ArrayAdapter<String> mAdapterMusic = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListMusic);
        mListView.setAdapter(mAdapterMusic);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this
                            , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
