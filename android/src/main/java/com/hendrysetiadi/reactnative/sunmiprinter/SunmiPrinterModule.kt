package com.hendrysetiadi.reactnative.sunmiprinter

import android.graphics.Bitmap
import android.os.RemoteException
import android.util.Base64
import android.widget.Toast
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.module.annotations.ReactModule
import com.hendrysetiadi.reactnative.sunmiprinter.utils.BitmapUtil
import com.hendrysetiadi.reactnative.sunmiprinter.utils.ESCUtil
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterException
import com.sunmi.peripheral.printer.InnerPrinterManager
import com.sunmi.peripheral.printer.SunmiPrinterService

@ReactModule(name = SunmiPrinterModule.NAME)
class SunmiPrinterModule(reactContext: ReactApplicationContext) :
  NativeSunmiPrinterSpec(reactContext) {
  companion object {
    const val NAME = "SunmiPrinter"
  }

  override fun getName(): String {
    return NAME
  }


  private val mReactContext: ReactApplicationContext = reactContext
  private var sunmiPrinterService: SunmiPrinterService? = null

  private val innerPrinterCallback: InnerPrinterCallback = object : InnerPrinterCallback() {
    override fun onConnected(service: SunmiPrinterService) {
      sunmiPrinterService = service
    }

    override fun onDisconnected() {
      sunmiPrinterService = null
    }
  }

  private fun connectSunmiPrinterService() {
    try {
      InnerPrinterManager.getInstance().bindService(mReactContext, innerPrinterCallback)
    } catch (e: InnerPrinterException) {
      e.printStackTrace()
      Toast.makeText(mReactContext, e.message, Toast.LENGTH_LONG).show();
    }
  }

  private fun disconnectSunmiPrinterService() {
    try {
      if (sunmiPrinterService != null) {
        InnerPrinterManager.getInstance().unBindService(mReactContext, innerPrinterCallback)
        sunmiPrinterService = null
      }
    } catch (e: InnerPrinterException) {
      e.printStackTrace()
      Toast.makeText(mReactContext, e.message, Toast.LENGTH_LONG).show();
    }
  }


  init {
    connectSunmiPrinterService()
  }



  override fun initPrinter(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.printerInit(null /* object : InnerResultCallback() {
        override fun onRunResult(isSuccess: Boolean) {
          if (isSuccess) {
            promise.resolve(null)
          } else {
            promise.reject("0", isSuccess.toString() + "")
          }
        }

        override fun onReturnString(result: String?) {
          promise.resolve(result)
        }

        override fun onRaiseException(code: Int, msg: String?) {
          promise.reject("" + code, msg)
        }

        override fun onPrintResult(code: Int, msg: String?) {
          TODO("Not yet implemented")
        }
      } */)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  override fun getPrinterSerialNo(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      promise.resolve(sunmiPrinterService!!.printerSerialNo)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  override fun getPrinterModel(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      promise.resolve(sunmiPrinterService!!.printerModal)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  override fun getPrinterVersion(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      promise.resolve(sunmiPrinterService!!.printerVersion)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  override fun getPrinterPaper(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      promise.resolve(if (sunmiPrinterService!!.printerPaper == 1) "58mm" else "80mm")
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Print Paper Feed after finishing the Previous printing content
   * @param n         Number of Lines
   * @param promise
   */
  override fun printLineWrap(n: Double, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.lineWrap(n.toInt(), null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Feed the paper automatically according to the distance between the paper hatch and the print head
   * If the device does not support, then replaced by printing three lines
   * @param promise
   */
  override fun feedPaper(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.autoOutPaper(null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      printLineWrap(3.0, promise)
    }
  }

  /**
   * Set the Printing Alignment
   * Setting the printing alignment will affect subsequent printing unless it is initialized
   * @param alignment     Alignment Mode: 0 = Left, 1 = Center, 2 = Right
   * @param promise
   */
  override fun setAlignment(alignment: Double, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.setAlignment(alignment.toInt(), null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Set the Printing Font Size
   * Setting the font size will affect subsequent printing unless it is initialized
   * Adjusting the font size will affect the character width and the number of characters per line
   * @param fontSize
   * @param promise
   */
  override fun setFontSize(fontSize: Double, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.setFontSize(fontSize.toFloat(), null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Print Text
   * @param content   Text to Print
   * @param promise
   */
  override fun printText(content: String, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.printText(content, null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Print Text with Size, Bold, and Underline options
   * @param content       Text to Print
   * @param fontSize
   * @param isBold
   * @param isUnderline
   * @param promise
   */
  override fun printTextWithOption(
    content: String,
    fontSize: Double,
    isBold: Boolean,
    isUnderline: Boolean,
    promise: Promise
  ) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      if (isBold) sunmiPrinterService!!.sendRAWData(ESCUtil.boldOn(), null)
      if (isUnderline) sunmiPrinterService!!.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null)

      sunmiPrinterService!!.printTextWithFont(content, null, fontSize.toFloat(), null)

      sunmiPrinterService!!.sendRAWData(ESCUtil.boldOff(), null)
      sunmiPrinterService!!.sendRAWData(ESCUtil.underlineOff(), null)

      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Print Text in a form of Table (can specify the column Width and Alignment)
   * @param contentArray    Array of Text for each Column
   * @param widthArray      Array of Width ratio for each Column
   * @param alignmentArray  Array of Alignment for each Column (0 = Left, 1 = Center, 2 = Right)
   * @param promise
   */
  override fun printTextTable(
    contentArray: ReadableArray,
    widthArray: ReadableArray,
    alignmentArray: ReadableArray,
    promise: Promise
  ) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      val colsTextArr = arrayOfNulls<String>(contentArray.size())
      for (i in 0 until contentArray.size()) {
        colsTextArr[i] = contentArray.getString(i)
      }

      val colsWidthArr = IntArray(widthArray.size())
      for (i in 0 until widthArray.size()) {
        colsWidthArr[i] = widthArray.getInt(i)
      }

      val colsAlign = IntArray(alignmentArray.size())
      for (i in 0 until alignmentArray.size()) {
        colsAlign[i] = alignmentArray.getInt(i)
      }

      sunmiPrinterService!!.printColumnsString(colsTextArr, colsWidthArr, colsAlign, null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Print One-Dimensional Barcode
   * @param data          Barcode data
   * @param symbology     Barcode Type
   *                          0 = UPC-A
   *                          1 = UPC-E
   *                          2 = JAN13(EAN13)
   *                          3 = JAN8(EAN8)
   *                          4 = CODE39
   *                          5 = ITF
   *                          6 = CODABAR
   *                          7 = CODE93
   *                          8 = CODE128
   * @param height        Barcode Height (1 - 255, default 162)
   * @param width         Barcode Width (2 - 6, default 2)
   * @param textPosition  Barcode Text Position
   *                          0 = No Text
   *                          1 = Text above the Barcode
   *                          2 = Text below the Barcode
   *                          3 = Text both above and below the Barcode
   * @param promise
   */
  override fun printBarcode(
    data: String,
    symbology: Double,
    height: Double,
    width: Double,
    textPosition: Double,
    promise: Promise
  ) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.printBarCode(data, symbology.toInt(), height.toInt(), width.toInt(),
        textPosition.toInt(), null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Print QR Code (Two-Dimensional Barcode)
   * @param data          QR Code content data
   * @param moduleSize    QR Code block size in Point (1 - 16)
   * @param errorLevel    QR Code error correction Level (0 - 3)
   *                          0 = Error correction level L (7%)
   *                          1 = Error correction level M (15%)
   *                          2 = Error correction level Q (25%)
   *                          3 = Error correction level H (30%)
   * @param promise
   */
  override fun printQrCode(
    data: String,
    moduleSize: Double,
    errorLevel: Double,
    promise: Promise
  ) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.printQRCode(data, moduleSize.toInt(), errorLevel.toInt(), null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Print Bitmap from Base-64 data
   * @param data      Base-64 bitmap data (Maximum width of 384 pixels)
   * @param width     Width must be less than 384 pixels and an integer multiple of 8
   * @param height
   * @param promise
   */
  override fun printBitmap(data: String, width: Double, height: Double, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      val decodedData: ByteArray = Base64.decode(data, Base64.DEFAULT)
      val bitmap: Bitmap = BitmapUtil.decodeBitmap(decodedData, width.toInt(), height.toInt())
      sunmiPrinterService!!.printBitmap(bitmap, null)
      promise.resolve(null)
    } catch (e: Exception) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Open Cash Box
   * If there is no Cash Box Interface (such as V1, P1) or the call fails, an exception will be thrown
   * @param promise
   */
  override fun openCashBox(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.openDrawer(null)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Enter the Buffer mode, all print call will be cached and will be printed after calling commitPrinterBuffer()
   * @param clean       Clean the Buffer content
   * @param promise
   */
  override fun enterPrintBuffer(clean: Boolean, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.enterPrinterBuffer(clean)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Commit Buffer to print all cached print call
   * @param promise
   */
  override fun commitPrinterBuffer(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.commitPrinterBuffer()
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }

  /**
   * Exit Buffer Mode
   * @param commit      Commit to print all cached print call before exit
   * @param promise
   */
  override fun exitPrinterBuffer(commit: Boolean, promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      sunmiPrinterService!!.exitPrinterBuffer(commit)
      promise.resolve(null)
    } catch (e: RemoteException) {
      promise.reject("0", e.message)
    }
  }


  /**
   * Show the status of the printer
   * @param promise
   */
  override fun showPrinterStatus(promise: Promise) {
    if (sunmiPrinterService == null) promise.reject("0", "Printer Service is not Connected")
    try {
      var status = ""
      val printerState = sunmiPrinterService!!.updatePrinterState()
      when (printerState) {
        1 -> status = "Printer is running"
        2 -> status = "Printer found but still initializing"
        3 -> status = "Printer hardware interface is abnormal and needs to be reprinted"
        4 -> status = "Printer is out of paper"
        5 -> status = "Printer is overheating"
        6 -> status = "Printer's cover is not closed"
        7 -> status = "Printer's cutter is abnormal"
        8 -> status = "Printer's cutter is normal"
        9 -> status = "Black Mark Paper is not found"
        505 -> status = "Printer does not exist"
        else -> {}
      }

      Toast.makeText(mReactContext, status, Toast.LENGTH_LONG).show()
      promise.resolve(null)
    } catch (e: RemoteException) {
      e.printStackTrace()
      promise.reject("0", e.message)
    }
  }
}
