import { useEffect, useState } from "react";
import { Row, Modal, Tabs } from "antd";
import styles from "./index.module.less";
import { UploadCoursewareSub } from "../../compenents";
import type { TabsProps } from "antd";

interface PropsInterface {
  defaultKeys: any[];
  open: boolean;
  onSelected: (arr: any[], videos: any[]) => void;
  onCancel: () => void;
}

type selAttachmentModel = {
  name: string;
  rid: number;
  type: string;
};

export const SelectAttachment = (props: PropsInterface) => {
  const [refresh, setRefresh] = useState(true);

  const [tabKey, setTabKey] = useState(1);
  const [selectKeys, setSelectKeys] = useState<number[]>([]);
  const [selectVideos, setSelectVideos] = useState<selAttachmentModel[]>([]);

  useEffect(() => {
    setRefresh(!refresh);
  }, [props.open]);

  const items: TabsProps["items"] = [
    {
      key: "1",
      label: `Courseware`,
      children: (
        <UploadCoursewareSub
          label="Courseware"
          defaultCheckedList={props.defaultKeys}
          open={refresh}
          onSelected={(arr: any[], videos: any[]) => {
            setSelectKeys(arr);
            setSelectVideos(videos);
          }}
        />
      ),
    },
  ];

  const onChange = (key: string) => {
    setTabKey(Number(key));
  };

  return (
    <>
      {props.open ? (
        <Modal
          title="Resource Library"
          centered
          closable={false}
          onCancel={() => {
            setSelectKeys([]);
            setSelectVideos([]);
            props.onCancel();
          }}
          open={true}
          width={800}
          maskClosable={false}
          onOk={() => {
            props.onSelected(selectKeys, selectVideos);
            setSelectKeys([]);
            setSelectVideos([]);
          }}
        >
          <Row>
            <Tabs defaultActiveKey="1" items={items} onChange={onChange} />
          </Row>
        </Modal>
      ) : null}
    </>
  );
};
