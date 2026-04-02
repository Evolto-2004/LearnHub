import React, { useEffect, useState } from "react";
import { Image, ProgressBar } from "antd-mobile";
import { useNavigate } from "react-router-dom";
import styles from "./courses-model.module.scss";
import mediaIcon from "../../../assets/images/commen/icon-medal.png";

interface PropInterface {
  id: number;
  title: string;
  thumb: string;
  isRequired: number;
  record: any;
  hourCount: number;
}

export const CoursesModel: React.FC<PropInterface> = ({
  id,
  title,
  thumb,
  isRequired,
  record,
  hourCount,
}) => {
  const navigate = useNavigate();
  const [userCourseProgress, setUserCourseProgress] = useState(0);

  useEffect(() => {
    if (record?.progress) {
      setUserCourseProgress(Math.floor(record.progress / 100));
    } else if (hourCount && hourCount > 0) {
      setUserCourseProgress(1);
    } else {
      setUserCourseProgress(0);
    }
  }, [record, hourCount]);

  return (
    <>
      <div
        className={styles["item"]}
        onClick={() => {
          navigate(`/course/${id}`);
        }}
      >
        <Image
          width={100}
          height={75}
          style={{ borderRadius: 8, marginRight: 15 }}
          src={thumb}
        />
        <div className={styles["info"]}>
          <div className={styles["title"]}>{title}</div>
          <div className={styles["status-content"]}>
            {isRequired === 1 && <div className={styles["type"]}>Required</div>}
            {isRequired === 0 && (
              <div className={styles["active-type"]}>Optional</div>
            )}
            {userCourseProgress == 0 && (
              <>
                <ProgressBar
                  percent={0}
                  style={{
                    flex: 1,
                    "--fill-color": "#2563EB",
                    "--track-color": "#F6F6F6",
                    "--track-width": "8px",
                    "--text-width": "27px",
                  }}
                />
                <span className={styles["no-pro"]}>Not Started</span>
              </>
            )}
            {userCourseProgress > 0 && userCourseProgress < 100 && (
              <ProgressBar
                percent={userCourseProgress}
                text
                style={{
                  flex: 1,
                  "--fill-color": "#2563EB",
                  "--track-color": "#F6F6F6",
                  "--track-width": "8px",
                  "--text-width": "27px",
                }}
              />
            )}
            {userCourseProgress >= 100 && (
              <div className={styles["success"]}>
                <Image
                  width={20}
                  height={20}
                  src={mediaIcon}
                  style={{ marginRight: 5 }}
                />
                <span>Course completed!</span>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};
