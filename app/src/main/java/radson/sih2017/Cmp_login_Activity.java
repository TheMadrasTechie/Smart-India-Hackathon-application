package radson.sih2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by SUNDAR on 02-04-2017.
 */
public class Cmp_login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.std_login_acty);
        final Button btn_reg = (Button)findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Cmp_login_Activity.this , Cmp_reg_Activity.class);
                startActivity(a);
            }
        });
    }
    public void insert(View view){

        Intent a = new Intent(Cmp_login_Activity.this,Std_Activity.class);
        startActivity(a);
    }
}
