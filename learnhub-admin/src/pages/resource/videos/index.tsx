import { useEffect, useState } from "react";
import {
  Modal,
  Table,
  message,
  Space,
  Dropdown,
  Typography,
  Input,
  Button,
} from "antd";
import type { MenuProps } from "antd";
import { resource } from "../../../api";
import { useLocation } from "react-router-dom";
import { DownOutlined, ExclamationCircleFilled } from "@ant-design/icons";
import type { ColumnsType } from "antd/es/table";
import { dateFormat, translateAdminIdentity } from "../../../utils/index";
import { TreeCategory, DurationText } from "../../../compenents";
import { UploadVideoButton } from "../../../compenents/upload-video-button";
import { VideoPlayDialog } from "./compenents/video-play-dialog";
import { VideosUpdateDialog } from "./compenents/update-dialog";

const { confirm } = Modal;

interface DataType {
  id: React.Key;
  admin_id: number;
  created_at: string;
  disk: string;
  extension: string;
  file_id: string;
  name: string;
  parent_id: number;
  path: string;
  size: number;
  type: string;
  url: string;
}

type VideosExtraModel = {
  [key: number]: VideoModel;
};

type VideoModel = {
  duration: number;
  poster: string;
  rid: number;
};

type AdminUsersModel = {
  [key: number]: string;
};

const ResourceVideosPage = () => {
  const result = new URLSearchParams(useLocation().search);
  const [videoList, setVideoList] = useState<DataType[]>([]);
  const [videosExtra, setVideoExtra] = useState<VideosExtraModel>({});
  const [adminUsers, setAdminUsers] = useState<AdminUsersModel>({});
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [refresh, setRefresh] = useState(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [category_ids, setCategoryIds] = useState<number[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<any>([]);
  const [selLabel, setLabel] = useState<string>(
    result.get("label") ? String(result.get("label")) : "All Videos"
  );
  const [cateId, setCateId] = useState(Number(result.get("cid")));
  const [updateVisible, setUpdateVisible] = useState(false);
  const [playVisible, setPlayeVisible] = useState(false);
  const [multiConfig, setMultiConfig] = useState(false);
  const [updateId, setUpdateId] = useState(0);
  const [playUrl, setPlayUrl] = useState("");
  const [title, setTitle] = useState("");

  useEffect(() => {
    setCateId(Number(result.get("cid")));
    if (Number(result.get("cid"))) {
      let arr = [];
      arr.push(Number(result.get("cid")));
      setCategoryIds(arr);
    }
  }, [result.get("cid")]);

  const columns: ColumnsType<DataType> = [
    {
      title: "Video Title",
      dataIndex: "name",
      render: (text: string) => (
        <div className="d-flex">
          <i
            className="iconfont icon-icon-video"
            style={{
              fontSize: 16,
              color: "rgba(0,0,0,0.3)",
            }}
          />
          <span className="ml-8">{text}</span>
        </div>
      ),
    },
    {
      title: "Video Duration",
      dataIndex: "id",
      render: (id: number) => (
        <DurationText duration={videosExtra[id].duration}></DurationText>
      ),
    },
    {
      title: "Created By",
      dataIndex: "admin_id",
      render: (admin_id: number) =>
        JSON.stringify(adminUsers) !== "{}" && (
          <span>{translateAdminIdentity(adminUsers[admin_id])}</span>
        ),
    },
    {
      title: "Created At",
      dataIndex: "created_at",
      render: (created_at: string) => <span>{dateFormat(created_at)}</span>,
    },
    {
      title: "Actions",
      key: "action",
      fixed: "right",
      width: 160,
      render: (_, record: any) => {
        const items: MenuProps["items"] = [
          {
            key: "1",
            label: (
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
            ),
          },
          {
            key: "2",
            label: (
              <Button
                type="link"
                className="b-link c-red"
                onClick={() => removeResource(record.id)}
              >
                Delete
              </Button>
            ),
          },
        ];

        return (
          <Space size="small">
            <Button
              type="link"
              size="small"
              className="b-n-link c-red"
              onClick={() => {
                setUpdateId(record.id);
                setPlayUrl(resourceUrl[record.id]);
                setPlayeVisible(true);
              }}
            >
              Preview
            </Button>
            <div className="form-column"></div>
            <Dropdown menu={{ items }}>
              <Button
                type="link"
                className="b-link c-red"
                onClick={(e) => e.preventDefault()}
              >
                <Space size="small" align="center">
                  More
                  <DownOutlined />
                </Space>
              </Button>
            </Dropdown>
          </Space>
        );
      },
    },
  ];

  // DeleteImage
  const removeResource = (id: number) => {
    if (id === 0) {
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Make sure the selected video files are not linked to any course before deleting. Continue?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        resource.destroyResource(id).then(() => {
          message.success("Deleted successfully");
          resetVideoList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const removeResourceMulti = () => {
    if (selectedRowKeys.length === 0) {
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Make sure the selected video files are not linked to any course before deleting. Continue?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        resource.destroyResourceMulti(selectedRowKeys).then(() => {
          message.success("Deleted successfully");
          resetVideoList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  // 获取Video列表
  const getVideoList = () => {
    setLoading(true);
    let categoryIds = category_ids.join(",");
    resource
      .resourceList(page, size, "", "", title, "VIDEO", categoryIds)
      .then((res: any) => {
        setTotal(res.data.result.total);
        setVideoList(res.data.result.data);
        setVideoExtra(res.data.videos_extra);
        setAdminUsers(res.data.admin_users);
        setResourceUrl(res.data.resource_url);
        setLoading(false);
      })
      .catch((err: any) => {
        console.log("Error,", err);
      });
  };

  // Reset列表
  const resetVideoList = () => {
    setPage(1);
    setSize(10);
    setVideoList([]);
    setSelectedRowKeys([]);
    setTitle("");
    setRefresh(!refresh);
  };

  // 加载Video列表
  useEffect(() => {
    getVideoList();
  }, [category_ids, refresh, page, size]);

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

  return (
    <>
      <div className="tree-main-body">
        <div className="left-box">
          <TreeCategory
            selected={category_ids}
            type="no-cate"
            text={"Video"}
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
            Video | {selLabel}
          </div>
          <div className="float-left  j-b-flex  mb-24">
            <div>
              <UploadVideoButton
                categoryIds={category_ids}
                onUpdate={() => {
                  resetVideoList();
                }}
              ></UploadVideoButton>
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
                className="ml-16"
                type="default"
                onClick={() => removeResourceMulti()}
                disabled={selectedRowKeys.length === 0}
              >
                Delete
              </Button>
            </div>
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
              <div className="d-flex">
                <Button className="mr-16" onClick={resetVideoList}>
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
                dataSource={videoList}
                loading={loading}
                pagination={paginationProps}
                rowKey={(record) => record.id}
              />
            ) : (
              <Table
                columns={columns}
                dataSource={videoList}
                loading={loading}
                pagination={paginationProps}
                rowKey={(record) => record.id}
              />
            )}
          </div>
        </div>
        <VideoPlayDialog
          id={Number(updateId)}
          open={playVisible}
          url={playUrl}
          onCancel={() => setPlayeVisible(false)}
        ></VideoPlayDialog>
        <VideosUpdateDialog
          id={Number(updateId)}
          open={updateVisible}
          onCancel={() => setUpdateVisible(false)}
          onSuccess={() => {
            setUpdateVisible(false);
            setRefresh(!refresh);
          }}
        ></VideosUpdateDialog>
      </div>
    </>
  );
};

export default ResourceVideosPage;
