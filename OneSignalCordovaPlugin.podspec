Pod::Spec.new do |s|
  s.name = 'OneSignalCordovaPlugin'
  s.version = '5.3.11'
  s.summary = 'OneSignal Cordova and Capacitor push notification plugin'
  s.license = { :type => 'MIT', :file => 'LICENSE' }
  s.homepage = 'https://github.com/OneSignal/OneSignal-Cordova-SDK'
  s.author = 'OneSignal'
  s.source = { :git => 'https://github.com/OneSignal/OneSignal-Cordova-SDK.git', :tag => s.version.to_s }
  s.ios.deployment_target = '13.0'
  s.swift_version = '5.1'
  s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
  s.dependency 'Capacitor'
  s.dependency 'OneSignalXCFramework', '5.5.2'
end
