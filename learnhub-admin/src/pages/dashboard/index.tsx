import { useState, useEffect } from "react";
import styles from "./index.module.less";
import { useNavigate } from "react-router-dom";
import iconN1 from "../../assets/images/dashboard/icon-n1.png";
import iconN2 from "../../assets/images/dashboard/icon-n2.png";
import iconN3 from "../../assets/images/dashboard/icon-n3.png";
import { Footer } from "../../compenents/footer";
import { dashboard } from "../../api/index";
import { timeFormat } from "../../utils/index";

type BasicDataModel = {
  admin_user_total: number;
  course_total: number;
  department_total: number;
  user_learn_today: number;
  user_learn_top10?: Top10Model[];
  user_learn_top10_users?: Top10UserModel;
  user_learn_yesterday: number;
  user_today: number;
  user_total: number;
  user_yesterday: number;
  version: string;
};

type Top10Model = {
  created_date: string;
  duration: number;
  id: number;
  user_id: number;
};

type Top10UserModel = {
  [key: number]: UserModel;
};

const DashboardPage = () => {
  const navigate = useNavigate();
  const [basicData, setBasicData] = useState<BasicDataModel | null>(null);

  const getData = () => {
    dashboard.dashboardList().then((res: any) => {
      setBasicData(res.data);
    });
  };

  useEffect(() => {
    getData();
  }, []);

  const compareNum = (today: number, yesterday: number) => {
    let num = today - yesterday || 0;
    if (num < 0) {
      return (
        <span className="c-green">
          <i className={styles["down"]}>&#9660;</i>
          {Math.abs(num)}
        </span>
      );
    }
    return (
      <span className="c-red">
        <i className={styles["up"]}>&#9650;</i>
        {Math.abs(num)}
      </span>
    );
  };

  return (
    <>
      <div className={styles["dashboard-layout"]}>
        <div className={styles["summary-card"]}>
          <div className="learnhub-main-top">
            <div className={styles["stats-grid"]}>
              <div className={styles["label-item"]}>
                <div className={styles["label"]}>Learners Studying Today</div>
                <div className={styles["info"]}>
                  <div className={styles["num"]}>
                    {basicData?.user_learn_today}
                  </div>
                  {basicData && (
                    <div className={styles["compare"]}>
                      <span className="mr-5">vs. yesterday</span>
                      {compareNum(
                        basicData.user_learn_today,
                        basicData.user_learn_yesterday
                      )}
                    </div>
                  )}
                </div>
              </div>
              <div className={styles["label-item"]}>
                <div className={styles["label"]}>Total Learners</div>
                <div className={styles["info"]}>
                  <div className={styles["num"]}>{basicData?.user_total}</div>
                  {basicData && (
                    <div className={styles["compare"]}>
                      <span className="mr-5">vs. yesterday</span>
                      {compareNum(basicData.user_today, 0)}
                    </div>
                  )}
                </div>
              </div>
              <div className={styles["label-item"]}>
                <div className={styles["label"]}>Courses</div>
                <div className={styles["info"]}>
                  <div className={styles["num"]}>{basicData?.course_total}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={styles["summary-card"]}>
          <div className="learnhub-main-top">
            <div className={styles["stats-grid"]}>
              <div className={styles["label-item"]}>
                <div className={styles["label"]}>Departments</div>
                <div className={styles["info"]}>
                  <div className={styles["num"]}>
                    {basicData?.department_total}
                  </div>
                </div>
              </div>
              <div className={styles["label-item"]}>
                <div className={styles["label"]}>Admins</div>
                <div className={styles["info"]}>
                  <div className={styles["num"]}>
                    {basicData?.admin_user_total}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={styles["full-card"]}>
          <div className="learnhub-main-top">
            <div className={styles["large-title"]}>Quick Actions</div>
            <div className={styles["mode-box"]}>
              <div
                className={styles["link-mode"]}
                onClick={() => {
                  navigate("/member/index");
                }}
              >
                <i
                  className="iconfont icon-adduser"
                  style={{ color: "#2563EB", fontSize: 36 }}
                ></i>
                <span>Add Learner</span>
              </div>
              <div
                className={styles["link-mode"]}
                onClick={() => {
                  navigate("/videos");
                }}
              >
                <i
                  className="iconfont icon-upvideo"
                  style={{ color: "#0EA5E9", fontSize: 36 }}
                ></i>
                <span>Upload Video</span>
              </div>
              <div
                className={styles["link-mode"]}
                onClick={() => {
                  navigate("/course");
                }}
              >
                <i
                  className="iconfont icon-onlinelesson"
                  style={{ color: "#3B82F6", fontSize: 36 }}
                ></i>
                <span>Courses</span>
              </div>
              <div
                className={styles["link-mode"]}
                onClick={() => {
                  navigate("/department");
                }}
              >
                <i
                  className="iconfont icon-department"
                  style={{ color: "#16A34A", fontSize: 36 }}
                ></i>
                <span>Create Department</span>
              </div>
            </div>
          </div>
        </div>
        <div className={styles["full-card"]}>
          <div className="learnhub-main-top" style={{ minHeight: 376 }}>
            <div className={styles["large-title"]}>Top Learners Today</div>
            <div className={styles["rank-list"]}>
              {basicData?.user_learn_top10 && (
                <div className={styles["half-list"]}>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <img
                        className={styles["item-icon"]}
                        src={iconN1}
                        alt=""
                      />
                      {basicData.user_learn_top10[0] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[0].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[0] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[0].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <img
                        className={styles["item-icon"]}
                        src={iconN2}
                        alt=""
                      />
                      {basicData.user_learn_top10[1] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[1].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[1] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[1].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <img
                        className={styles["item-icon"]}
                        src={iconN3}
                        alt=""
                      />
                      {basicData.user_learn_top10[2] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[2].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[2] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[2].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>4</div>
                      {basicData.user_learn_top10[3] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[3].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[3] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[3].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>5</div>
                      {basicData.user_learn_top10[4] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[4].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[4] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[4].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                </div>
              )}
              {basicData?.user_learn_top10 && (
                <div className={styles["half-list"]}>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>6</div>
                      {basicData.user_learn_top10[5] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[5].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[5] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[5].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>7</div>
                      {basicData.user_learn_top10[6] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[6].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[6] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[6].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>8</div>
                      {basicData.user_learn_top10[7] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[7].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[7] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[7].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>9</div>
                      {basicData.user_learn_top10[8] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[8].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[8] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[8].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                  <div className={styles["rank-item"]}>
                    <div className={styles["left-item"]}>
                      <div className={styles["item-num"]}>10</div>
                      {basicData.user_learn_top10[9] &&
                        basicData.user_learn_top10_users && (
                          <div className={styles["item-name"]}>
                            {
                              basicData.user_learn_top10_users[
                                basicData.user_learn_top10[9].user_id
                              ]?.name
                            }
                          </div>
                        )}
                    </div>
                    {basicData.user_learn_top10[9] && (
                      <div className={styles["item-time"]}>
                        {timeFormat(
                          Number(basicData.user_learn_top10[9].duration) / 1000
                        )}
                      </div>
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
      <Footer></Footer>
    </>
  );
};

export default DashboardPage;
