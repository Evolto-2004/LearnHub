import React, { useState, useEffect } from "react";
import { Modal, Form, Input, message, Spin } from "antd";
import { resource } from "../../../../../api/index";

interface PropInterface {
  id: number;
  open: boolean;
  onCancel: () => void;
  onSuccess: () => void;
}

export const CoursewareUpdateDialog: React.FC<PropInterface> = ({
  id,
  open,
  onCancel,
  onSuccess,
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState<boolean>(true);
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
    setLoading(true);
    resource
      .videoUpdate(id, values)
      .then((res: any) => {
        setLoading(false);
        message.success("Saved successfully!");
        onSuccess();
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <>
      {open ? (
        <Modal
          title="EditCourseware"
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
                label="Courseware Title"
                name="name"
                rules={[{ required: true, message: "Please enter the courseware title!" }]}
              >
                <Input
                  allowClear
                  style={{ width: 200 }}
                  placeholder="Please enter the courseware title"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
