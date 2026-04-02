import React, { useState, useEffect } from "react";
import { Modal, Form, Input, message } from "antd";
import styles from "./index.module.less";
import { user } from "../../api/index";

interface PropInterface {
  open: boolean;
  onCancel: () => void;
}

export const ChangePasswordModel: React.FC<PropInterface> = ({
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    form.setFieldsValue({
      old_password: "",
      new_password: "",
      again_new_password: "",
    });
  }, [form, open]);

  const onFinish = (values: any) => {
    if (values.again_new_password !== values.new_password) {
      message.error("The password confirmation does not match");
      return;
    }
    user.password(values.old_password, values.new_password).then((res: any) => {
      message.success("Saved successfully!");
      onCancel();
    });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <>
      {open ? (
        <Modal
          title="Change Password"
          centered
          forceRender
          open={true}
          width={416}
          onOk={() => form.submit()}
          onCancel={() => onCancel()}
          maskClosable={false}
        >
          <div className="float-left mt-24">
            <Form
              form={form}
              name="change-password"
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 16 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="Please enter your current password"
                name="old_password"
                rules={[{ required: true, message: "Please enter your current password!" }]}
              >
                <Input.Password
                  style={{ width: 200 }}
                  autoComplete="off"
                  placeholder="Please enter your current password"
                />
              </Form.Item>
              <Form.Item
                label="Enter a new password"
                name="new_password"
                rules={[{ required: true, message: "Please enter a new password!" }]}
              >
                <Input.Password
                  style={{ width: 200 }}
                  autoComplete="off"
                  placeholder="Please enter a new password"
                />
              </Form.Item>
              <Form.Item
                label="Confirm the new password"
                name="again_new_password"
                rules={[{ required: true, message: "Please confirm the new password!" }]}
              >
                <Input.Password
                  style={{ width: 200 }}
                  autoComplete="off"
                  placeholder="Confirm the new password"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
