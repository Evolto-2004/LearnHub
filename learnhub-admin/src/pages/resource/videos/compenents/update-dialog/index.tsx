import React, { useState, useEffect } from "react";
import { Modal, Form, Input, message, Spin } from "antd";
import { resource } from "../../../../../api/index";

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
  onSuccess: () => void;
}

export const VideosUpdateDialog: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
  onSuccess,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);

  useEffect(() => {
    setInit(true);
    if (id === 0) {
      return;
    }
    if (open) {
      getDetail();
    }
  }, [form, id, open]);

  const getDetail = () => {
    resource.videoDetail(id).then((res: any) => {
      let data = res.data.resources;
      form.setFieldsValue({
        name: data.name,
      });
      setInit(false);
    });
  };

  const onFinish = (values: any) => {
    resource.videoUpdate(id, values).then((res: any) => {
      message.success("Saved successfully!");
      onSuccess();
    });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <>
      {open ? (
        <Modal
          title="EditVideo"
          centered
          forceRender
          open={true}
          width={416}
          onOk={() => form.submit()}
          onCancel={() => onCancel()}
          maskClosable={false}
        >
          {init && (
            <div className="float-left text-center mt-30">
              <Spin></Spin>
            </div>
          )}
          <div
            className="float-left mt-24"
            style={{ display: init ? "none" : "block" }}
          >
            <Form
              form={form}
              name="videos-update"
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 16 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="Video Title"
                name="name"
                rules={[{ required: true, message: "Please enter the video title!" }]}
              >
                <Input
                  allowClear
                  style={{ width: 200 }}
                  placeholder="Please enter the video title"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
