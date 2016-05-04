package me.argha.tonu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.argha.tonu.R;

public class ReportActivity extends AppCompatActivity implements OnClickListener{
    @Bind(R.id.report_name_textview)TextView reportNameTv;
    @Bind(R.id.report_name_edittext)EditText reportNameEt;
    @Bind(R.id.report_description_textview)TextView reportDescTv;
    @Bind(R.id.report_description_edittext)EditText reportDescEt;
    @Bind(R.id.report_voice_btn)TextView reportVoiceBtn;
    @Bind(R.id.report_submit_btn)TextView reportSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        reportSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        if(id==R.id.report_submit_btn){
            submitReport();
        }else if(id==R.id.report_voice_btn){

        }
    }

    private void submitReport() {
        String name, desc, nationalId;
        name=reportNameEt.getText().toString();
        desc=reportDescEt.getText().toString();
        Toast.makeText(ReportActivity.this, "Your report has been successfully submitted and will" +
                " be reviewed", Toast
                .LENGTH_LONG).show();
        reportNameEt.setText("");
        reportDescEt.setText("");
    }
}
