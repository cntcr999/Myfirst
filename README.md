# 天氣預測 Android 應用程式

這是一個功能完整的Android天氣預測應用程式，使用Kotlin開發，支援繁體中文界面。

## 功能特色

### 🌤️ 主要功能
- **實時天氣信息**: 顯示當前溫度、天氣狀況、濕度、風速、氣壓等詳細信息
- **5天天氣預測**: 提供未來5天的天氣預報
- **城市搜尋**: 支援手動輸入城市名稱查詢天氣
- **GPS定位**: 自動獲取當前位置的天氣信息
- **天氣詳情頁面**: 提供更詳細的天氣數據，包括日出日落時間、能見度、紫外線指數等

### 🎨 UI設計
- **現代化Material Design**: 使用Material 3設計規範
- **漸變背景**: 美觀的藍色漸變背景
- **卡片式佈局**: 清晰的信息層次
- **天氣圖標**: 根據天氣狀況顯示相應的圖標
- **響應式設計**: 適配不同螢幕尺寸

### 🔧 技術架構
- **MVVM架構**: 使用ViewModel和LiveData
- **Retrofit**: 網路請求和API調用
- **Coroutines**: 異步處理
- **View Binding**: 安全的視圖綁定
- **Material Components**: 現代UI組件
- **Location Services**: GPS定位服務

## 安裝和設置

### 前置需求
- Android Studio Arctic Fox 或更新版本
- Android SDK API 24 (Android 7.0) 或更高
- Kotlin 1.9.0+

### 設置步驟

1. **克隆專案**
   ```bash
   git clone <repository-url>
   cd weather-forecast-app
   ```

2. **獲取API Key**
   - 前往 [OpenWeatherMap](https://openweathermap.org/api) 註冊帳號
   - 獲取免費的API Key
   - 在 `WeatherViewModel.kt` 中替換 `YOUR_API_KEY`

3. **打開專案**
   - 使用Android Studio打開專案
   - 等待Gradle同步完成

4. **運行應用**
   - 連接Android設備或啟動模擬器
   - 點擊Run按鈕或按Ctrl+R

## API配置

### OpenWeatherMap API
應用程式使用OpenWeatherMap API來獲取天氣數據。你需要：

1. 註冊免費帳號: https://openweathermap.org/api
2. 獲取API Key
3. 在以下文件中替換API Key:
   - `app/src/main/java/com/weatherapp/forecast/viewmodel/WeatherViewModel.kt`

```kotlin
// 替換這裡的YOUR_API_KEY
apiKey = "你的_API_KEY_在這裡"
```

### API功能
- 當前天氣數據
- 5天天氣預報
- 按城市名稱搜尋
- 按GPS坐標搜尋
- 多語言支援（繁體中文）

## 權限說明

應用程式需要以下權限：

- `INTERNET`: 網路連接，獲取天氣數據
- `ACCESS_NETWORK_STATE`: 檢查網路狀態
- `ACCESS_FINE_LOCATION`: 精確定位
- `ACCESS_COARSE_LOCATION`: 大概定位

## 專案結構

```
app/
├── src/main/
│   ├── java/com/weatherapp/forecast/
│   │   ├── MainActivity.kt                 # 主活動
│   │   ├── WeatherDetailActivity.kt       # 天氣詳情頁面
│   │   ├── adapter/
│   │   │   └── WeatherAdapter.kt          # RecyclerView適配器
│   │   ├── model/
│   │   │   └── WeatherData.kt             # 數據模型
│   │   ├── network/
│   │   │   ├── WeatherApiService.kt       # API服務接口
│   │   │   └── RetrofitInstance.kt        # Retrofit配置
│   │   └── viewmodel/
│   │       └── WeatherViewModel.kt        # ViewModel
│   └── res/
│       ├── layout/                        # 佈局文件
│       ├── drawable/                      # 圖標和背景
│       ├── values/                        # 顏色、字符串、樣式
│       └── xml/                           # 配置文件
```

## 功能說明

### 主頁面功能
1. **搜尋欄**: 輸入城市名稱搜尋天氣
2. **定位按鈕**: 使用GPS獲取當前位置天氣
3. **當前天氣卡片**: 顯示溫度、天氣描述、體感溫度等
4. **天氣詳情**: 濕度、風速、氣壓信息
5. **5天預報**: 未來5天的天氣預測

### 詳情頁面功能
1. **完整天氣信息**: 所有可用的天氣數據
2. **日出日落時間**: 當日的日出日落時間
3. **能見度**: 當前能見度數據
4. **紫外線指數**: UV指數信息

## 自定義和擴展

### 添加新的天氣圖標
1. 在 `res/drawable/` 添加新的圖標文件
2. 在 `MainActivity.kt` 和 `WeatherAdapter.kt` 中更新圖標映射邏輯

### 修改主題顏色
在 `res/values/colors.xml` 中修改顏色值：
```xml
<color name="primary_color">#你的顏色</color>
<color name="accent_color">#你的顏色</color>
```

### 添加新語言
1. 創建 `res/values-語言代碼/strings.xml`
2. 翻譯所有字符串資源

## 故障排除

### 常見問題

1. **API請求失敗**
   - 檢查網路連接
   - 確認API Key是否正確
   - 檢查API配額是否用完

2. **定位失敗**
   - 確認位置權限已授予
   - 檢查GPS是否開啟
   - 確認網路連接正常

3. **編譯錯誤**
   - 檢查Android SDK版本
   - 確認Gradle配置正確
   - 清理並重建專案

### 偵錯模式
應用程式包含詳細的日誌記錄，可在Android Studio的Logcat中查看網路請求和錯誤信息。

## 貢獻指南

歡迎提交Issue和Pull Request來改進這個專案！

### 開發流程
1. Fork專案
2. 創建功能分支
3. 提交更改
4. 創建Pull Request

## 許可證

本專案採用MIT許可證。

## 更新日誌

### v1.0.0
- 初始版本發布
- 基本天氣查詢功能
- 5天天氣預報
- GPS定位支援
- 繁體中文界面

---

**注意**: 記得在發布前替換OpenWeatherMap API Key，並遵循相關的使用條款。