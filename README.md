# 天氣預報 Android 應用程式

一個美觀、現代化的 Android 天氣預報應用程式，使用 Kotlin 開發，提供當前天氣信息和 5 天天氣預報。

## 功能特色

- 🌤️ **當前天氣信息**：顯示實時溫度、天氣狀況、體感溫度
- 📍 **自動定位**：自動獲取用戶當前位置的天氣信息
- 📊 **詳細信息**：濕度、風速、氣壓、能見度等詳細天氣數據
- 🗓️ **5天預報**：未來5天的天氣預報，包括最高/最低溫度
- 🔄 **下拉刷新**：支持下拉刷新更新天氣數據
- 🎨 **現代化UI**：Material Design 設計，漸變背景，美觀的卡片布局
- 🌐 **中文支持**：完整的中文界面和天氣描述

## 技術架構

- **語言**：Kotlin
- **架構**：MVVM 架構模式
- **網絡請求**：Retrofit + OkHttp
- **JSON解析**：Gson
- **位置服務**：Google Play Services Location
- **權限管理**：Dexter
- **UI組件**：Material Design Components
- **數據綁定**：View Binding

## 安裝要求

- Android API 24 (Android 7.0) 或更高版本
- 網絡連接
- 位置權限

## 設置說明

### 1. 獲取 OpenWeatherMap API 密鑰

1. 前往 [OpenWeatherMap](https://openweathermap.org/api) 註冊免費帳戶
2. 登錄後在 API keys 頁面生成新的 API 密鑰
3. 複製您的 API 密鑰

### 2. 配置 API 密鑰

在 `MainActivity.kt` 文件中找到以下行：

```kotlin
private val apiKey = "YOUR_API_KEY_HERE" // 請替換為您的OpenWeatherMap API密鑰
```

將 `YOUR_API_KEY_HERE` 替換為您的實際 API 密鑰。

### 3. 構建項目

```bash
# 清理項目
./gradlew clean

# 構建 Debug 版本
./gradlew assembleDebug

# 安裝到設備
./gradlew installDebug
```

## 項目結構

```
app/
├── src/main/
│   ├── java/com/example/weatherforecast/
│   │   ├── MainActivity.kt                 # 主活動
│   │   ├── adapter/
│   │   │   └── ForecastAdapter.kt         # 預報列表適配器
│   │   ├── model/
│   │   │   └── WeatherResponse.kt         # 天氣數據模型
│   │   └── network/
│   │       ├── WeatherApi.kt              # API 接口定義
│   │       └── WeatherService.kt          # 網絡服務
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml          # 主界面布局
│   │   │   └── item_forecast.xml          # 預報項目布局
│   │   ├── drawable/
│   │   │   ├── gradient_background.xml    # 漸變背景
│   │   │   └── ic_sunny.xml              # 天氣圖標
│   │   ├── values/
│   │   │   ├── colors.xml                # 顏色資源
│   │   │   ├── strings.xml               # 字符串資源
│   │   │   └── themes.xml                # 主題樣式
│   │   └── xml/
│   │       ├── backup_rules.xml          # 備份規則
│   │       └── data_extraction_rules.xml # 數據提取規則
│   └── AndroidManifest.xml               # 應用清單
├── build.gradle                          # 應用構建配置
└── proguard-rules.pro                    # ProGuard 規則
```

## 使用方法

1. **啟動應用**：首次啟動時會請求位置權限
2. **授予權限**：允許應用訪問位置信息
3. **查看天氣**：應用會自動加載當前位置的天氣信息
4. **刷新數據**：下拉主界面可以刷新天氣數據
5. **查看預報**：滾動查看未來5天的天氣預報

## 權限說明

應用需要以下權限：

- `ACCESS_FINE_LOCATION`：獲取精確位置信息
- `ACCESS_COARSE_LOCATION`：獲取大概位置信息
- `INTERNET`：訪問網絡獲取天氣數據
- `ACCESS_NETWORK_STATE`：檢查網絡狀態

## API 參考

應用使用 [OpenWeatherMap API](https://openweathermap.org/api) 獲取天氣數據：

- **當前天氣 API**：`/weather`
- **5天預報 API**：`/forecast`
- **數據格式**：JSON
- **溫度單位**：攝氏度
- **語言**：繁體中文

## 自定義和擴展

### 添加新的天氣圖標

1. 在 `res/drawable/` 目錄添加新的矢量圖標
2. 在 `ForecastAdapter.kt` 中更新圖標映射邏輯

### 修改主題顏色

編輯 `res/values/colors.xml` 和 `res/values/themes.xml` 文件來自定義應用主題。

### 添加新的天氣信息

1. 更新 `WeatherResponse.kt` 數據模型
2. 修改 `activity_main.xml` 布局
3. 在 `MainActivity.kt` 中添加相應的UI更新邏輯

## 故障排除

### 常見問題

1. **無法獲取天氣數據**
   - 檢查網絡連接
   - 確認 API 密鑰是否正確設置
   - 檢查 API 密鑰是否有效且未過期

2. **位置獲取失敗**
   - 確認已授予位置權限
   - 檢查設備定位服務是否開啟
   - 嘗試在室外或窗邊獲取更好的GPS信號

3. **應用崩潰**
   - 檢查 Logcat 輸出獲取錯誤信息
   - 確認所有依賴項版本兼容
   - 檢查網絡請求是否正確處理異常

## 貢獻指南

歡迎貢獻代碼！請遵循以下步驟：

1. Fork 此倉庫
2. 創建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打開 Pull Request

## 版本歷史

- **v1.0.0** - 初始發布
  - 基本天氣信息顯示
  - 5天天氣預報
  - 自動定位功能
  - Material Design UI

## 許可證

此項目採用 MIT 許可證 - 詳見 [LICENSE](LICENSE) 文件

## 致謝

- [OpenWeatherMap](https://openweathermap.org/) - 提供天氣數據 API
- [Material Design](https://material.io/) - UI 設計指南
- [Retrofit](https://square.github.io/retrofit/) - 網絡請求庫
- [Dexter](https://github.com/Karumi/Dexter) - 權限管理庫

## 聯繫方式

如有問題或建議，請通過以下方式聯繫：

- 提交 [Issue](https://github.com/your-username/weather-forecast/issues)
- 發送郵件至：your-email@example.com

---

**注意**：此應用僅供學習和演示用途。在生產環境中使用前，請確保遵循所有相關的隱私政策和服務條款。