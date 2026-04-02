import { useEffect, useState } from "react";
import { Button, Result } from "antd";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import styles from "./index.module.less";

const ErrorPage = () => {
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [code, setCode] = useState(Number(result.get("code")));
  const [error, setError] = useState("");

  useEffect(() => {
    setCode(Number(result.get("code")));
  }, [result.get("code")]);

  useEffect(() => {
    if (code === 403) {
      setError("Permission denied");
    } else if (code === 404) {
      setError("URL or resource not found");
    } else if (code === 429) {
      setError("Too many requests. Please try again later.");
    } else {
      setError("System Error");
    }
  }, [code]);

  return (
    <Result
      status="404"
      title={code}
      subTitle={error}
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
