import {
  StyleSheet,
  SafeAreaView,
  StatusBar,
  ScrollView,
  View,
  Text,
  Button,
  useColorScheme,
  Alert,
  type StyleProp,
  type ViewStyle,
} from 'react-native';
import { Colors } from 'react-native/Libraries/NewAppScreen';
import { useEffect, useState } from 'react';
import SunmiPrinter from '@hendrysetiadi/react-native-sunmi-printer';

export default function App() {
  const isDarkMode = useColorScheme() === 'dark';
  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };
  const innerContainerStyle: StyleProp<ViewStyle> = {
    ...backgroundStyle,
    padding: 30,
    minHeight: '100%',
  };

  const [printerSerialNo, setPrinterSerialNo] = useState<string>();
  const [printerModel, setPrinterModel] = useState<string>();
  const [printerVersion, setPrinterVersion] = useState<string>();
  const [printerPaper, setPrinterPaper] = useState<string>('58mm');

  useEffect(() => {
    setTimeout(getPrinterInformation, 5000);
  }, []);

  //

  const getPrinterInformation = async () => {
    try {
      const mPrinterSerialNo = await SunmiPrinter.getPrinterSerialNo();
      if (mPrinterSerialNo) setPrinterSerialNo(mPrinterSerialNo);
      const mPrinterModel = await SunmiPrinter.getPrinterModel();
      if (mPrinterModel) setPrinterModel(mPrinterModel);
      const mPrinterVersion = await SunmiPrinter.getPrinterVersion();
      if (mPrinterVersion) setPrinterVersion(mPrinterVersion);
      const mPrinterPaper = await SunmiPrinter.getPrinterPaper();
      if (mPrinterPaper) setPrinterPaper(mPrinterPaper);
    } catch (e) {
      if (e instanceof Error) Alert.alert('Print Error', e.message);
      else Alert.alert('Print Error', `${e}`);
    }
  };

  const printExampleReceipt = async () => {
    const separator =
      printerPaper === '58mm'
        ? '--------------------------------'
        : '------------------------------------------------';

    try {
      await SunmiPrinter.initPrinter();

      await SunmiPrinter.setAlignment(1);
      await SunmiPrinter.printBitmap(exampleBase64Image, 320, 80);
      await SunmiPrinter.printLineWrap(2);

      await SunmiPrinter.printTextWithOption(
        'EXAMPLE MARKET\n\n',
        32,
        true,
        true
      );
      await SunmiPrinter.printText('My Example Market Outlet Address\n');
      await SunmiPrinter.printText('City Index - 10101\n');
      await SunmiPrinter.printText('Phone: +99 9999 9999\n');
      await SunmiPrinter.printText(`${separator}\n`);

      await SunmiPrinter.printTextTable(['Order No', ': 0001'], [1, 2], [0, 0]);
      await SunmiPrinter.printTextTable(
        ['Date', ': Jan 1st, 10:00'],
        [1, 2],
        [0, 0]
      );
      await SunmiPrinter.printTextTable(
        ['Cashier', ': John Doe'],
        [1, 2],
        [0, 0]
      );
      await SunmiPrinter.printText(`${separator}\n`);

      await SunmiPrinter.printTextTable(
        ['Market Food', 'x2', '250.00'],
        [4, 1, 2],
        [0, 2, 2]
      );
      await SunmiPrinter.printTextTable(
        ['Market Snack', 'x3', '60.00'],
        [4, 1, 2],
        [0, 2, 2]
      );
      await SunmiPrinter.printTextTable(
        ['Market Beverage', 'x2', '50.00'],
        [4, 1, 2],
        [0, 2, 2]
      );
      await SunmiPrinter.printTextTable(
        ['Other Product from Market', 'x1', '200.00'],
        [4, 1, 2],
        [0, 2, 2]
      );
      await SunmiPrinter.printTextTable(
        ['Other Product from Market', 'x4', '40.00'],
        [4, 1, 2],
        [0, 2, 2]
      );
      await SunmiPrinter.printText(`${separator}\n`);

      await SunmiPrinter.setFontSize(32);
      await SunmiPrinter.printTextTable(
        ['Sub Total', '600.00'],
        [1, 1],
        [0, 2]
      );

      await SunmiPrinter.setFontSize(24);
      await SunmiPrinter.printLineWrap(1);
      await SunmiPrinter.printTextTable(['CASH', '1000.00'], [1, 1], [0, 2]);
      await SunmiPrinter.printTextTable(['CHANGE', '400.00'], [1, 1], [0, 2]);
      await SunmiPrinter.printText(`\n--------------------\n\n`);

      await SunmiPrinter.printBarcode('1234567890', 8, 80, 2, 0);
      await SunmiPrinter.printLineWrap(1);
      await SunmiPrinter.printText(`THANK YOU\n`);
      await SunmiPrinter.printText(`Glad to see you again!\n`);
      await SunmiPrinter.printLineWrap(2);

      await SunmiPrinter.printText(`Subscribe to Social Media\n\n`);
      await SunmiPrinter.printQrCode('https://google.com', 8, 2);
      await SunmiPrinter.printLineWrap(3);

      await SunmiPrinter.feedPaper();
    } catch (e) {
      if (e instanceof Error) Alert.alert('Print Error', e.message);
      else Alert.alert('Print Error', `${e}`);
    }
  };

  const showPrinterStatus = async () => {
    try {
      await SunmiPrinter.showPrinterStatus();
    } catch (e) {
      if (e instanceof Error) Alert.alert('Status Error', e.message);
      else Alert.alert('Status Error', `${e}`);
    }
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />

      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={innerContainerStyle}
      >
        <View>
          <View>
            <Text style={styles.title}>Printer Serial No</Text>
            <Text>{printerSerialNo}</Text>
          </View>
          <View style={{ marginTop: 20 }}>
            <Text style={styles.title}>Printer Model</Text>
            <Text>{printerModel}</Text>
          </View>
          <View style={{ marginTop: 20 }}>
            <Text style={styles.title}>Printer Version</Text>
            <Text>{printerVersion}</Text>
          </View>
          <View style={{ marginTop: 20 }}>
            <Text style={styles.title}>Printer Paper</Text>
            <Text>{printerPaper}</Text>
          </View>
        </View>

        <View style={{ marginTop: 40 }}>
          <Button
            onPress={printExampleReceipt}
            title="Print Example Receipt"
            color="#841584"
          />
        </View>
        <View style={{ marginTop: 10 }}>
          <Button
            onPress={showPrinterStatus}
            title="Check Printer Status"
            color="#841584"
          />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  title: {
    fontSize: 14,
    fontWeight: 'bold',
  },
  button: {
    paddingVertical: 15,
  },
});

// eslint-disable-next-line prettier/prettier
const exampleBase64Image = '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';
