import { useEffect, useState } from "react";
import {
  Modal,
  Table,
  message,
  Space,
  Typography,
  Input,
  Select,
  Button,
} from "antd";
import { resource } from "../../../api";
import { useLocation } from "react-router-dom";
import { ExclamationCircleFilled } from "@ant-design/icons";
import type { ColumnsType } from "antd/es/table";
import { dateFormat, translateAdminIdentity } from "../../../utils/index";
import { TreeCategory, UploadCoursewareButton } from "../../../compenents";
import { CoursewareUpdateDialog } from "./compenents/update-dialog";

const { confirm } = Modal;

interface DataType {
  admin_id: number;
  created_at: string;
  disk: string;
  extension: string;
  file_id: string;
  id: React.Key;
  name: string;
  parent_id: number;
  path: string;
  size: number;
  type: string;
  url: string;
}

type AdminUsersModel = {
  [key: number]: string;
};

const ResourceCoursewarePage = () => {
  const result = new URLSearchParams(useLocation().search);
  const [list, setList] = useState<DataType[]>([]);
  const [adminUsers, setAdminUsers] = useState<AdminUsersModel>({});
  const [existingTypes, setExistingTypes] = useState<string[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [refresh, setRefresh] = useState(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [category_ids, setCategoryIds] = useState<number[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<any>([]);
  const [type, setType] = useState("WORD,EXCEL,PPT,PDF,TXT,RAR,ZIP");
  const [title, setTitle] = useState("");
  const [multiConfig, setMultiConfig] = useState(false);
  const [selLabel, setLabel] = useState<string>(
    result.get("label") ? String(result.get("label")) : "All Courseware"
  );
  const [cateId, setCateId] = useState(Number(result.get("cid")));
  const [updateId, setUpdateId] = useState(0);
  const [updateVisible, setUpdateVisible] = useState(false);
  const types = [
    { label: "All", value: "WORD,EXCEL,PPT,PDF,TXT,RAR,ZIP" },
    { label: "WORD", value: "WORD" },
    { label: "EXCEL", value: "EXCEL" },
    { label: "PPT", value: "PPT" },
    { label: "PDF", value: "PDF" },
    { label: "TXT", value: "TXT" },
    { label: "RAR", value: "RAR" },
    { label: "ZIP", value: "ZIP" },
  ];

  useEffect(() => {
    setCateId(Number(result.get("cid")));
    if (Number(result.get("cid"))) {
      let arr = [];
      arr.push(Number(result.get("cid")));
      setCategoryIds(arr);
    }
  }, [result.get("cid")]);

  // 加载Courseware列表
  useEffect(() => {
    getList();
  }, [category_ids, refresh, page, size]);

  const getList = () => {
    setLoading(true);
    let categoryIds = category_ids.join(",");
    resource
      .resourceList(page, size, "", "", title, type, categoryIds)
      .then((res: any) => {
        setTotal(res.data.result.total);
        setList(res.data.result.data);
        setExistingTypes(res.data.existing_types);
        setAdminUsers(res.data.admin_users);
        setResourceUrl(res.data.resource_url);
        setLoading(false);
      })
      .catch((err: any) => {
        console.log("Error,", err);
      });
  };

  const columns: ColumnsType<DataType> = [
    {
      title: "Courseware Title",
      render: (_, record: any) => (
        <div className="d-flex">
          <i
            className="iconfont icon-icon-file"
            style={{
              fontSize: 16,
              color: "rgba(0,0,0,0.3)",
            }}
          />
          <span className="ml-8">
            {record.name}.{record.extension}
          </span>
        </div>
      ),
    },
    {
      title: "Format",
      dataIndex: "type",
      render: (type: string) => <span>{type}</span>,
    },
    {
      title: "Courseware Size",
      dataIndex: "size",
      render: (size: number) => <span>{(size / 1024 / 1024).toFixed(2)}M</span>,
    },
    {
      title: "Created By",
      dataIndex: "admin_id",
      render: (text: number) =>
        JSON.stringify(adminUsers) !== "{}" && (
          <span>{translateAdminIdentity(adminUsers[text])}</span>
        ),
    },
    {
      title: "Created At",
      dataIndex: "created_at",
      render: (text: string) => <span>{dateFormat(text)}</span>,
    },
    {
      title: "Actions",
      key: "action",
      fixed: "right",
      width: 180,
      render: (_, record: any) => {
        return (
          <Space size="small">
            <Button
              type="link"
              size="small"
              className="b-n-link c-red"
              onClick={() => {
                downLoadFile(
                  resourceUrl[record.id],
                  record.name,
                  record.extension
                );
              }}
            >
              Download
            </Button>
            <div className="form-column"></div>
            <Button
              type="link"
              className="b-link c-red"
              onClick={() => {
                setUpdateId(record.id);
                setUpdateVisible(true);
              }}
            >
              Edit
            </Button>
            <div className="form-column"></div>
            <Button
              type="link"
              className="b-link c-red"
              onClick={() => removeResource(record.id)}
            >
              Delete
            </Button>
          </Space>
        );
      },
    },
  ];

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

  const rowSelection = {
    onChange: (selectedRowKeys: React.Key[], selectedRows: DataType[]) => {
      setSelectedRowKeys(selectedRowKeys);
    },
  };

  // Reset列表
  const resetList = () => {
    setPage(1);
    setSize(10);
    setList([]);
    setTitle("");
    setSelectedRowKeys([]);
    setType("WORD,EXCEL,PPT,PDF,TXT,RAR,ZIP");
    setRefresh(!refresh);
  };

  // DeleteCourseware
  const removeResource = (id: number) => {
    if (id === 0) {
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Make sure the selected courseware files are not linked to any course before deleting. Continue?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        resource.destroyResource(id).then(() => {
          message.success("Deleted successfully");
          resetList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  // 批量DeleteCourseware
  const removeResourceMulti = () => {
    if (selectedRowKeys.length === 0) {
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Make sure the selected courseware files are not linked to any course before deleting. Continue?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        resource.destroyResourceMulti(selectedRowKeys).then(() => {
          message.success("Deleted successfully");
          resetList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const downLoadFile = (url: string, name: string, extension: string) => {
    window.open(url);
    const a = document.createElement("a");
    a.style.display = "none";
    a.href = url;
    a.download = `${name}.${extension}`; // 设置Download的文件名
    document.body.appendChild(a);
    a.click(); // 触发点击事件
    // 释放 URL 对象
    URL.revokeObjectURL(url);
    document.body.removeChild(a);
  };

  return (
    <>
      <div className="tree-main-body">
        <div className="left-box">
          <TreeCategory
            selected={category_ids}
            type="no-cate"
            text={"Courseware"}
            onUpdate={(keys: any, title: any) => {
              setPage(1);
              setCategoryIds(keys);
              if (typeof title === "string") {
                setLabel(title);
              } else {
                setLabel(title.props.children[0]);
              }
            }}
          />
        </div>
        <div className="right-box">
          <div className="d-flex learnhub-main-title float-left mb-24">
            Courseware | {selLabel}
          </div>
          <div className="float-left  j-b-flex  mb-24">
            <div>
              <UploadCoursewareButton
                categoryIds={category_ids}
                onUpdate={() => {
                  resetList();
                }}
              ></UploadCoursewareButton>
              <Button
                type="default"
                className="ml-16"
                onClick={() => {
                  setSelectedRowKeys([]);
                  setMultiConfig(!multiConfig);
                }}
              >
                {multiConfig ? "Cancelled" : "Bulk Actions"}
              </Button>
              <Button
                type="default"
                className="ml-16"
                onClick={() => removeResourceMulti()}
                disabled={selectedRowKeys.length === 0}
              >
                Delete
              </Button>
            </div>
            <div className="d-flex">
              <div className="d-flex">
                <div className="d-flex mr-24">
                  <Typography.Text>Name:</Typography.Text>
                  <Input
                    value={title}
                    onChange={(e) => {
                      setTitle(e.target.value);
                    }}
                    allowClear
                    style={{ width: 160 }}
                    placeholder="Enter a name keyword"
                  />
                </div>
                <div className="d-flex mr-24">
                  <Typography.Text>Format：</Typography.Text>
                  <Select
                    style={{ width: 160 }}
                    placeholder="Select a format"
                    value={type}
                    onChange={(value: string) => setType(value)}
                    options={types}
                  />
                </div>
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
            {multiConfig ? (
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
            ) : (
              <Table
                columns={columns}
                dataSource={list}
                loading={loading}
                pagination={paginationProps}
                rowKey={(record) => record.id}
              />
            )}
          </div>
        </div>
        <CoursewareUpdateDialog
          id={Number(updateId)}
          open={updateVisible}
          onCancel={() => setUpdateVisible(false)}
          onSuccess={() => {
            setUpdateVisible(false);
            setRefresh(!refresh);
          }}
        ></CoursewareUpdateDialog>
      </div>
    </>
  );
};

export default ResourceCoursewarePage;
