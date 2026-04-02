import styles from "./index.module.scss";
import React from "react";
import { Image } from "antd-mobile";
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
      <Image src={empty} width={180} height={180} className={styles["image"]} />
      <div className={styles["title"]}>{title}</div>
      <div className={styles["description"]}>{description}</div>
    </div>
  );
};
