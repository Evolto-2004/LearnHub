import { useState } from "react";
import { Button, Result } from "antd";
import { useNavigate, useLocation } from "react-router-dom";
import styles from "./index.module.scss";

const ErrorPage = () => {
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [status, setStatus] = useState(String(result.get("status") || "404"));

  return (
    <Result
      status="404"
      title={status}
      subTitle="The page you visited does not exist"
      className={styles["main"]}
      extra={
        <Button
          type="primary"
          onClick={() => {
            navigate("/", { replace: true });
          }}
        >
          Back to Home
        </Button>
      }
    />
  );
};

export default ErrorPage;
