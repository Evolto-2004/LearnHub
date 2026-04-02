import { useEffect, useState } from "react";
import { Table, Typography, Input, Button, DatePicker } from "antd";
import { adminLog } from "../../../api";
import type { ColumnsType } from "antd/es/table";
import {
  dateWholeFormat,
  transUtcTime,
  translateAdminIdentity,
  translateDisplayText,
} from "../../../utils/index";
import { AdminLogDetailDialog } from "./compenents/detail-dialog";
const { RangePicker } = DatePicker;
import moment from "moment";

interface DataType {
  id: React.Key;
  admin_id: number;
  admin_name: string;
  created_at: string;
  error_msg?: string;
  ip: string;
  ip_area: string;
  method: string;
  module: string;
  opt: number;
  param: string;
  request_method: string;
  result: string;
  title: string;
  url: string;
}

const SystemLogPage = () => {
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [list, setList] = useState<DataType[]>([]);
  const [total, setTotal] = useState(0);
  const [refresh, setRefresh] = useState(false);
  const [title, setTitle] = useState("");
  const [adminId, setAdminId] = useState("");
  const [adminName, setAdminName] = useState("");
  const [created_at, setCreatedAt] = useState<string[]>([]);
  const [createdAts, setCreatedAts] = useState<any>([]);
  const [visiable, setVisiable] = useState(false);
  const [admId, setAdmId] = useState(0);

  useEffect(() => {
    getData();
  }, [refresh, page, size]);

  const getData = () => {
    setLoading(true);
    adminLog
      .adminLogList(
        page,
        size,
        adminName,
        title,
        "",
        created_at[0],
        created_at[1]
      )
      .then((res: any) => {
        setList(res.data.data);
        setTotal(res.data.total);
        setLoading(false);
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const resetData = () => {
    setTitle("");
    setAdminId("");
    setAdminName("");
    setPage(1);
    setSize(10);
    setList([]);
    setCreatedAts([]);
    setCreatedAt([]);
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

  const disabledDate = (current: any) => {
    return current && current >= moment().add(0, "days"); // 选择Time要大于等于当前天。若今天不能被选择，去掉等号即可。
  };

  const columns: ColumnsType<DataType> = [
    {
      title: "Administrator Name",
      width: 150,
      render: (_, record: any) => (
        <span>{translateAdminIdentity(record.admin_name)}</span>
      ),
    },
    {
      title: "Action",
      render: (_, record: any) => (
        <span>{translateDisplayText(record.title)}</span>
      ),
    },
    {
      title: "IP",
      width: 250,
      dataIndex: "ip",
      render: (ip: string) => <span>{ip}</span>,
    },
    {
      title: "Time",
      width: 200,
      dataIndex: "created_at",
      render: (created_at: string) => (
        <span>{dateWholeFormat(created_at)}</span>
      ),
    },
    {
      title: "Details",
      key: "action",
      fixed: "right",
      width: 160,
      render: (_, record) => (
        <Button
          type="link"
          className="b-link c-red"
          onClick={() => {
            setAdmId(Number(record.id));
            setVisiable(true);
          }}
        >
          Details
        </Button>
      ),
    },
  ];

  return (
    <div className="learnhub-main-body">
      <div className="float-left j-b-flex mb-24">
        <div className="d-flex"></div>
        <div className="d-flex">
          <div className="d-flex mr-24">
            <Typography.Text>Administrator Name:</Typography.Text>
            <Input
              value={adminName}
              onChange={(e) => {
                setAdminName(e.target.value);
              }}
              allowClear
              style={{ width: 160 }}
              placeholder="Please enter the administrator name"
            />
          </div>
          <div className="d-flex mr-24">
            <Typography.Text>Action:</Typography.Text>
            <Input
              value={title}
              onChange={(e) => {
                setTitle(e.target.value);
              }}
              allowClear
              style={{ width: 160 }}
              placeholder="Enter an action keyword"
            />
          </div>
          <div className="d-flex mr-24">
            <Typography.Text>Time:</Typography.Text>
            <RangePicker
              disabledDate={disabledDate}
              format={"YYYY-MM-DD"}
              value={createdAts}
              style={{ marginLeft: 10 }}
              onChange={(date, dateString) => {
                let date1 = dateString[0] + " 00:00:00";
                let date2 = dateString[1] + " 23:59:59";
                dateString[0] = transUtcTime(date1);
                dateString[1] = transUtcTime(date2);
                setCreatedAt(dateString);
                setCreatedAts(date);
              }}
              placeholder={["Start Time", "End Time"]}
            />
          </div>
          <div className="d-flex">
            <Button className="mr-16" onClick={resetData}>
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
          loading={loading}
          columns={columns}
          dataSource={list}
          rowKey={(record) => record.id}
          pagination={paginationProps}
        />
      </div>
      <AdminLogDetailDialog
        id={admId}
        open={visiable}
        onCancel={() => setVisiable(false)}
      ></AdminLogDetailDialog>
    </div>
  );
};

export default SystemLogPage;
