import React, { useState, useEffect } from "react";
import { Modal, Select, Switch, Form, Input, message, Spin } from "antd";
import styles from "./create.module.less";
import { adminUser } from "../../../../api/index";
import { translateAdminIdentity } from "../../../../utils/index";

interface PropInterface {
  roleId: number;
  refresh: boolean;
  open: boolean;
  onCancel: () => void;
}

type selRoleModel = {
  label: string;
  value: number;
};

export const SystemAdministratorCreate: React.FC<PropInterface> = ({
  roleId,
  refresh,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [loading, setLoading] = useState(false);
  const [roles, setRoles] = useState<selRoleModel[]>([]);

  useEffect(() => {
    setInit(true);
    if (open) {
      getParams();
    }
  }, [refresh, open]);

  useEffect(() => {
    let roleIds = [];
    if (roleId) {
      roleIds.push(roleId);
    }
    form.setFieldsValue({
      email: "",
      name: "",
      password: "",
      is_ban_login: 0,
      roleIds: roleIds,
    });
  }, [form, open, roleId]);

  const getParams = () => {
    adminUser.createAdminUser().then((res: any) => {
      const arr = [];
      let roles: RoleModel[] = res.data.roles;
      for (let i = 0; i < roles.length; i++) {
        arr.push({
          label: translateAdminIdentity(roles[i].name),
          value: roles[i].id,
        });
      }
      setRoles(arr);
      setInit(false);
    });
  };

  const onFinish = (values: any) => {
    if (loading) {
      return;
    }
    setLoading(true);
    adminUser
      .storeAdminUser(
        values.name,
        values.email,
        values.password,
        values.is_ban_login,
        values.roleIds
      )
      .then((res: any) => {
        setLoading(false);
        message.success("Saved successfully!");
        onCancel();
      })
      .catch((e) => {
        setLoading(false);
      });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const handleChange = (value: any) => {};

  const onChange = (checked: boolean) => {
    if (checked) {
      form.setFieldsValue({ is_ban_login: 1 });
    } else {
      form.setFieldsValue({ is_ban_login: 0 });
    }
  };

  return (
    <>
      {open ? (
        <Modal
          title="Add Administrator"
          centered
          forceRender
          open={true}
          width={416}
          onOk={() => form.submit()}
          onCancel={() => onCancel()}
          maskClosable={false}
          okButtonProps={{ loading: loading }}
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
              name="basic"
              labelCol={{ span: 8 }}
              wrapperCol={{ span: 16 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="Select Role"
                name="roleIds"
                rules={[{ required: true, message: "Please select a role!" }]}
              >
                <Select
                  style={{ width: 200 }}
                  mode="multiple"
                  allowClear
                  placeholder="Select a role"
                  onChange={handleChange}
                  options={roles}
                />
              </Form.Item>
              <Form.Item
                label="Administrator Name"
                name="name"
                rules={[{ required: true, message: "Please enter the administrator name!" }]}
              >
                <Input
                  allowClear
                  style={{ width: 200 }}
                  placeholder="Please enter the administrator name"
                />
              </Form.Item>
              <Form.Item
                label="Email"
                name="email"
                rules={[{ required: true, message: "Please enter the administrator email!" }]}
              >
                <Input
                  allowClear
                  style={{ width: 200 }}
                  placeholder="Please enter the administrator email"
                />
              </Form.Item>
              <Form.Item
                label="Password"
                name="password"
                rules={[{ required: true, message: "Please enter the login password!" }]}
              >
                <Input.Password
                  autoComplete="new-password"
                  allowClear
                  style={{ width: 200 }}
                  placeholder="Please enter the login password"
                />
              </Form.Item>

              <Form.Item
                label="Disable Sign-in"
                name="is_ban_login"
                valuePropName="checked"
              >
                <Switch onChange={onChange} />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
