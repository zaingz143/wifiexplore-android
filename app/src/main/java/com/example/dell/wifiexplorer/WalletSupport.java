package com.example.dell.wifiexplorer;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.WalletConstants;

/**
 * The responsibility for this class is to interface with the Google Wallet object model.
 *
 * Created by rwaldura on 1/2/15.
 */
public class WalletSupport {

    public static final String MERCHANT_NAME = "WIFI Explore";

    public static final String CURRENCY_CODE_USD = "USD";

    // complete and utter magic, got it from the sample app
    public static final int REQUEST_CODE_MASKED_WALLET = 1001;
    public static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 1002;

    public static final int ENVIRONMENT = WalletConstants.ENVIRONMENT_SANDBOX;

    /**
     *
     * @param item
     * @return
     */
    public static MaskedWalletRequest createMaskedWalletRequest(ImpulseItem item) {
        Cart cart = buildCart(item);

        MaskedWalletRequest req = MaskedWalletRequest.newBuilder()
            .setMerchantName(MERCHANT_NAME)
            .setPhoneNumberRequired(false)
                .setMerchantTransactionId("muhammadhsna")
            .setShippingAddressRequired(true)
            .setCurrencyCode(CURRENCY_CODE_USD)
            //.setShouldRetrieveWalletObjects(true)
            .setCart(cart)
            .setEstimatedTotalPrice(cart.getTotalPrice())
            .build();

        Log.i(ImpulseStore.TAG, "built wallet request " + req);

        return req;
    }

    /**
     *
     * @param googleTransactionId
     * @return {@link com.google.android.gms.wallet.FullWalletRequest} instance
     */
    public static FullWalletRequest createFullWalletRequest(ImpulseItem item, String googleTransactionId) {
        Cart cart = buildCart(item);

        FullWalletRequest req = FullWalletRequest.newBuilder()
            .setGoogleTransactionId(googleTransactionId)
            .setCart(cart)
            .build();

        Log.i(ImpulseStore.TAG, "built wallet request " + req);

        return req;
    }

    private static Cart buildCart(ImpulseItem item) {
        Log.i(ImpulseStore.TAG, "building cart for item: " + item);

        int shipping = ImpulseStore.getShipping(item);
        int tax = ImpulseStore.getTax(item);
        int totalPrice = ImpulseStore.computeTotalPrice(item);

        return Cart.newBuilder()
            .setCurrencyCode(CURRENCY_CODE_USD)
            .setTotalPrice(ImpulseStore.toUSD(totalPrice))
            .addLineItem(LineItem.newBuilder()
                .setCurrencyCode(CURRENCY_CODE_USD)
                .setDescription(item.description)
                .setQuantity("1")
                .setUnitPrice(ImpulseStore.toUSD(item.price))
                .setTotalPrice(ImpulseStore.toUSD(item.price))
                .build())
            .addLineItem(LineItem.newBuilder()
                .setCurrencyCode(CURRENCY_CODE_USD)
                .setDescription(ImpulseStore.DESCRIPTION_LINE_ITEM_SHIPPING)
                .setRole(LineItem.Role.SHIPPING)
                .setTotalPrice(ImpulseStore.toUSD(shipping))
                .build())
            .addLineItem(LineItem.newBuilder()
                .setCurrencyCode(CURRENCY_CODE_USD)
                .setDescription(ImpulseStore.DESCRIPTION_LINE_ITEM_TAX)
                .setRole(LineItem.Role.TAX)
                .setTotalPrice(ImpulseStore.toUSD(tax))
                .build())
            .build();
    }


    static void handleError(Activity context, int code) {
        Log.e(ImpulseStore.TAG, "error code = " + code);

        switch (code) {
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                Toast.makeText(context, context.getString(R.string.spending_limit_exceeded, code), Toast.LENGTH_LONG).show();
                break;
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                // unrecoverable error
                String errorMessage = context.getString(R.string.google_wallet_unavailable) + "\n" + context.getString(R.string.error_code, code);
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static String getErrorMessage(int txStatus) {
        switch (txStatus) {
            case NotifyTransactionStatusRequest.Status.Error.AVS_DECLINE:
                return "AVS decline";
            case NotifyTransactionStatusRequest.Status.Error.BAD_CARD:
                return "Bad card";
            case NotifyTransactionStatusRequest.Status.Error.BAD_CVC:
                return "Bad CVC";
            case NotifyTransactionStatusRequest.Status.Error.DECLINED:
                return "Declined";
            case NotifyTransactionStatusRequest.Status.Error.FRAUD_DECLINE:
                return "Fraud decline";
            case NotifyTransactionStatusRequest.Status.Error.OTHER:
                return "Other reason";
            case NotifyTransactionStatusRequest.Status.Error.UNKNOWN:
                return "Unknown reason";
            default:
                return null;
        }
    }
}
