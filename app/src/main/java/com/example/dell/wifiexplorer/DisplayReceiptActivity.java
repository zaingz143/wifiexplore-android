package com.example.dell.wifiexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.InstrumentInfo;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.WalletConstants;

/**
 * This activity does something, but I'm not sure what.
 * This activity is launched from FullWalletConfirmationButtonFragment.fetchTransactionStatus().
 */
public class DisplayReceiptActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        // retrieve the wallet
        FullWallet wallet = getIntent().getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
        ImpulseItem item = (ImpulseItem) getIntent().getSerializableExtra(ImpulseStore.EXTRA_ITEM);
        int txStatus = getIntent().getIntExtra(ImpulseStore.EXTRA_TX_STATUS, -1);

        TextView textLabel = (TextView) findViewById(R.id.order_completed);

        if (txStatus != NotifyTransactionStatusRequest.Status.SUCCESS)
            textLabel.setText(String.format("Failed to process transaction; error %s", WalletSupport.getErrorMessage(txStatus)));
        else {
            StringBuilder text = new StringBuilder( String.format(
                    textLabel.getText().toString(),
                    item.description,
                    formatAddress(wallet.getBuyerShippingAddress()),
                    wallet.getEmail(),
                    wallet.getGoogleTransactionId()));

            text.append("\nPayment descriptions");
            for (String descr : wallet.getPaymentDescriptions()) {
                text.append("\n* ").append(descr);
            }

            text.append("\nInstrument infos (secret!)");
            for (InstrumentInfo ii : wallet.getInstrumentInfos()) {
                text.append("\n* type: ").append(ii.getInstrumentType())
                        .append(" details: ").append(ii.getInstrumentDetails());
            }

            textLabel.setText(text);
        }

        Button b = (Button) findViewById(R.id.button_again);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayReceiptActivity.this, SelectItemActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private String formatAddress(UserAddress address) {
        StringBuilder sb = new StringBuilder();
        sb.append(address.getName()).append("\n")
                .append(address.getAddress1()).append("\n")
                .append(address.getPostalCode()).append(' ')
                .append(address.getLocality());
        return sb.toString();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completed_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
