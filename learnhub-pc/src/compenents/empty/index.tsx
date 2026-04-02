import styles from "./index.module.scss";
import React from "react";
import { Image } from "antd";
import empty from "../../assets/images/commen/empty.png";

type Props = {
  title?: string;
  description?: string;
};

export const Empty: React.FC<Props> = ({
  title = "No records yet",
  description = "New learning activity will appear here.",
}) => {
  return (
    <div className={styles["empty-card"]}>
      <Image
        className={styles["illustration"]}
        src={empty}
        width={280}
        height={280}
        preview={false}
      />
      <div className={styles["title"]}>{title}</div>
      <div className={styles["description"]}>{description}</div>
    </div>
  );
};
