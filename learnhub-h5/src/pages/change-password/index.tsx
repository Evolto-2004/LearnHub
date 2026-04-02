import { useState, useEffect } from "react";
import { Button, Toast, Input, Image } from "antd-mobile";
import styles from "./index.module.scss";
import { useNavigate } from "react-router-dom";
import { user } from "../../api/index";
import backIcon from "../../assets/images/commen/icon-back.png";

const ChangePasswordPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [againPassword, setAgainPassword] = useState("");

  useEffect(() => {
    document.title = "Change Password";
  }, []);

  const onFinish = () => {
    if (!oldPassword) {
      Toast.show({
        content: "Please enter your current password",
      });
      return;
    }
    if (!newPassword) {
      Toast.show({
        content: "Please enter a new password",
      });
      return;
    }
    if (!againPassword) {
      Toast.show({
        content: "Confirm the new password",
      });
      return;
    }
    if (againPassword !== newPassword) {
      Toast.show({
        content: "The password confirmation does not match",
      });
      return;
    }
    if (loading) {
      return;
    }
    handleSubmit();
  };

  const handleSubmit = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    user
      .password(oldPassword, newPassword)
      .then((res: any) => {
        Toast.show({
          content: "Password changed successfully",
        });
        navigate(-1);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  return (
    <div className="main-body">
      <div className="main-header">
        <Image
          className="back-icon"
          src={backIcon}
          onClick={() => navigate(-1)}
        />
        <div className="main-title">Change Password</div>
      </div>
      <div className={styles["form-box"]}>
        <div className={styles["input-box"]}>
          <div className={styles["input-box"]}>
            <Input
              type="password"
              className={styles["input-item"]}
              placeholder="Please enter your current password"
              value={oldPassword}
              onChange={(val) => {
                setOldPassword(val);
              }}
            />
            <div className={styles["line"]}></div>
            <Input
              type="password"
              className={styles["input-item"]}
              placeholder="Please enter a new password"
              value={newPassword}
              onChange={(val) => {
                setNewPassword(val);
              }}
            />
          </div>
        </div>
        <div className={styles["input2-box"]}>
          <Input
            type="password"
            className={styles["input-item"]}
            placeholder="Please confirm the new password"
            value={againPassword}
            onChange={(val) => {
              setAgainPassword(val);
            }}
          />
        </div>
        <div className={styles["button-box"]}>
          <Button
            className={styles["primary-button"]}
            disabled={
              oldPassword === "" || newPassword === "" || againPassword === ""
            }
            color="primary"
            loading={loading}
            onClick={onFinish}
          >
            Confirm Changes
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ChangePasswordPage;
