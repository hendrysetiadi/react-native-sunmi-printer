import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  getPrinterSerialNo(): Promise<string>;
  getPrinterModel(): Promise<string>;
  getPrinterVersion(): Promise<string>;
  getPrinterPaper(): Promise<string>;

  initPrinter(): Promise<null>;

  printLineWrap(n: number): Promise<null>;
  feedPaper(): Promise<null>;
  setAlignment(alignment: number): Promise<null>;
  setFontSize(fontSize: number): Promise<null>;

  printText(content: string): Promise<null>;
  printTextWithOption(
    content: string,
    fontSize: number,
    isBold: boolean,
    isUnderline: boolean
  ): Promise<null>;
  printTextTable(
    contentArray: string[],
    widthArray: number[],
    alignmentArray: number[]
  ): Promise<null>;

  printBarcode(
    data: string,
    symbology: number,
    height: number,
    width: number,
    textPosition: number
  ): Promise<null>;
  printQrCode(
    data: string,
    moduleSize: number,
    errorLevel: number
  ): Promise<null>;

  printBitmap(data: string, width: number, height: number): Promise<null>;
  openCashBox(): Promise<null>;

  enterPrintBuffer(clean: boolean): Promise<null>;
  commitPrinterBuffer(): Promise<null>;
  exitPrinterBuffer(commit: boolean): Promise<null>;

  showPrinterStatus(): Promise<null>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('SunmiPrinter');
