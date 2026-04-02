import ReactDOM from "react-dom/client";
import { Provider } from "react-redux";
import store from "./store";
import { BrowserRouter } from "react-router-dom";
import { ConfigProvider } from "antd-mobile";
import enUS from "antd-mobile/es/locales/en-US";
import "./assets/iconfont/iconfont.css";
import "./main.scss";
import App from "./App";
import AutoScorllTop from "./AutoTop";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <Provider store={store}>
    <ConfigProvider locale={enUS}>
      <BrowserRouter>
        <AutoScorllTop>
          <App />
        </AutoScorllTop>
      </BrowserRouter>
    </ConfigProvider>
  </Provider>
);
