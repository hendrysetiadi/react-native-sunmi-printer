import SunmiPrinter from './NativeSunmiPrinter';

export async function getPrinterSerialNo(): Promise<string> {
  return SunmiPrinter.getPrinterSerialNo();
}
export async function getPrinterModel(): Promise<string> {
  return SunmiPrinter.getPrinterModel();
}
export async function getPrinterVersion(): Promise<string> {
  return SunmiPrinter.getPrinterVersion();
}
export async function getPrinterPaper(): Promise<string> {
  return SunmiPrinter.getPrinterPaper();
}

export async function initPrinter(): Promise<null> {
  return SunmiPrinter.initPrinter();
}

/**
 * Print Paper Feed after finishing the Previous printing content
 * @param n         Number of Lines
 */
export async function printLineWrap(n: number): Promise<null> {
  return SunmiPrinter.printLineWrap(n);
}
/**
 * Feed the paper automatically according to the distance between the paper hatch and the print head
 * If the device does not support, then replaced by printing three lines
 */
export async function feedPaper(): Promise<null> {
  return SunmiPrinter.feedPaper();
}
/**
 * Set the Printing Alignment
 * Setting the printing alignment will affect subsequent printing unless it is initialized
 * @param alignment     Alignment Mode: 0 = Left, 1 = Center, 2 = Right
 */
export async function setAlignment(alignment: number): Promise<null> {
  return SunmiPrinter.setAlignment(alignment);
}
/**
 * Set the Printing Font Size
 * Setting the font size will affect subsequent printing unless it is initialized
 * Adjusting the font size will affect the character width and the number of characters per line
 * @param fontSize
 */
export async function setFontSize(fontSize: number): Promise<null> {
  return SunmiPrinter.setFontSize(fontSize);
}

/**
 * Print Text
 * @param content   Text to Print
 */
export async function printText(content: string): Promise<null> {
  return SunmiPrinter.printText(content);
}
/**
 * Print Text with Size, Bold, and Underline options
 * @param content       Text to Print
 * @param fontSize
 * @param isBold
 * @param isUnderline
 */
export async function printTextWithOption(
  content: string,
  fontSize: number,
  isBold: boolean,
  isUnderline: boolean
): Promise<null> {
  return SunmiPrinter.printTextWithOption(
    content,
    fontSize,
    isBold,
    isUnderline
  );
}
/**
 * Print Text in a form of Table (can specify the column Width and Alignment)
 * @param contentArray    Array of Text for each Column
 * @param widthArray      Array of Width ratio for each Column
 * @param alignmentArray  Array of Alignment for each Column (0 = Left, 1 = Center, 2 = Right)
 */
export async function printTextTable(
  contentArray: string[],
  widthArray: number[],
  alignmentArray: number[]
): Promise<null> {
  return SunmiPrinter.printTextTable(contentArray, widthArray, alignmentArray);
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
 */
export async function printBarcode(
  data: string,
  symbology: number,
  height: number,
  width: number,
  textPosition: number
): Promise<null> {
  return SunmiPrinter.printBarcode(
    data,
    symbology,
    height,
    width,
    textPosition
  );
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
 */
export async function printQrCode(
  data: string,
  moduleSize: number,
  errorLevel: number
): Promise<null> {
  return SunmiPrinter.printQrCode(data, moduleSize, errorLevel);
}

/**
 * Print Bitmap from Base-64 data
 * @param data      Base-64 bitmap data (Maximum width of 384 pixels)
 * @param width
 * @param height
 */
export async function printBitmap(
  data: string,
  width: number,
  height: number
): Promise<null> {
  return SunmiPrinter.printBitmap(data, width, height);
}
/**
 * Open Cash Box
 * If there is no Cash Box Interface (such as V1, P1) or the call fails, an exception will be thrown
 */
export async function openCashBox(): Promise<null> {
  return SunmiPrinter.openCashBox();
}

/**
 * Enter the Buffer mode, all print call will be cached and will be printed after calling commitPrinterBuffer()
 * @param clean       Clean the Buffer content
 */
export async function enterPrintBuffer(clean: boolean): Promise<null> {
  return SunmiPrinter.enterPrintBuffer(clean);
}
/**
 * Commit Buffer to print all cached print call
 */
export async function commitPrinterBuffer(): Promise<null> {
  return SunmiPrinter.commitPrinterBuffer();
}
/**
 * Exit Buffer Mode
 * @param commit      Commit to print all cached print call before exit
 */
export async function exitPrinterBuffer(commit: boolean): Promise<null> {
  return SunmiPrinter.exitPrinterBuffer(commit);
}

/**
 * Show the status of the printer
 */
export async function showPrinterStatus(): Promise<null> {
  return SunmiPrinter.showPrinterStatus();
}

//

export default {
  getPrinterSerialNo,
  getPrinterModel,
  getPrinterVersion,
  getPrinterPaper,

  initPrinter,

  printLineWrap,
  feedPaper,
  setAlignment,
  setFontSize,

  printText,
  printTextWithOption,
  printTextTable,

  printBarcode,
  printQrCode,

  printBitmap,
  openCashBox,

  enterPrintBuffer,
  commitPrinterBuffer,
  exitPrinterBuffer,
  showPrinterStatus,
};
