package co.creativev.bootcamp.got;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_CHARACTER = "character";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        GoTCharacter gotCharacter = getIntent().getParcelableExtra(EXTRA_CHARACTER);
        setTitle(gotCharacter.name);
        ((ImageView) findViewById(R.id.image_character)).setImageResource(gotCharacter.fullResId);
        ((ImageView) findViewById(R.id.image_house)).setImageResource(gotCharacter.houseResId);
        ((TextView) findViewById(R.id.text_house_name)).setText(gotCharacter.house);
        ((TextView) findViewById(R.id.text_character_story)).setText(gotCharacter.description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
