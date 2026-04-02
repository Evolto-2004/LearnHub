import { Button, Input, message, Modal } from "antd";
import { useState } from "react";
import { resourceCategory } from "../../api";
import { PlusOutlined } from "@ant-design/icons";

interface PropInterface {
  type: string;
  onUpdate: () => void;
}

export const CreateResourceCategory = (props: PropInterface) => {
  const [showModal, setShowModal] = useState(false);
  const [name, setName] = useState<string>("");

  const confirm = () => {
    if (name.length == 0) {
      message.error("Please enter the category name");
      return;
    }
    resourceCategory
      .storeResourceCategory(name, 0, 0)
      .then(() => {
        setName("");
        message.success("Category created successfully");
        setShowModal(false);
        props.onUpdate();
      })
      .catch((err) => {
        console.log("Error", err);
      });
  };

  return (
    <>
      <Button
        type="primary"
        onClick={() => {
          setShowModal(true);
        }}
        shape="circle"
        icon={<PlusOutlined />}
      />
      {showModal ? (
        <Modal
          onCancel={() => {
            setShowModal(false);
          }}
          onOk={confirm}
          open={true}
          title="Create Category"
        >
          <Input
            placeholder="Please enter the category name"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }}
            allowClear
          />
        </Modal>
      ) : null}
    </>
  );
};
