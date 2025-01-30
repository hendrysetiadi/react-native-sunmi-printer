# react-native-sunmi-printer

React Native Library for SUNMI Inner Printer (Android only)

## Installation

```sh
npm install @hendrysetiadi/react-native-sunmi-printer
```

## Usage

```js
import SunmiPrinter from '@hendrysetiadi/react-native-sunmi-printer';

// or ES6+ destructured imports

import { initPrinter } from '@hendrysetiadi/react-native-sunmi-printer';
```

## API

### getPrinterSerialNo()

> getPrinterSerialNo(): Promise&lt;string&gt;

#### Examples

```js
const serialNo = await SunmiPrinter.getPrinterSerialNo();
```

---

### getPrinterModel()

> getPrinterModel(): Promise&lt;string&gt;

#### Examples

```js
const printerModel = await SunmiPrinter.getPrinterModel();
```

---

### getPrinterVersion()

> getPrinterVersion(): Promise&lt;string&gt;

#### Examples

```js
const printerVersion = await SunmiPrinter.getPrinterVersion();
```

---

### getPrinterPaper()

> getPrinterPaper(): Promise&lt;string&gt;

#### Examples

```js
const printerPaper = await SunmiPrinter.getPrinterPaper();
```

---

### initPrinter()

> initPrinter(): Promise&lt;null&gt;

Initialize printer and reset printer state (font size, alignment, etc.), should be called first before the print call

#### Examples

```js
await SunmiPrinter.getPrinterPaper();
```

---

### printLineWrap()

> printLineWrap(n: number) : Promise&lt;null&gt;

Print Paper Feed after finishing the Previous printing content

#### Parameters

- n : Number of Lines

#### Examples

```js
await SunmiPrinter.printLineWrap(3);
```

---

### feedPaper()

> feedPaper() : Promise&lt;null&gt;

Feed the paper automatically according to the distance between the paper hatch and the print head. If the device does not support, then replaced by printing three lines

#### Examples

```js
await SunmiPrinter.feedPaper();
```

---

### setAlignment()

> setAlignment(alignment: number) : Promise&lt;null&gt;

Set the Printing Alignment (Setting the printing alignment will affect subsequent printing unless it is initialized)

#### Parameters

- alignment : Alignment Mode (0 = Left, 1 = Center, 2 = Right)

#### Examples

```js
// Set Alignment to Center
await SunmiPrinter.setAlignment(1);
```

---

### setFontSize()

> setFontSize(fontSize: number) : Promise&lt;null&gt;

Set the Printing Font Size (Setting the font size will affect subsequent printing unless it is initialized). Adjusting the font size will affect the character width and the number of characters per line

#### Examples

```js
await SunmiPrinter.setFontSize(32);
```

---

### printText()

> printText(content: string) : Promise&lt;null&gt;

#### Parameters

- content : Text to Print

#### Examples

```js
await SunmiPrinter.printText('Hello World');
```

---

### printTextWithOption()

> printTextWithOption(content: string, fontSize: number, isBold: boolean, isUnderline: boolean) : Promise&lt;null&gt;

Print Text with Size, Bold, and Underline options

#### Parameters

- content : Text to Print
- fontSize
- isBold
- isUnderline

#### Examples

```js
await SunmiPrinter.printTextWithOption('Hello World', 24, true, true);
```

---

### printTextTable()

> printTextTable(contentArray: string[], widthArray: number[], alignmentArray: number[]) : Promise&lt;null&gt;

Print Text in a form of Table (can specify the column Width and Alignment)

#### Parameters

- contentArray : Array of Text for each Column
- widthArray : Array of Width ratio for each Column
- alignmentArray : Array of Alignment for each Column (0 = Left, 1 = Center, 2 = Right)

#### Examples

```js
await SunmiPrinter.printTextTable(
  ['Date', 'Jan 1st, 10:00 AM'],
  [1, 1],
  [0, 2]
);
```

---

### printBarcode()

> printBarcode(data: string, symbology: number, height: number, width: number, textPosition: number) : Promise&lt;null&gt;

Print One-Dimensional Barcode

#### Parameters

- data : Barcode data
- symbology : Barcode Type
  - 0 = UPC-A
  - 1 = UPC-E
  - 2 = JAN13(EAN13)
  - 3 = JAN8(EAN8)
  - 4 = CODE39
  - 5 = ITF
  - 6 = CODABAR
  - 7 = CODE93
  - 8 = CODE128
- height : Barcode Height (1 - 255)
- width : Barcode Width (2 - 6)
- textPosition : Barcode Text Position
  - 0 = No Text
  - 1 = Text above the Barcode
  - 2 = Text below the Barcode
  - 3 = Text both above and below the Barcode

#### Examples

```js
await SunmiPrinter.printBarcode('01234567890', 8, 80, 2, 0);
```

---

### printQrCode()

> printQrCode(data: string, moduleSize: number, errorLevel: number) : Promise&lt;null&gt;

Print QR Code (Two-Dimensional Barcode)

#### Parameters

- data : QR Code content data
- moduleSize : QR Code block size in Point (1 - 16)
- errorLevel : QR Code error correction Level (0 - 3)
  - 0 = Error correction level L (7%)
  - 1 = Error correction level M (15%)
  - 2 = Error correction level Q (25%)
  - 3 = Error correction level H (30%)

#### Examples

```js
await SunmiPrinter.printQrCode('https://google.com', 8, 2);
```

---

### printBitmap()

> printBitmap(data: string, width: number, height: number) : Promise&lt;null&gt;

Print Bitmap from Base-64 data

#### Parameters

- data : Base-64 bitmap data (Maximum width of 384 pixels)
- width
- height

#### Examples

```js
const base64Image = '/9j/2wCEAAEBAQEBAQEBA...BKJzETKAj2KAAH//2Q==';
await SunmiPrinter.printBitmap(base64Image, 320, 80);
```

#### Notes

> If you want to print local image from device storage, you can use library like [rn-fetch-blob](https://github.com/joltup/rn-fetch-blob) or [react-native-fs](https://github.com/itinance/react-native-fs) to convert the image to base64

---

### openCashBox()

> openCashBox() : Promise&lt;null&gt;

Open the Cash Box. If there is no Cash Box Interface (such as V1, P1) or the call fails, an exception will be thrown

#### Examples

```js
await SunmiPrinter.openCashBox();
```

---

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
