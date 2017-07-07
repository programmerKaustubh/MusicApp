package com.kmema.musicapp.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmema.musicapp.R;
import com.kmema.musicapp.database.FavoriteDatabase;
import com.kmema.musicapp.database.SonglistDBHelper;
import com.kmema.musicapp.helper.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AcadGildMentor on 6/3/2015.
 */
public class SongListAdapter extends BaseAdapter {

    public String listNameTobeDelete;
    private Context mContext;
    private ArrayList<Song> songList;//Data Source for ListView
    public Button deleteBtn;
    private SQLiteDatabase mDb;

    public SongListAdapter(Context context, ArrayList<Song> list) {
        mContext = context;
        this.songList = list;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Song getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            //Layout inflate for list item
               convertView = LayoutInflater.from(mContext).inflate(R.layout.song_list_item, null);
        }

        deleteBtn=(Button)convertView.findViewById(R.id.buttonDelete);
        ImageView mImgSong = (ImageView) convertView.findViewById(R.id.img_listitem_file);
        TextView mtxtSongName = (TextView) convertView.findViewById(R.id.txt_listitem_filename);
        TextView mTxtSongAlbumName = (TextView) convertView.findViewById(R.id.txt_listitem_albumname);
        TextView mTxtSongDuration = (TextView) convertView.findViewById(R.id.txt_listitem_duration);

        mtxtSongName.setText(songList.get(position).getSongName());
        mTxtSongAlbumName.setText(songList.get(position).getSongAlbumName());
        mTxtSongDuration.setText(songList.get(position).getSongDuration());

        if((songList.get(position).getAlbumArt()) != null)
        {
            Drawable img = Drawable.createFromPath(songList.get(position).getAlbumArt());
            mImgSong.setImageDrawable(img);
        }
        else
        {
            mImgSong.setImageResource(R.drawable.no_clipart);
        }

       deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SonglistDBHelper songDBhelper = new SonglistDBHelper(mContext);
                mDb = songDBhelper.getWritableDatabase();
                mDb.beginTransaction();
                    removeAlbumList(listNameTobeDelete,songList.get(position).getSongName());
                mDb.endTransaction();
                mDb.close();
                songList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(mContext, "You Are Clicking", Toast.LENGTH_SHORT).show();
            }
        });
       return convertView;
    }

    public void setSongsList(ArrayList<Song> list) {
        songList = list;
        this.notifyDataSetChanged();
    }


    public int removeAlbumList(String albumNamefromToDeleted, String songNameTodelete)
    {
        try {

            return mDb.delete(FavoriteDatabase.connectionTableSong.TABLE_NAME_TAG,
                    FavoriteDatabase.connectionTableSong.COLUMN_LIST_NAME +" = ? AND "+
                            FavoriteDatabase.connectionTableSong.COLUMN_SONG_NAME +" = ?",new String[]{albumNamefromToDeleted,songNameTodelete});
        }
        catch (Exception e)
        {
            Toast.makeText(mContext, "Could Not Delete", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }


    public void SetListName(String listname)
    {
        this.listNameTobeDelete = listname;
    }
}
