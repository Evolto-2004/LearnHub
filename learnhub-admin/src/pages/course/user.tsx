import { useEffect, useState } from "react";
import {
  Row,
  Col,
  Button,
  Modal,
  Typography,
  Input,
  Table,
  message,
  Image,
} from "antd";
import { course as Course } from "../../api";
import { useParams, useLocation } from "react-router-dom";
import type { ColumnsType } from "antd/es/table";
import { BackBartment } from "../../compenents";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { PerButton } from "../../compenents";
import { dateFormat } from "../../utils/index";
import memberDefaultAvatar from "../../assets/thumb/avatar.png";

const { confirm } = Modal;

interface DataType {
  id: React.Key;
  avatar: string;
  create_city?: string;
  create_ip?: string;
  created_at?: string;
  credit1?: number;
  email: string;
  id_card?: string;
  is_active?: number;
  is_lock?: number;
  is_set_password?: number;
  is_verify?: number;
  login_at?: string;
  name: string;
  updated_at?: string;
  verify_at?: string;
}

type UserCourseRecordsModel = {
  [key: number]: CourseRecordModel;
};

type CourseRecordModel = {
  course_id: number;
  created_at: string;
  finished_at?: string;
  finished_count: number;
  hour_count: number;
  id: number;
  is_finished: number;
  progress: number;
  updated_at: string;
  user_id: number;
};

type HourCountModel = {
  [key: number]: string;
};

type PerCourseRecordsModel = {
  [key: number]: {
    course_id: number;
    created_at: string;
    finished_at: string;
    finished_duration: number;
    hour_id: number;
    id: number;
    is_finished: number;
    real_duration: number;
    total_duration: number;
    updated_at: string;
    user_id: number;
  };
};

