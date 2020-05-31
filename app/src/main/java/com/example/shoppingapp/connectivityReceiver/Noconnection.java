package com.example.shoppingapp.connectivityReceiver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.shoppingapp.config.Config.deleteCache;

public class Noconnection extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = Noconnection.class.getSimpleName();

    @BindView(R.id.retryButton)
    ImageButton buttonRetryConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        ButterKnife.bind(this);

        checkConnection();




        //  buttonRetryConnection.setOnClickListener(v -> checkConnection());
    }
    @OnClick(R.id.retryButton)
    void checkConnection()
    {
        Log.d(TAG, "checkConnection: ");
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        if (isConnected) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }




    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are You Sure To?")
                .setCancelable(false)
                .setPositiveButton("Exit", (dialog, id) -> {
                    finishAffinity();
                    deleteCache(Noconnection.this);

                })
                .setNegativeButton("Retry", (dialog, id) -> checkConnection());
        AlertDialog alert = builder.create();
        alert.show();

    }
}
