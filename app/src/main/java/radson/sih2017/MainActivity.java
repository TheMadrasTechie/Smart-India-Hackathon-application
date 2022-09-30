package radson.sih2017;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn_std = (Button)findViewById(R.id.btn_std);
        final Button btn_cmp = (Button)findViewById(R.id.btn_cmp);
        btn_std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this,Std_login_Activity.class);
                startActivity(a);
            }
        });
        btn_cmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this,Cmp_login_Activity.class);
                startActivity(a);
            }
        });
    }
}
