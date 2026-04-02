import React, { useState, useEffect } from "react";
import { Modal, Form, TreeSelect, Input, message, Spin } from "antd";
import styles from "./create.module.less";
import { useSelector } from "react-redux";
import { user, department } from "../../../api/index";
import { UploadImageButton } from "../../../compenents";
import { ValidataCredentials } from "../../../utils/index";
import memberDefaultAvatar from "../../../assets/thumb/avatar.png";

interface PropInterface {
  open: boolean;
  depIds: any;
  onCancel: () => void;
}

interface Option {
  value: string | number;
  label: string;
  children?: Option[];
}

export const MemberCreate: React.FC<PropInterface> = ({
  open,
  depIds,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [init, setInit] = useState(true);
  const [loading, setLoading] = useState(false);
  const [departments, setDepartments] = useState<any>([]);
  const systemMemberDefaultAvatar = useSelector(
    (state: any) => state.systemConfig.value.memberDefaultAvatar
  );
  const systemResourceUrl = useSelector(
    (state: any) => state.systemConfig.value.resourceUrl
  );
  const [avatar, setAvatar] = useState<string>(memberDefaultAvatar);

  useEffect(() => {
    setInit(true);
    if (open) {
      getParams();
    }
  }, [open]);

  useEffect(() => {
    form.setFieldsValue({
      email: "",
      name: "",
      password: "",
      avatar: systemMemberDefaultAvatar
        ? Number(systemMemberDefaultAvatar)
        : -1,
      idCard: "",
      dep_ids: depIds,
  
    });
    if (systemMemberDefaultAvatar === -1) {
      setAvatar(memberDefaultAvatar);
    } else if (systemMemberDefaultAvatar >= 0) {
      setAvatar(systemResourceUrl[Number(systemMemberDefaultAvatar)]);
    } else {
      setAvatar(memberDefaultAvatar);
    }
  }, [form, open, depIds]);

  const getParams = () => {
    department.departmentList({}).then((res: any) => {
      const departments = res.data.departments;
      if (JSON.stringify(departments) !== "{}") {
        const new_arr: Option[] = checkArr(departments, 0);
        setDepartments(new_arr);
      }
      setInit(false);
    });
  };

  const checkArr = (departments: any[], id: number) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        arr.push({
          label: departments[id][i].name,
          value: departments[id][i].id,
        });
      } else {
        const new_arr: Option[] = checkArr(departments, departments[id][i].id);
        arr.push({
          label: departments[id][i].name,
          value: departments[id][i].id,
          children: new_arr,
        });
      }
    }
    return arr;
  };

  const onFinish = (values: any) => {
    if (loading) {
      return;
    }
    // if (values.idCard !== "" && !ValidataCredentials(values.idCard)) {
    //   message.error("Please enter a valid ID number!");
    //   return;
    // }
    setLoading(true);
    user
      .storeUser(
        values.email,
        values.name,
        values.avatar,
        values.password,
        values.idCard,
        values.dep_ids
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

  return (
    <>
      {open ? (
        <Modal
          title="Add Learner"
          centered
          forceRender
          open={true}
          width={484}
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
              name="create-basic"
              labelCol={{ span: 7 }}
              wrapperCol={{ span: 17 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="Learner Avatar"
                labelCol={{ style: { marginTop: 15, marginLeft: 46 } }}
                name="avatar"
                rules={[{ required: true, message: "Please upload a learner avatar!" }]}
              >
                <div className="d-flex">
                  {avatar && (
                    <img className="form-avatar mr-16" src={avatar} alt="" />
                  )}
                  <div className="d-flex">
                    <UploadImageButton
                      text="Change Avatar"
                      onSelected={(url, id) => {
                        setAvatar(url);
                        form.setFieldsValue({ avatar: id });
                      }}
                    ></UploadImageButton>
                  </div>
                </div>
              </Form.Item>
              <Form.Item
                label="Learner Name"
                name="name"
                rules={[{ required: true, message: "Please enter the learner name!" }]}
              >
                <Input style={{ width: 274 }} placeholder="Enter the learner name" />
              </Form.Item>
              <Form.Item
                label="Login Email"
                name="email"
                rules={[{ required: true, message: "Please enter the login email!" }]}
              >
                <Input
                  autoComplete="off"
                  allowClear
                  style={{ width: 274 }}
                  placeholder="Please enter the learner login email"
                />
              </Form.Item>
              <Form.Item
                label="Password"
                name="password"
                rules={[{ required: true, message: "Please enter the login password!" }]}
              >
                <Input.Password
                  autoComplete="off"
                  allowClear
                  style={{ width: 274 }}
                  placeholder="Please enter the login password"
                />
              </Form.Item>
              <Form.Item
                label="Department"
                name="dep_ids"
                rules={[{ required: true, message: "Please select learner departments!" }]}
              >
                <TreeSelect
                  showCheckedStrategy={TreeSelect.SHOW_ALL}
                  style={{ width: 274 }}
                  treeData={departments}
                  multiple
                  allowClear
                  treeDefaultExpandAll
                  placeholder="Select learner departments"
                />
              </Form.Item>
            </Form>
          </div>
        </Modal>
      ) : null}
    </>
  );
};
