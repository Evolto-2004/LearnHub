import { useEffect, useState } from "react";
import { Row, Col, Spin, Image } from "antd";
import { useNavigate, useLocation } from "react-router-dom";
import { user } from "../../api/index";
import styles from "./index.module.scss";
import { useSelector } from "react-redux";
import { CoursesModel } from "./compenents/courses-model";
import { Empty } from "../../compenents";
import myLesoon from "../../assets/images/commen/icon-mylesoon.png";
import studyTime from "../../assets/images/commen/icon-studytime.png";
import iconRoute from "../../assets/images/commen/icon-route.png";
import defaultThumb1 from "../../assets/thumb/thumb1.png";
import defaultThumb2 from "../../assets/thumb/thumb2.png";
import defaultThumb3 from "../../assets/thumb/thumb3.png";
import { studyTimeFormat } from "../../utils/index";

type StatsModel = {
  learn_duration: number;
  nun_required_course_count: number;
  nun_required_finished_course_count: number;
  nun_required_finished_hour_count: number;
  nun_required_hour_count: number;
  required_course_count: number;
  required_finished_course_count: number;
  required_finished_hour_count: number;
  required_hour_count: number;
  today_learn_duration: number;
};

type LearnCourseRecordsModel = {
  [key: number]: CourseRecordModel;
};

