import { useState } from "react";
import styles from "./index.module.less";
import { Input, Button, message } from "antd";
import { login as loginApi, system } from "../../api/index";
import { setToken } from "../../utils/index";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import banner from "../../assets/images/login/banner.png";
import icon from "../../assets/images/login/icon.png";
import "./login.less";
import { loginAction } from "../../store/user/loginUserSlice";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../store/system/systemConfigSlice";
import { Footer } from "../../compenents/footer";

const LoginPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const loginSubmit = async () => {
    if (!email) {
      message.error("Please enter the admin email");
      return;
    }
    if (!password) {
      message.error("Please enter your password");
      return;
    }
    await handleSubmit();
  };

  const handleSubmit = async () => {
    if (loading) {
      return;
    }
    setLoading(true);
    try {
      let res: any = await loginApi.login(email, password);
      setToken(res.data.token); //将token写入本地
      await getSystemConfig(); //获取System Configuration并写入store
      await getUser(); //获取Sign In用户的信息并写入store

      navigate("/", { replace: true });
    } catch (e) {
      console.error("Error details", e);
      setLoading(false);
    }
  };

  const getUser = async () => {
    let res: any = await loginApi.getUser();
    dispatch(loginAction(res.data));
  };

  const getSystemConfig = async () => {
    let res: any = await system.getSystemConfig();
    let data: SystemConfigStoreInterface = {
      systemName: res.data["system.name"],
      systemLogo: res.data["system.logo"],
      systemPcUrl: res.data["system.pc_url"],
      systemH5Url: res.data["system.h5_url"],
      resourceUrl: res.data["resource_url"],
      memberDefaultAvatar: res.data["member.default_avatar"],
      courseDefaultThumbs: res.data["default.course_thumbs"],
      departments: res.data["departments"],
      resourceCategories: res.data["resource_categories"],
    };
    dispatch(saveConfigAction(data));
  };

  const keyUp = (e: any) => {
    if (e.keyCode === 13) {
      loginSubmit();
    }
  };

  return (
    <div className={styles["login-content"]}>
      <div className={styles["banner-box"]}>
        <img className={styles["banner"]} src={banner} alt="" />
      </div>
      <div className={styles["login-box"]}>
        <div className={styles["left-box"]}>
          <img className={styles["icon"]} src={icon} alt="" />
        </div>
        <div className={styles["right-box"]}>
          <div className={styles["eyebrow"]}>LEARNHUB CONSOLE</div>
          <div className={styles["title"]}>Admin Sign In</div>
          <div className={styles["desc"]}>
            A blue enterprise learning hub for managing courses, learners, and resources.
          </div>
          <div className="login-box d-flex mt-50">
            <Input
              value={email}
              onChange={(e) => {
                setEmail(e.target.value);
              }}
              style={{ width: 400, height: 54 }}
              placeholder="Please enter the admin email"
              allowClear
              onKeyUp={(e) => keyUp(e)}
            />
          </div>
          <div className="login-box d-flex mt-50">
            <Input.Password
              value={password}
              onChange={(e) => {
                setPassword(e.target.value);
              }}
              allowClear
              style={{ width: 400, height: 54 }}
              placeholder="Please enter your password"
              onKeyUp={(e) => keyUp(e)}
            />
          </div>
          <div className="login-box d-flex mt-50">
            <Button
              style={{ width: 400, height: 54 }}
              type="primary"
              onClick={loginSubmit}
              loading={loading}
            >
              Sign In
            </Button>
          </div>
        </div>
      </div>
      <div className={styles["footer-box"]}>
        <Footer type="none"></Footer>
      </div>
    </div>
  );
};

export default LoginPage;
