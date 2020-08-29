
package com.hendrysetiadi.reactnative.sunmiprinter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hendrysetiadi.reactnative.sunmiprinter.utils.ESCUtil;
import com.hendrysetiadi.reactnative.sunmiprinter.utils.BitmapUtil;

import woyou.aidlservice.jiuiv5.IWoyouService;

public class RNSunmiPrinterModule extends ReactContextBaseJavaModule {
    private static final String SERVICE_PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE_ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private final ReactApplicationContext reactContext;
    private IWoyouService woyouService;

    public RNSunmiPrinterModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.connectPrinterService();
    }

    @Override
    public String getName() {
        return "RNSunmiPrinter";
    }


    public void connectPrinterService() {
        Intent intent = new Intent();
        intent.setPackage(SERVICE_PACKAGE);
        intent.setAction(SERVICE_ACTION);
        this.reactContext.getApplicationContext().startService(intent);
        this.reactContext.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    public void disconnectPrinterService() {
        if (woyouService != null) {
            this.reactContext.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnectedToPrinter() {
        return woyouService != null;
    }


    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };



    @ReactMethod
    public void initPrinter(Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.printerInit(null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }



    /**
     * Print Paper Feed after finishing the Previous printing content
     * @param n         Number of Lines
     * @param promise
     */
    @ReactMethod
    public void printLineWrap(int n, Promise promise){
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.lineWrap(n, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }

    /**
     * Set the Printing Alignment
     * Setting the printing alignment will affect subsequent printing unless it is initialized
     * @param alignment     Alignment Mode: 0-Left, 1-Center, 2-Right
     * @param promise
     */
    @ReactMethod
    public void setAlignment(int alignment, Promise promise){
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.setAlignment(alignment, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }

    /**
     * Set the Printing Font Size
     * Setting the font size will affect subsequent printing unless it is initialized
     * Adjusting the font size will affect the character width and the number of characters per line
     * @param fontSize
     * @param promise
     */
    @ReactMethod
    public void setFontSize(float fontSize, Promise promise){
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.setFontSize(fontSize, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }


    /**
     * Print the Text
     * @param content   Text to Print
     * @param promise
     */
    @ReactMethod
    public void printText(String content, Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.printText(content, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }

    /**
     * Print the Text with Bold & Underline Options
     * @param content       Text to Print
     * @param isBold
     * @param isUnderline
     * @param promise
     */
    @ReactMethod
    public void printText(String content, boolean isBold, boolean isUnderline, Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            if (isBold) woyouService.sendRAWData(ESCUtil.boldOn(), null);
            if (isUnderline) woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);

            woyouService.printText(content, null);
            woyouService.sendRAWData(ESCUtil.boldOff(), null);
            woyouService.sendRAWData(ESCUtil.underlineOff(), null);

            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }


    /**
     * Print QR Code (Two-Dimensional Barcode)
     * @param data          QR Code content data
     * @param moduleSize    QR Code block size in Point (1 - 16)
     * @param errorLevel    QR Code error correction Level (0 - 3)
     *                          0 - Error correction level L (7%)
     *                          1 - Error correction level M (15%)
     *                          2 - Error correction level Q (25%)
     *                          3 - Error correction level H (30%)
     * @param promise
     */
    @ReactMethod
    public void printQrCode(String data, int moduleSize, int errorLevel, Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.printQRCode(data, moduleSize, errorLevel, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }

    /**
     * Print One-Dimensional Barcode
     * @param data          Barcode data
     * @param symbology     Barcode Type
     *                          0 - UPC-A
     *                          1 - UPC-E
     *                          2 - JAN13(EAN13)
     *                          3 - JAN8(EAN8)
     *                          4 - CODE39
     *                          5 - ITF
     *                          6 - CODABAR
     *                          7 - CODE93
     *                          8 - CODE128
     * @param height        Barcode Height (1 - 255, default 162)
     * @param width         Barcode Width (2 - 6, default 2)
     * @param textPosition  Barcode Text Position
     *                          0 - No Text
     *                          1 - Text above the Barcode
     *                          2 - Text below the Barcode
     *                          3 - Text both above and below the Barcode
     * @param promise
     */
    @ReactMethod
    public void printBarCode(String data, int symbology, int height, int width, int textPosition, Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.printBarCode(data, symbology, height, width, textPosition, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }


    /**
     * Print Bitmap from Base-64 data
     * @param data      Base-64 bitmap data (Maximum width of 384 pixels)
     * @param width
     * @param height
     * @param promise
     */
    @ReactMethod
    public void printBitmap(String data, int width, int height, Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            final IWoyouService ss = woyouService;
            byte[] decodedData = Base64.decode(data, Base64.DEFAULT);
            final Bitmap bitmap = BitmapUtil.decodeBitmap(decodedData, width, height);
            woyouService.printBitmap(bitmap, null);
            promise.resolve(null);
        } catch (Exception e) {
            promise.reject("0", e.getMessage());
        }
    }


    /**
     * Print using Original Instructions
     * @param data      Command byte data
     * @param promise
     */
    @ReactMethod
    public void printRawData(byte[] data, Promise promise) {
        if (woyouService == null) {
            Toast.makeText(this.reactContext, R.string.printer_disconnect, Toast.LENGTH_LONG).show();
            promise.reject("0", "");
        }

        try {
            woyouService.sendRAWData(data, null);
            promise.resolve(null);
        } catch (RemoteException e) {
            promise.reject("0", e.getMessage());
        }
    }
}