import { Input, Button, message } from "antd";
import React, { useState } from "react";
import styles from "./index.module.scss";
import banner from "../../assets/images/login/banner.png";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { loginAction } from "../../store/user/loginUserSlice";
import { login, user } from "../../api/index";
import { setToken } from "../../utils/index";
import { NoFooter } from "../../compenents";

const LoginPage: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const loginSubmit = (e: any) => {
    if (!email) {
      message.error("Please enter your email or UID");
      return;
    }
    if (!password) {
      message.error("Please enter your password");
      return;
    }
    if (loading) {
      return;
    }
    handleSubmit();
  };

  const keyUp = (e: any) => {
    if (e.keyCode === 13) {
      loginSubmit(e);
    }
  };

  const handleSubmit = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    login
      .login(email, password)
      .then((res: any) => {
        const token = res.data.token;
        setToken(token);
        getUser();
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const getUser = () => {
    user.detail().then((res: any) => {
      const data = res.data;
      dispatch(loginAction(data));
      setLoading(false);
      navigate("/", { replace: true });
    });
  };

  return (
    <div className={styles["login-content"]}>
      <div className={styles["top-content"]}>
        <div className={styles["eyebrow"]}>LEARNHUB PORTAL</div>
        <div className={styles["title"]}>Learner Sign In</div>
        <div className={styles["login-box"]}>
          <div className={styles["left-box"]}>
            <img className={styles["icon"]} src={banner} alt="" />
          </div>
          <div className={styles["right-box"]}>
            <div className={styles["desc"]}>
              Access courses, progress, and your profile in one place.
            </div>
            <div className="login-box d-flex">
              <Input
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                }}
                style={{ width: 400, height: 54 }}
                placeholder={"Please enter your email or UID"}
                onKeyUp={(e) => keyUp(e)}
              />
            </div>
            <div className="login-box d-flex mt-50">
              <Input.Password
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                }}
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
      </div>
      <div className={styles["footer"]}>
        <NoFooter></NoFooter>
      </div>
    </div>
  );
};

export default LoginPage;
