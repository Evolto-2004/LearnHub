import { useState, useEffect } from "react";
import styles from "./progrss.module.less";
import { Table, Modal, message } from "antd";
import { PerButton, DurationText } from "../../../compenents";
import { user as member } from "../../../api/index";
import type { ColumnsType } from "antd/es/table";
import { dateFormat } from "../../../utils/index";
import { ExclamationCircleFilled } from "@ant-design/icons";
const { confirm } = Modal;

interface DataType {
  id: React.Key;
  title: string;
  type: string;
  created_at: string;
  duration: number;
  finished_duration: number;
  is_finished: boolean;
  finished_at: boolean;
}

interface PropInterface {
  open: boolean;
  uid: number;
  id: number;
  onCancel: () => void;
}

export const MemberLearnProgressDialog: React.FC<PropInterface> = ({
  open,
  uid,
  id,
  onCancel,
}) => {
  const [loading, setLoading] = useState<boolean>(false);
  const [list, setList] = useState<any>([]);
  const [records, setRecords] = useState<any>({});
  const [refresh, setRefresh] = useState(false);

  useEffect(() => {
    if (open) {
      getData();
    }
  }, [uid, id, open, refresh]);

  const getData = () => {
    if (loading) {
      return;
    }
    setLoading(true);
    member.learnCoursesProgress(uid, id, {}).then((res: any) => {
      setList(res.data.hours);
      setRecords(res.data.learn_records);
      setLoading(false);
    });
  };

  const column: ColumnsType<DataType> = [
    {
      title: "Lesson Title",
      dataIndex: "title",
      render: (title: string) => (
        <>
          <span>{title}</span>
        </>
      ),
    },
    {
      title: "Total Duration",
      dataIndex: "duration",
      render: (duration: number) => (
        <>
          <DurationText duration={duration}></DurationText>
        </>
      ),
    },
    {
      title: "Studied Duration",
      dataIndex: "finished_duration",
      render: (_, record: any) => (
        <>
          {records && records[record.id] ? (
            <span>
              <DurationText
                duration={records[record.id].finished_duration || 0}
              ></DurationText>
            </span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "Completed",
      dataIndex: "is_finished",
      render: (_, record: any) => (
        <>
          {records &&
          records[record.id] &&
          records[record.id].is_finished === 1 ? (
            <span className="c-green">Completed</span>
          ) : (
            <span className="c-red">Incomplete</span>
          )}
        </>
      ),
    },
    {
      title: "Start Time",
      dataIndex: "created_at",
      render: (_, record: any) => (
        <>
          {records && records[record.id] ? (
            <span>{dateFormat(records[record.id].created_at)}</span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "Completed At",
      dataIndex: "finished_at",
      render: (_, record: any) => (
        <>
          {records && records[record.id] ? (
            <span>{dateFormat(records[record.id].finished_at)}</span>
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
    {
      title: "Actions",
      key: "action",
      fixed: "right",
      render: (_, record: any) => (
        <>
          {records && records[record.id] ? (
            <PerButton
              type="link"
              text="Reset"
              class="b-link c-red"
              icon={null}
              p="user-learn-destroy"
              onClick={() => {
                clearSingleProgress(records[record.id].hour_id);
              }}
              disabled={null}
            />
          ) : (
            <span>-</span>
          )}
        </>
      ),
    },
  ];

  const clearProgress = () => {
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Reset learning records for all lessons in this course?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        member.destroyAllUserLearned(uid, id).then((res: any) => {
          message.success("Done");
          setRefresh(!refresh);
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const clearSingleProgress = (hour_id: number) => {
    if (hour_id === 0) {
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Reset the learning record for this lesson?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        member.destroyUserLearned(uid, id, hour_id).then((res: any) => {
          message.success("Done");
          setRefresh(!refresh);
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  return (
    <>
      {open ? (
        <Modal
          title="Lesson Progress"
          centered
          forceRender
          open={true}
          width={1000}
          onOk={() => onCancel()}
          onCancel={() => onCancel()}
          maskClosable={false}
          footer={null}
        >
          <div className="mt-24">
            <PerButton
              type="primary"
              text="Reset Learning Records"
              class="c-white"
              icon={null}
              p="user-learn-destroy"
              onClick={() => {
                clearProgress();
              }}
              disabled={null}
            />
          </div>
          <div className="mt-24" style={{ maxHeight: 800, overflowY: "auto" }}>
            <Table
              columns={column}
              dataSource={list}
              loading={loading}
              rowKey={(record) => record.id}
              pagination={false}
            />
          </div>
        </Modal>
      ) : null}
    </>
  );
};