const CourseUserPage = () => {
  const params = useParams();
  const result = new URLSearchParams(useLocation().search);
  const [list, setList] = useState<DataType[]>([]);
  const [course, setCourse] = useState<CourseModel | null>(null);
  const [records, setRecords] = useState<UserCourseRecordsModel>({});
  const [hourCount, setHourCount] = useState<HourCountModel>({});
  const [userDepIds, setUserDepIds] = useState<DepIdsModel>({});
  const [departments, setDepartments] = useState<DepartmentsModel>({});
  const [perRecords, setPerRecords] = useState<PerCourseRecordsModel>({});
  const [refresh, setRefresh] = useState(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [idCard, setIdCard] = useState("");
  const [selectedRowKeys, setSelectedRowKeys] = useState<any>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [title, setTitle] = useState<string>(String(result.get("title")));

  const columns: ColumnsType<DataType> = [
    {
      title: "Learner",
      render: (_, record: any) => (
        <div className="d-flex">
          <Image
            style={{ borderRadius: "50%" }}
            preview={false}
            width={40}
            height={40}
            src={
              record.avatar == -1
                ? memberDefaultAvatar
                : resourceUrl[record.avatar]
            }
          ></Image>
          <span className="ml-8">{record.name}</span>
        </div>
      ),
    },
    {
      title: "Email",
      render: (_, record: any) => <span>{record.email}</span>,
    },
    {
      title: "Department",
      render: (_, record: any) => (
        <div className="float-left">
          {userDepIds[record.id] &&
            userDepIds[record.id].map((item: any, index: number) => {
              return (
                <span key={index}>
                  {index === userDepIds[record.id].length - 1
                    ? departments[item]
                    : departments[item] + ", "}
                </span>
              );
            })}
        </div>
      ),
    },
    {
      title: "Course Progress",
      dataIndex: "progress",
      render: (_, record: any) => (
        <span>
          Completed Lessons: 
          {(records[record.id] && records[record.id].finished_count) ||
            0} /{" "}
          {(records[record.id] && records[record.id].hour_count) ||
            course?.class_hour}
        </span>
      ),
    },
    {
      title: "First Learning Time",
      dataIndex: "created_at",
      render: (_, record: any) => (
        <>
          {perRecords[record.id] ? (
            <span>{dateFormat(perRecords[record.id].created_at)}</span>
          ) : hourCount[record.id] ? (
            <span>{dateFormat(hourCount[record.id])}</span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "Completed At",
      dataIndex: "id",
      render: (_, record: any) => (
        <>
          {records[record.id] && records[record.id].finished_at ? (
            <span>{dateFormat(String(records[record.id].finished_at))}</span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "Progress",
      dataIndex: "progress",
      render: (_, record: any) => (
        <>
          {records[record.id] ? (
            <span
              className={
                Math.floor(
                  (records[record.id].finished_count /
                    records[record.id].hour_count) *
                    100
                ) >= 100
                  ? "c-green"
                  : "c-red"
              }
            >
              {Math.floor(
                (records[record.id].finished_count /
                  records[record.id].hour_count) *
                  100
              )}
              %
            </span>
          ) : hourCount[record.id] ? (
            <span className="c-red">1%</span>
          ) : (
            <span className="c-red">0%</span>
          )}
        </>
      ),
    },
  ];

  useEffect(() => {
    getList();
  }, [params.courseId, refresh, page, size]);

  const getList = () => {
    setLoading(true);
    Course.courseUser(
      Number(params.courseId),
      page,
      size,
      "",
      "",
      name,
      email,
      idCard
    )
      .then((res: any) => {
        setTotal(res.data.total);
        setList(res.data.data);
        setHourCount(res.data.user_course_hour_user_first_at);
        setRecords(res.data.user_course_records);
        setPerRecords(res.data.per_user_earliest_records);
        setResourceUrl(res.data.resource_url);
        setCourse(res.data.course);
        setDepartments(res.data.departments);
        setUserDepIds(res.data.user_dep_ids);
        setLoading(false);
      })
      .catch((err: any) => {
        console.log("Error,", err);
      });
  };

  // Reset列表
  const resetList = () => {
    setPage(1);
    setSize(10);
    setList([]);
    setName("");
    setEmail("");
    setIdCard("");
    setRefresh(!refresh);
  };

  const paginationProps = {
    current: page, //当前页码
    pageSize: size,
    total: total, // 总条数
    onChange: (page: number, pageSize: number) =>
      handlePageChange(page, pageSize), //改变页码的函数
    showSizeChanger: true,
  };

  const handlePageChange = (page: number, pageSize: number) => {
    setPage(page);
    setSize(pageSize);
  };

  // DeleteLearner
  const delItem = () => {
    if (selectedRowKeys.length === 0) {
      message.error("Select learners before resetting");
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Reset learning records for the selected learners?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        Course.destroyCourseUser(Number(params.courseId), selectedRowKeys).then(
          () => {
            message.success("Cleared successfully");
            resetList();
          }
        );
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const rowSelection = {
    onChange: (selectedRowKeys: React.Key[], selectedRows: DataType[]) => {
      setSelectedRowKeys(selectedRowKeys);
    },
  };

  return (
    <>
      <Row className="learnhub-main-body">
        <Col span={24}>
          <div className="float-left mb-24">
            <BackBartment title={title || "Course Learners"} />
          </div>
          <div className="float-left j-b-flex mb-24">
            <div className="d-flex">
              <PerButton
                type="primary"
                text="Reset Learning Records"
                class="mr-16"
                icon={null}
                p="course"
                onClick={() => delItem()}
                disabled={selectedRowKeys.length === 0}
              />
            </div>
            <div className="d-flex">
              <div className="d-flex mr-24">
                <Typography.Text>Learner Name：</Typography.Text>
                <Input
                  value={name}
                  onChange={(e) => {
                    setName(e.target.value);
                  }}
                  allowClear
                  style={{ width: 160 }}
                  placeholder="Enter a name keyword"
                />
              </div>
              <div className="d-flex mr-24">
                <Typography.Text>LearnerEmail：</Typography.Text>
                <Input
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value);
                  }}
                  allowClear
                  style={{ width: 160 }}
                  placeholder="Please enter the learner email"
                />
              </div>
              {/* <div className="d-flex mr-24">
                <Typography.Text>ID Number：</Typography.Text>
                <Input
                  value={idCard}
                  onChange={(e) => {
                    setIdCard(e.target.value);
                  }}
                  style={{ width: 160 }}
                  placeholder="Please enter an ID number"
                />
              </div> */}
              <div className="d-flex">
                <Button className="mr-16" onClick={resetList}>
                  Reset
                </Button>
                <Button
                  type="primary"
                  onClick={() => {
                    setPage(1);
                    setRefresh(!refresh);
                  }}
                >
                  Search
                </Button>
              </div>
            </div>
          </div>
          <div className="float-left">
            <Table
              rowSelection={{
                type: "checkbox",
                ...rowSelection,
              }}
              columns={columns}
              dataSource={list}
              loading={loading}
              pagination={paginationProps}
              rowKey={(record) => record.id}
            />
          </div>
        </Col>
      </Row>
    </>
  );
};
export default CourseUserPage;
