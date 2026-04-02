import { Button, message, Modal } from "antd";
import Dragger from "antd/es/upload/Dragger";
import { useState } from "react";
import config from "../../../js/config";
import { getToken, checkUrl } from "../../../utils";
import { InboxOutlined } from "@ant-design/icons";

interface PropsInterface {
  categoryIds: number[];
  onUpdate: () => void;
}

export const UploadImageSub = (props: PropsInterface) => {
  const [showModal, setShowModal] = useState(false);

  const uploadProps = {
    name: "file",
    multiple: true,
    action:
      checkUrl(config.app_url) +
      "backend/v1/upload/minio?category_ids=" +
      props.categoryIds.join(","),
    headers: {
      authorization: "Bearer " + getToken(),
    },
    onChange(info: any) {
      const { status, response } = info.file;
      if (status === "done") {
        if (response.code === 0) {
          message.success(`${info.file.name} Upload succeeded`);
        } else {
          message.error(response.msg);
        }
      } else if (status === "error") {
        message.error(`${info.file.name} Upload failed`);
      }
    },
    showUploadList: {
      showRemoveIcon: false,
      showDownloadIcon: false,
    },
  };

  return (
    <>
      <Button
        type="primary"
        onClick={() => {
          setShowModal(true);
        }}
      >
        Upload Image
      </Button>

      {showModal && (
        <Modal
          open={true}
          closable={false}
          onCancel={() => {
            setShowModal(false);
          }}
          onOk={() => {
            props.onUpdate();
            setShowModal(false);
          }}
          maskClosable={false}
        >
          <Dragger {...uploadProps}>
            <p className="ant-upload-drag-icon">
              <InboxOutlined />
            </p>
            <p className="ant-upload-text">Drag images here to upload</p>
            <p className="ant-upload-hint">
              Supports multiple uploads / supports PNG, JPG, JPEG, and GIF images
            </p>
          </Dragger>
        </Modal>
      )}
    </>
  );
};