const IndexPage = () => {
  const navigate = useNavigate();
  const result = new URLSearchParams(useLocation().search);
  const [loading, setLoading] = useState<boolean>(false);
  const [tabKey, setTabKey] = useState(Number(result.get("tab") || 0));
  const [coursesList, setCoursesList] = useState<CourseModel[]>([]);
  const [learnCourseRecords, setLearnCourseRecords] =
    useState<LearnCourseRecordsModel>({});
  const [learnCourseHourCount, setLearnCourseHourCount] = useState<any>({});
  const [stats, setStats] = useState<StatsModel | null>(null);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const currentDepId = useSelector(
    (state: any) => state.loginUser.value.currentDepId
  );

  useEffect(() => {
    if (currentDepId === 0) {
      return;
    }
    getData();
  }, [tabKey, currentDepId]);

  const getData = () => {
    setLoading(true);
    user.courses(currentDepId).then((res: any) => {
      const records: LearnCourseRecordsModel = res.data.learn_course_records;
      setStats(res.data.stats);
      setLearnCourseRecords(records);
      setLearnCourseHourCount(res.data.user_course_hour_count);
      setResourceUrl(res.data.resource_url);
      if (tabKey === 0) {
        setCoursesList(res.data.courses);
      } else if (tabKey === 1) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (item.is_required === 1) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (tabKey === 2) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (item.is_required === 0) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (tabKey === 3) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (records[item.id] && records[item.id].progress >= 10000) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      } else if (tabKey === 4) {
        const arr: CourseModel[] = [];
        res.data.courses.map((item: any) => {
          if (
            !records[item.id] ||
            (records[item.id] && records[item.id].progress < 10000)
          ) {
            arr.push(item);
          }
        });
        setCoursesList(arr);
      }
      setLoading(false);
    });
  };

  const items = [
    {
      key: 0,
      label: `All`,
    },
    {
      key: 1,
      label: `Required`,
    },
    {
      key: 2,
      label: `Optional`,
    },
    {
      key: 3,
      label: `Completed`,
    },
    {
      key: 4,
      label: `Incomplete`,
    },
  ];

  const onChange = (key: number) => {
    setTabKey(key);
    navigate("/?tab=" + key);
  };

  return (
    <div className="main-body">
      <div className="content">
        <div className={styles["top-cont"]}>
          <div className={styles["top-item"]}>
            <div className={styles["title"]}>
              <img className={styles["icon"]} src={myLesoon} />
              <span>Course Progress</span>
            </div>
            <div
              className={`${styles["info"]} ${styles["course-progress-info"]}`}
            >
              <div className={styles["info-item"]}>
                <span>Required: Completed courses</span>
                <strong>{stats?.required_finished_course_count || 0}</strong>
                <span>/</span>
                <span>{stats?.required_course_count || 0}</span>
              </div>
              {stats && stats.nun_required_course_count > 0 && (
                <div className={styles["info-item"]}>
                  <span>Optional: Completed courses</span>
                  <strong>{stats?.nun_required_finished_course_count || 0}</strong>
                  <span>/</span>
                  <span>{stats?.nun_required_course_count || 0}</span>
                </div>
              )}
            </div>
          </div>
          <div className={styles["top-item"]}>
            <div className={styles["title"]}>
              <img className={styles["icon"]} src={studyTime} />
              <span>Study Duration</span>
            </div>
            {stats ? (
              <div
                className={`${styles["info"]} ${styles["study-duration-info"]}`}
              >
                <div className={styles["info-item"]}>
                  Today：
                  {studyTimeFormat(stats.today_learn_duration)[0] !== 0 && (
                    <>
                      <strong>
                        {" "}
                        {studyTimeFormat(stats.today_learn_duration)[0] ||
                          0}{" "}
                      </strong>
                      h
                    </>
                  )}
                  <strong>
                    {" "}
                    {studyTimeFormat(stats.today_learn_duration)[1] || 0}{" "}
                  </strong>
                   min
                </div>
                <div className={styles["info-item"]}>
                  Total: 
                  {studyTimeFormat(stats.learn_duration || 0)[0] !== 0 && (
                    <>
                      <strong>
                        {" "}
                        {studyTimeFormat(stats.learn_duration || 0)[0] ||
                          0}{" "}
                      </strong>
                      h
                    </>
                  )}
                  <strong>
                    {" "}
                    {studyTimeFormat(stats.learn_duration || 0)[1] || 0}{" "}
                  </strong>
                   min
                </div>
              </div>
            ) : null}
          </div>
        </div>
        <div className={styles["tabs"]}>
          {items.map((item: any) => (
            <div
              key={item.key}
              className={
                item.key === tabKey
                  ? styles["tab-active-item"]
                  : styles["tab-item"]
              }
              onClick={() => {
                onChange(item.key);
              }}
            >
              <div className={styles["tit"]}>{item.label}</div>
              {item.key === tabKey && (
                <Image
                  className={styles["banner"]}
                  width={40}
                  height={8}
                  preview={false}
                  src={iconRoute}
                  style={{ marginTop: -16 }}
                />
              )}
            </div>
          ))}
        </div>
        {loading && (
          <Row
            style={{
              width: 1200,
              margin: "0 auto",
              paddingTop: 14,
              minHeight: 301,
            }}
          >
            <div className="float-left d-j-flex mt-50">
              <Spin size="large" />
            </div>
          </Row>
        )}

        {!loading && coursesList.length === 0 && (
          <Row
            style={{
              width: 1200,
              margin: "0 auto",
              paddingTop: 14,
              minHeight: 301,
            }}
          >
            <Col span={24}>
              <Empty />
            </Col>
          </Row>
        )}
        {!loading && coursesList.length > 0 && (
          <div className={styles["courses-list"]}>
            {coursesList.map((item: any) => (
              <div key={item.id}>
                {learnCourseRecords[item.id] && (
                  <CoursesModel
                    id={item.id}
                    title={item.title}
                    thumb={
                      item.thumb === -1
                        ? defaultThumb1
                        : item.thumb === -2
                        ? defaultThumb2
                        : item.thumb === -3
                        ? defaultThumb3
                        : resourceUrl[item.thumb]
                    }
                    isRequired={item.is_required}
                    progress={Math.floor(
                      learnCourseRecords[item.id].progress / 100
                    )}
                  ></CoursesModel>
                )}

                {!learnCourseRecords[item.id] &&
                  learnCourseHourCount[item.id] &&
                  learnCourseHourCount[item.id] > 0 && (
                    <CoursesModel
                      id={item.id}
                      title={item.title}
                      thumb={
                        item.thumb === -1
                          ? defaultThumb1
                          : item.thumb === -2
                          ? defaultThumb2
                          : item.thumb === -3
                          ? defaultThumb3
                          : resourceUrl[item.thumb]
                      }
                      isRequired={item.is_required}
                      progress={1}
                    ></CoursesModel>
                  )}
                {!learnCourseRecords[item.id] &&
                  !learnCourseHourCount[item.id] && (
                    <CoursesModel
                      id={item.id}
                      title={item.title}
                      thumb={
                        item.thumb === -1
                          ? defaultThumb1
                          : item.thumb === -2
                          ? defaultThumb2
                          : item.thumb === -3
                          ? defaultThumb3
                          : resourceUrl[item.thumb]
                      }
                      isRequired={item.is_required}
                      progress={0}
                    ></CoursesModel>
                  )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default IndexPage;
