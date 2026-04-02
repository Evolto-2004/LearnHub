import React, { useState, useEffect } from "react";
import { Button, Drawer, Form, Modal, message, Spin } from "antd";
import styles from "./hour-update.module.less";
import { course, courseAttachment } from "../../../api/index";
import { SelectAttachment } from "../../../compenents";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { TreeAttachments } from "./attachments";

const { confirm } = Modal;

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
}

export const CourseAttachmentUpdate: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [attachmentVisible, setAttachmentVisible] = useState<boolean>(false);
  const [attachmentData, setAttachmentData] = useState<AttachmentDataModel[]>(
    []
  );
  const [attachments, setAttachments] = useState<number[]>([]);

  useEffect(() => {
    setInit(true);
    if (id === 0) {
      return;
    }
    setAttachmentVisible(false);
    setAttachmentData([]);
    setAttachments([]);
    getDetail();
  }, [id, open]);

  const getDetail = () => {
    course.course(id).then((res: any) => {
      let treeData = res.data.attachments;
      if (treeData.length > 0) {
        const arr: any = resetAttachments(treeData).arr;
        const keys: any = resetAttachments(treeData).keys;
        setAttachmentData(arr);
        setAttachments(keys);
      }
      setInit(false);
    });
  };

  const resetAttachments = (data: any) => {
    const arr: any = [];
    const keys: any = [];
    if (data) {
      for (let i = 0; i < data.length; i++) {
        arr.push({
          type: data[i].type,
          name: data[i].title,
          rid: data[i].rid,
          id: data[i].id,
        });
        keys.push(data[i].rid);
      }
    }
    return { arr, keys };
  };

  const onFinish = (values: any) => {};

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const selectAttachmentData = (arr: any, videos: any) => {
    const hours: any = [];
    for (let i = 0; i < videos.length; i++) {
      hours.push({
        sort: attachmentData.length + i,
        title: videos[i].name,
        type: videos[i].type,
        rid: videos[i].rid,
      });
    }
    if (hours.length === 0) {
      message.error("Select courseware");
      return;
    }

    courseAttachment.storeCourseAttachmentMulti(id, hours).then((res: any) => {
      console.log("ok");
      setAttachmentVisible(false);
      getDetail();
    });
  };

  const delAttachments = (hid: number) => {
    const data = [...attachmentData];
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Confirm deletion of this courseware?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        const index = data.findIndex((i: any) => i.rid === hid);
        let delId = data[index].id;
        if (index >= 0) {
          data.splice(index, 1);
        }
        if (data.length > 0) {
          setAttachmentData(data);
          const keys = data.map((item: any) => item.rid);
          setAttachments(keys);
        } else {
          setAttachmentData([]);
          setAttachments([]);
        }
        if (delId) {
          courseAttachment.destroyAttachment(id, delId).then((res: any) => {
            console.log("ok");
          });
        }
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const transAttachments = (arr: any) => {
    setAttachments(arr);
    const data = [...attachmentData];
    const newArr: any = [];
    const hourIds: any = [];
    for (let i = 0; i < arr.length; i++) {
      data.map((item: any) => {
        if (item.rid === arr[i]) {
          newArr.push(item);
          hourIds.push(item.id);
        }
      });
    }
    setAttachmentData(newArr);
    courseAttachment.transCourseAttachment(id, hourIds).then((res: any) => {
      console.log("ok");
    });
  };

  return (
    <>
      {open ? (
        <Drawer
          title="Courseware"
          onClose={onCancel}
          maskClosable={false}
          open={true}
          width={634}
        >
          <div className={styles["top-content"]}>
            <p>1. Courseware changes take effect immediately and cannot be undone. Proceed carefully.</p>
          </div>
          <div className="float-left mt-24">
            <SelectAttachment
              defaultKeys={attachments}
              open={attachmentVisible}
              onCancel={() => {
                setAttachmentVisible(false);
              }}
              onSelected={(arr: any, videos: any) => {
                selectAttachmentData(arr, videos);
              }}
            ></SelectAttachment>
            {init && (
              <div className="float-left text-center mt-30">
                <Spin></Spin>
              </div>
            )}
            <div
              className="float-left"
              style={{ display: init ? "none" : "block" }}
            >
              <Form
                form={form}
                name="attachment-update-basic"
                labelCol={{ span: 5 }}
                wrapperCol={{ span: 19 }}
                initialValues={{ remember: true }}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                autoComplete="off"
              >
                <div className="c-flex">
                  <Form.Item>
                    <div className="ml-42">
                      <Button
                        onClick={() => setAttachmentVisible(true)}
                        type="primary"
                      >
                        Add Courseware
                      </Button>
                    </div>
                  </Form.Item>
                  <div className={styles["hous-box"]}>
                    {attachmentData.length === 0 && (
                      <span className={styles["no-hours"]}>
                        Click the button above to add courseware
                      </span>
                    )}
                    {attachmentData.length > 0 && (
                      <TreeAttachments
                        data={attachmentData}
                        onRemoveItem={(id: number) => {
                          delAttachments(id);
                        }}
                        onUpdate={(arr: any[]) => {
                          transAttachments(arr);
                        }}
                      />
                    )}
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </Drawer>
      ) : null}
    </>
  );
};
