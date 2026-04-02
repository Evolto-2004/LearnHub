import ReactDOM from "react-dom/client";
import "./assets/iconfont/iconfont.css";
import "./index.less";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./store";
import { ConfigProvider } from "antd";
import zhCN from "antd/locale/zh_CN";
import "dayjs/locale/zh-cn";
import AutoScorllTop from "./AutoTop";
import { adminTheme } from "./theme";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <Provider store={store}>
    <ConfigProvider locale={zhCN} theme={adminTheme}>
      <BrowserRouter>
        <AutoScorllTop>
          <App />
        </AutoScorllTop>
      </BrowserRouter>
    </ConfigProvider>
  </Provider>
);
