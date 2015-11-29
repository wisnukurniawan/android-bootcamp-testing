package co.creativev.bootcamp.got;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "GOT_APP";
    private GoTAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = (ListView) findViewById(R.id.list);
        adapter = new GoTAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_CHARACTER, DatabaseHelper.GOT_CHARACTERS[position % DatabaseHelper.GOT_CHARACTERS.length]);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.onStop();
    }

    public static class GoTAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        private final DatabaseHelper databaseHelper;
        private Cursor cursor;

        public GoTAdapter(Context context) {
            databaseHelper = DatabaseHelper.getDatabaseHelper(context);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        @Override
        public GoTCharacter getItem(int position) {
            if (cursor == null) return null;
            cursor.moveToPosition(position);
            return new GoTCharacter(
                    cursor.getString(cursor.getColumnIndexOrThrow(GoTCharacter.NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(GoTCharacter.RES_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(GoTCharacter.FULL_RES_ID)),
                    true,
                    cursor.getString(cursor.getColumnIndexOrThrow(GoTCharacter.HOUSE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(GoTCharacter.HOUSE_RES_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(GoTCharacter.DESCRIPTION))
            );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_got, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.text);
                ImageView image = (ImageView) convertView.findViewById(R.id.image_character);
                convertView.setTag(new GotViewHolder(name, image));
            }
            GoTCharacter item = getItem(position);
            GotViewHolder tag = (GotViewHolder) convertView.getTag();
            tag.name.setText(item.name);
            tag.image.setImageResource(item.resId);
            return convertView;
        }

        public void onStart() {
            Log.d(LOG_TAG, "Adapter onStart");
            closeCursorIfOpen();
            cursor = databaseHelper.getCharacterCursor();
            notifyDataSetChanged();
        }

        public void onStop() {
            Log.d(LOG_TAG, "Adapter onStop");
            closeCursorIfOpen();
        }

        private void closeCursorIfOpen() {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
    }

    public static class GotViewHolder {
        private final TextView name;
        private final ImageView image;

        public GotViewHolder(TextView name, ImageView image) {
            this.name = name;
            this.image = image;
        }
    }
}
