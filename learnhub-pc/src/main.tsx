import ReactDOM from "react-dom/client";
import { Provider } from "react-redux";
import store from "./store";
import { BrowserRouter } from "react-router-dom";
import { ConfigProvider, Empty as AntdEmpty } from "antd";
import enUS from "antd/locale/en_US";
import "./assets/iconfont/iconfont.css";
import App from "./App";
import "./index.scss"; //全局样式
import AutoScorllTop from "./AutoTop";
import { pcTheme } from "./theme";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <Provider store={store}>
    <ConfigProvider
      locale={enUS}
      theme={pcTheme}
      renderEmpty={() => (
        <AntdEmpty
          image={AntdEmpty.PRESENTED_IMAGE_SIMPLE}
          description="No records yet"
        />
      )}
    >
      <BrowserRouter>
        <AutoScorllTop>
          <App />
        </AutoScorllTop>
      </BrowserRouter>
    </ConfigProvider>
  </Provider>
);
