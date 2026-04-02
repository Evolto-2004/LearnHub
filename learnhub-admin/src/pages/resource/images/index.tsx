import { useEffect, useState } from "react";
import {
  Spin,
  Button,
  Row,
  Col,
  Modal,
  Image,
  Empty,
  message,
  Pagination,
} from "antd";
import { resource } from "../../../api";
import { useLocation } from "react-router-dom";
import styles from "./index.module.less";
import { UploadImageSub } from "../../../compenents/upload-image-button/upload-image-sub";
import { TreeCategory } from "../../../compenents";
import { ExclamationCircleFilled, CheckOutlined } from "@ant-design/icons";

const { confirm } = Modal;

interface ImageItem {
  id: number;
  category_id: number;
  name: string;
  extension: string;
  size: number;
  disk: string;
  file_id: string;
  path: string;
  url: string;
  created_at: string;
}

const ResourceImagesPage = () => {
  const result = new URLSearchParams(useLocation().search);
  const [imageList, setImageList] = useState<ImageItem[]>([]);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [refresh, setRefresh] = useState(false);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(32);
  const [total, setTotal] = useState(0);
  const [category_ids, setCategoryIds] = useState<number[]>([]);
  const [selectKey, setSelectKey] = useState<number[]>([]);
  const [visibleArr, setVisibleArr] = useState<boolean[]>([]);
  const [hoverArr, setHoverArr] = useState<boolean[]>([]);
  const [selLabel, setLabel] = useState<string>(
    result.get("label") ? String(result.get("label")) : "All Images"
  );
  const [loading, setLoading] = useState(false);
  const [cateId, setCateId] = useState(Number(result.get("cid")));

  useEffect(() => {
    setCateId(Number(result.get("cid")));
    if (Number(result.get("cid"))) {
      let arr = [];
      arr.push(Number(result.get("cid")));
      setCategoryIds(arr);
    }
  }, [result.get("cid")]);

  // 加载Image列表
  useEffect(() => {
    getImageList();
  }, [category_ids, refresh, page, size]);

  // DeleteImage
  const removeResource = () => {
    if (selectKey.length === 0) {
      return;
    }
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Confirm deletion of the selected images?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        resource.destroyResourceMulti(selectKey).then(() => {
          message.success("Deleted successfully");
          resetImageList();
        });
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  // 获取Image列表
  const getImageList = () => {
    setLoading(true);
    let categoryIds = category_ids.join(",");
    resource
      .resourceList(page, size, "", "", "", "IMAGE", categoryIds)
      .then((res: any) => {
        setTotal(res.data.result.total);
        setImageList(res.data.result.data);
        setResourceUrl(res.data.resource_url);
        let data: ImageItem[] = res.data.result.data;
        let arr = [];
        for (let i = 0; i < data.length; i++) {
          arr.push(false);
        }
        setVisibleArr(arr);
        setHoverArr(arr);
        setLoading(false);
      })
      .catch((err: any) => {
        setLoading(false);
        console.log("Error,", err);
      });
  };
  // Reset列表
  const resetImageList = () => {
    setPage(1);
    setImageList([]);
    setSelectKey([]);
    setRefresh(!refresh);
  };

  const onChange = (e: any, id: number) => {
    e.preventDefault();
    e.stopPropagation();
    const arr = [...selectKey];
    if (arr.indexOf(id) === -1) {
      arr.push(id);
    } else {
      arr.splice(arr.indexOf(id), 1);
    }
    setSelectKey(arr);
  };

  const selectAll = () => {
    let arr = [];
    for (let i = 0; i < imageList.length; i++) {
      arr.push(imageList[i].id);
    }
    setSelectKey(arr);
  };

  const cancelAll = () => {
    setSelectKey([]);
  };

  const showImage = (index: number, value: boolean) => {
    const arr = [...visibleArr];
    arr[index] = value;
    setVisibleArr(arr);
  };

  const showHover = (index: number, value: boolean) => {
    const arr = [...hoverArr];
    for (let i = 0; i < arr.length; i++) {
      arr[i] = false;
    }
    arr[index] = value;
    setHoverArr(arr);
  };

  return (
    <>
      <div className="tree-main-body">
        <div className="left-box">
          <TreeCategory
            selected={category_ids}
            type="no-cate"
            text={"Image"}
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
            Image | {selLabel}
          </div>
          <Row gutter={16} style={{ marginBottom: 24 }}>
            <Col span={24}>
              <div className="j-b-flex">
                <div className="d-flex">
                  <UploadImageSub
                    categoryIds={category_ids}
                    onUpdate={() => {
                      resetImageList();
                    }}
                  ></UploadImageSub>
                  {selectKey.length > 0 && (
                    <Button className="ml-16" onClick={() => cancelAll()}>
                      Cancelled
                    </Button>
                  )}
                  {selectKey.length === 0 && (
                    <Button className="ml-16" onClick={() => selectAll()}>
                      Bulk Actions
                    </Button>
                  )}
                  {imageList.length !== 0 && (
                    <Button
                      className="ml-16"
                      disabled={selectKey.length === 0}
                      type="primary"
                      onClick={() => removeResource()}
                    >
                      Delete
                    </Button>
                  )}
                </div>
                <div className="d-flex"></div>
              </div>
            </Col>
          </Row>
          {loading && (
            <div className="float-left d-j-flex mt-24">
              <Spin size="large" />
            </div>
          )}
          {imageList.length === 0 && (
            <div className="d-flex">
              <Col span={24}>
                <Empty description="No images yet" />
              </Col>
            </div>
          )}
          <div className={styles["images-box"]}>
            {imageList.map((item: any, index: number) => (
              <div
                key={item.id}
                className={`${styles.imageItem} ref-image-item`}
                style={{ backgroundImage: `url(${resourceUrl[item.id]})` }}
                onClick={() => showImage(index, true)}
                onMouseOver={() => showHover(index, true)}
                onMouseOut={() => showHover(index, false)}
              >
                {hoverArr[index] && (
                  <i
                    className={styles.checkbox}
                    onClick={(e) => onChange(e, item.id)}
                  ></i>
                )}
                {selectKey.indexOf(item.id) !== -1 && (
                  <i
                    className={styles.checked}
                    onClick={(e) => onChange(e, item.id)}
                  >
                    <CheckOutlined />
                  </i>
                )}
                <Image
                  width={200}
                  style={{ display: "none" }}
                  src={resourceUrl[item.id]}
                  preview={{
                    visible: visibleArr[index],
                    src: resourceUrl[item.id],
                    onVisibleChange: (value) => {
                      showImage(index, value);
                    },
                  }}
                />
              </div>
            ))}
          </div>
          {imageList.length > 0 && (
            <Col
              span={24}
              style={{ display: "flex", flexDirection: "row-reverse" }}
            >
              <Pagination
                onChange={(currentPage, currentSize) => {
                  setPage(currentPage);
                  setSize(currentSize);
                }}
                defaultCurrent={page}
                total={total}
                pageSize={size}
              />
            </Col>
          )}
        </div>
      </div>
    </>
  );
};

export default ResourceImagesPage;
