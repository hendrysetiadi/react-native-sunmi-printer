
# react-native-sunmi-printer

## Getting started

`$ npm install react-native-sunmi-printer --save`

### Mostly automatic installation

`$ react-native link react-native-sunmi-printer`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.hendrysetiadi.reactnative.sunmiprinter.RNSunmiPrinterPackage;` to the imports at the top of the file
  - Add `new RNSunmiPrinterPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-sunmi-printer'
  	project(':react-native-sunmi-printer').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-sunmi-printer/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-sunmi-printer')
  	```


## Usage
```javascript
import RNSunmiPrinter from 'react-native-sunmi-printer';

// TODO: What to do with the module?
RNSunmiPrinter;
```
  