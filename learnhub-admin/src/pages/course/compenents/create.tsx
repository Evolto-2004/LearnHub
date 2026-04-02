import React, { useState, useEffect } from "react";
import {
  Space,
  Radio,
  Button,
  Drawer,
  Form,
  Input,
  Modal,
  message,
  Image,
  TreeSelect,
  Spin,
  Tag,
} from "antd";
import styles from "./create.module.less";
import { course, department } from "../../../api/index";
import {
  UploadImageButton,
  SelectResource,
  SelectAttachment,
  SelectRange,
} from "../../../compenents";
import { ExclamationCircleFilled } from "@ant-design/icons";
import { TreeHours } from "./hours";
import { TreeAttachments } from "./attachments";
import defaultThumb1 from "../../../assets/thumb/thumb1.png";
import defaultThumb2 from "../../../assets/thumb/thumb2.png";
import defaultThumb3 from "../../../assets/thumb/thumb3.png";

const { confirm } = Modal;

interface PropInterface {
  cateIds: any;
  open: boolean;
  onCancel: () => void;
}

interface Option {
  value: string | number;
  title: string;
  children?: Option[];
}

export const CourseCreate: React.FC<PropInterface> = ({
  cateIds,
  open,
  onCancel,
}) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [init, setInit] = useState(true);
  const [departments, setDepartments] = useState<Option[]>([]);
  const [categories, setCategories] = useState<Option[]>([]);
  const [thumb, setThumb] = useState("");
  const [chapterType, setChapterType] = useState(0);
  const [chapters, setChapters] = useState<CourseChaptersModel[]>([]);
  const [hours, setHours] = useState<number[]>([]);
  const [chapterHours, setChapterHours] = useState<any>([]);
  const [videoVisible, setVideoVisible] = useState(false);
  const [treeData, setTreeData] = useState<CourseHourModel[]>([]);
  const [addvideoCurrent, setAddvideoCurrent] = useState(0);
  const [showDrop, setShowDrop] = useState(false);
  const [attachmentVisible, setAttachmentVisible] = useState(false);
  const [attachmentData, setAttachmentData] = useState<AttachmentDataModel[]>(
    []
  );
  const [attachments, setAttachments] = useState<number[]>([]);
  const [depIds, setDepIds] = useState<number[]>([]);
  const [deps, setDeps] = useState<any[]>([]);
  const [idsVisible, setIdsVisible] = useState(false);
  const [type, setType] = useState("open");

  useEffect(() => {
    setInit(true);
    if (open) {
      initData();
    }
  }, [open, cateIds]);

  useEffect(() => {
    form.setFieldsValue({
      title: "",
      thumb: -1,
      isRequired: 1,
      short_desc: "",
      hasChapter: 0,
    });
    setThumb(defaultThumb1);
    setChapterType(0);
    setChapters([]);
    setChapterHours([]);
    setHours([]);
    setTreeData([]);
    setAttachmentData([]);
    setAttachments([]);
    setShowDrop(false);
    setDepIds([]);
    setDeps([]);
  }, [form, open]);

  const initData = async () => {
    await getParams();
    await getCategory();
    setInit(false);
  };

  const getParams = async () => {
    let res: any = await department.departmentList({});
    const departments = res.data.departments;
    const departCount: DepIdsModel = res.data.dep_user_count;
    if (JSON.stringify(departments) !== "{}") {
      const new_arr: any = checkArr(departments, 0, departCount);
      setDepartments(new_arr);
    }
    let type = "open";
    form.setFieldsValue({
      type: type,
      dep_ids: [],
    });
    setType(type);
  };

  const checkChild = (departments: any[], id: number) => {
    for (let key in departments) {
      for (let i = 0; i < departments[key].length; i++) {
        if (departments[key][i].id === id) {
          return departments[key][i];
        }
      }
    }
  };

  const getCategory = async () => {
    let res: any = await course.createCourse();
    const categories = res.data.categories;
    if (JSON.stringify(categories) !== "{}") {
      const new_arr: any = checkArr(categories, 0, null);
      setCategories(new_arr);
    }

    if (cateIds.length !== 0 && cateIds[0] !== 0) {
      let item = checkChild(res.data.categories, cateIds[0]);
      let arr: any[] = [];
      if (item === undefined) {
        arr.push(cateIds[0]);
      } else if (item.parent_chain === "") {
        arr.push(cateIds[0]);
      } else {
        let new_arr = item.parent_chain.split(",");
        new_arr.map((num: any) => {
          arr.push(Number(num));
        });
        arr.push(cateIds[0]);
      }
      form.setFieldsValue({
        category_ids: arr,
      });
    } else {
      form.setFieldsValue({
        category_ids: cateIds,
      });
    }
  };

  const getNewTitle = (title: any, id: number, counts: any) => {
    if (counts) {
      let value = counts[id] || 0;
      return title + "(" + value + ")";
    } else {
      return title;
    }
  };

  const checkArr = (departments: any[], id: number, counts: any) => {
    const arr = [];
    for (let i = 0; i < departments[id].length; i++) {
      if (!departments[departments[id][i].id]) {
        arr.push({
          title: getNewTitle(
            departments[id][i].name,
            departments[id][i].id,
            counts
          ),
          value: departments[id][i].id,
        });
      } else {
        const new_arr: any = checkArr(
          departments,
          departments[id][i].id,
          counts
        );
        arr.push({
          title: getNewTitle(
            departments[id][i].name,
            departments[id][i].id,
            counts
          ),
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
    let dep_ids: any[] = [];
    if (type === "elective") {
      dep_ids = depIds;
    }
    if (chapters.length === 0 && treeData.length === 0) {
      message.error("Please configure lessons");
      return;
    }
    setLoading(true);
    course
      .storeCourse(
        values.title,
        values.thumb,
        values.short_desc,
        1,
        values.isRequired,
        dep_ids,
        values.category_ids,
        chapters,
        treeData,
        attachmentData
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

  const getType = (e: any) => {
    setType(e.target.value);
  };

  const selectData = (arr: any, videos: any) => {
    if (arr.length === 0) {
      message.error("Select a video");
      return;
    }
    let keys = [...hours];
    let data = [...treeData];
    keys = keys.concat(arr);
    data = data.concat(videos);
    setHours(keys);
    setTreeData(data);
    setVideoVisible(false);
  };

  const selectChapterData = (arr: any, videos: any) => {
    if (arr.length === 0) {
      message.error("Select a video");
      return;
    }
    const data = [...chapters];
    const keys = [...chapterHours];
    keys[addvideoCurrent] = keys[addvideoCurrent].concat(arr);
    data[addvideoCurrent].hours = data[addvideoCurrent].hours.concat(videos);
    setChapters(data);
    setChapterHours(keys);
    setVideoVisible(false);
  };

  const selectAttachmentData = (arr: any, videos: any) => {
    if (arr.length === 0) {
      message.error("Select courseware");
      return;
    }
    let keys = [...attachments];
    let data = [...attachmentData];
    keys = keys.concat(arr);
    data = data.concat(videos);
    setAttachments(keys);
    setAttachmentData(data);
    setAttachmentVisible(false);
  };

  const getChapterType = (e: any) => {
    const arr = [...chapters];
    if (arr.length > 0) {
      confirm({
        title: "Confirm Action",
        icon: <ExclamationCircleFilled />,
        content: "Switching the list option will clear added lessons. Continue?",
        centered: true,
        okText: "Confirm",
        cancelText: "Cancel",
        onOk() {
          setChapterType(e.target.value);
          setChapters([]);
          setHours([]);
          setChapterHours([]);
          setTreeData([]);
        },
        onCancel() {
          form.setFieldsValue({
            hasChapter: chapterType,
          });
        },
      });
    } else {
      setChapterType(e.target.value);
      setChapters([]);
      setHours([]);
      setChapterHours([]);
      setTreeData([]);
    }
  };

  const delHour = (id: number) => {
    const data = [...treeData];
    const index = data.findIndex((i: any) => i.rid === id);
    if (index >= 0) {
      data.splice(index, 1);
    }
    if (data.length > 0) {
      setTreeData(data);
      const keys = data.map((item: any) => item.rid);
      setHours(keys);
    } else {
      setTreeData([]);
      setHours([]);
    }
  };

  const transHour = (arr: any) => {
    setHours(arr);
    const data = [...treeData];
    const newArr: any = [];
    for (let i = 0; i < arr.length; i++) {
      data.map((item: any) => {
        if (item.rid === arr[i]) {
          newArr.push(item);
        }
      });
    }
    setTreeData(newArr);
  };

  const delAttachments = (id: number) => {
    const data = [...attachmentData];
    const index = data.findIndex((i: any) => i.rid === id);
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
  };

  const transAttachments = (arr: any) => {
    setAttachments(arr);
    const data = [...attachmentData];
    const newArr: any = [];
    for (let i = 0; i < arr.length; i++) {
      data.map((item: any) => {
        if (item.rid === arr[i]) {
          newArr.push(item);
        }
      });
    }
    setAttachmentData(newArr);
  };

  const addNewChapter = () => {
    const arr = [...chapters];
    const keys = [...chapterHours];
    arr.push({
      name: "",
      hours: [],
    });
    keys.push([]);
    setChapters(arr);
    setChapterHours(keys);
  };

  const setChapterName = (index: number, value: string) => {
    const arr = [...chapters];
    arr[index].name = value;
    setChapters(arr);
  };

  const delChapter = (index: number) => {
    const arr = [...chapters];
    const keys = [...chapterHours];
    confirm({
      title: "Confirm Action",
      icon: <ExclamationCircleFilled />,
      content: "Deleting this chapter will remove all added lessons. Continue?",
      centered: true,
      okText: "Confirm",
      cancelText: "Cancel",
      onOk() {
        arr.splice(index, 1);
        keys.splice(index, 1);
        setChapters(arr);
        setChapterHours(keys);
      },
      onCancel() {},
    });
  };

  const delChapterHour = (index: number, id: number) => {
    const keys = [...chapterHours];
    const data = [...chapters];
    const current = data[index].hours.findIndex((i: any) => i.rid === id);
    if (current >= 0) {
      data[index].hours.splice(current, 1);
    }
    if (data[index].hours.length > 0) {
      setChapters(data);
      keys[index] = data[index].hours.map((item: any) => item.rid);
      setChapterHours(keys);
    } else {
      keys[index] = [];
      data[index].hours = [];
      setChapters(data);
      setChapterHours(keys);
    }
  };

  const transChapterHour = (index: number, arr: any) => {
    const keys = [...chapterHours];
    keys[index] = arr;
    setChapterHours(keys);
    const data = [...chapters];
    const newArr: any = [];
    for (let i = 0; i < arr.length; i++) {
      data[index].hours.map((item: any) => {
        if (item.rid === arr[i]) {
          newArr.push(item);
        }
      });
    }
    data[index].hours = newArr;
    setChapters(data);
  };

  const changeChapterHours = (arr: any) => {
    const newArr: any = [];
    for (let i = 0; i < arr.length; i++) {
      arr[i].map((item: any) => {
        newArr.push(item);
      });
    }
    return newArr;
  };

  return (
    <>
      {open ? (
        <Drawer
          title="Create Course"
          onClose={onCancel}
          maskClosable={false}
          open={true}
          footer={
            <Space className="j-r-flex">
              <Button onClick={() => onCancel()}>Cancel</Button>
              <Button
                loading={loading}
                onClick={() => form.submit()}
                type="primary"
              >
                Confirm
              </Button>
            </Space>
          }
          width={634}
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
            <SelectResource
              defaultKeys={
                chapterType == 0 ? hours : changeChapterHours(chapterHours)
              }
              open={videoVisible}
              onCancel={() => {
                setVideoVisible(false);
              }}
              onSelected={(arr: any, videos: any) => {
                if (chapterType === 0) {
                  selectData(arr, videos);
                } else {
                  selectChapterData(arr, videos);
                }
              }}
            />
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
            <SelectRange
              defaultDepIds={depIds}
              defaultDeps={deps}
              open={idsVisible}
              onCancel={() => setIdsVisible(false)}
              onSelected={(selDepIds, selDeps) => {
                setDepIds(selDepIds);
                setDeps(selDeps);
                form.setFieldsValue({
                  ids: selDepIds,
                });
                setIdsVisible(false);
              }}
            />
            <Form
              form={form}
              name="create-basic"
              labelCol={{ span: 5 }}
              wrapperCol={{ span: 19 }}
              initialValues={{ remember: true }}
              onFinish={onFinish}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                label="Course Category"
                name="category_ids"
                rules={[{ required: true, message: "Please select a course category!" }]}
              >
                <TreeSelect
                  showCheckedStrategy={TreeSelect.SHOW_ALL}
                  allowClear
                  multiple
                  style={{ width: 424 }}
                  treeData={categories}
                  placeholder="Select a course category"
                  treeDefaultExpandAll
                />
              </Form.Item>
              <Form.Item
                label="Course Title"
                name="title"
                rules={[{ required: true, message: "Please enter the course title!" }]}
              >
                <Input
                  style={{ width: 424 }}
                  placeholder="Enter the course title"
                  allowClear
                />
              </Form.Item>
              <Form.Item
                label="Course Type"
                name="isRequired"
                rules={[{ required: true, message: "Please select a course type!" }]}
              >
                <Radio.Group>
                  <Radio value={1}>Required</Radio>
                  <Radio value={0} style={{ marginLeft: 22 }}>
                    Optional
                  </Radio>
                </Radio.Group>
              </Form.Item>
              <Form.Item
                label="Assigned Departments"
                name="type"
                rules={[{ required: true, message: "Please select assigned departments!" }]}
              >
                <Radio.Group onChange={getType}>
                  <Radio value="open">All Departments</Radio>
                  <Radio value="elective">Select Department</Radio>
                </Radio.Group>
              </Form.Item>
              {type === "elective" && (
                <>
                  <Form.Item
                    label="Select Department"
                    name="ids"
                    rules={[{ required: true, message: "Please select a department!" }]}
                  >
                    <div
                      className="d-flex"
                      style={{
                        width: "100%",
                        flexWrap: "wrap",
                        marginBottom: -8,
                      }}
                    >
                      <Button
                        type="default"
                        style={{ marginBottom: 14 }}
                        onClick={() => setIdsVisible(true)}
                      >
                        Add Scope
                      </Button>
                      <div
                        className="d-flex"
                        style={{
                          width: "100%",
                          flexWrap: "wrap",
                          marginBottom: -16,
                        }}
                      >
                        {deps.length > 0 &&
                          deps.map((item: any, i: number) => (
                            <Tag
                              key={i}
                              closable
                              style={{
                                height: 32,
                                lineHeight: "32px",
                                fontSize: 14,
                                color: "#2563EB",
                                background: "rgba(255,77,79,0.1)",
                                marginRight: 16,
                                marginBottom: 16,
                              }}
                              onClose={(e) => {
                                e.preventDefault();
                                let arr = [...deps];
                                let arr2 = [...depIds];
                                arr.splice(i, 1);
                                arr2.splice(i, 1);
                                setDeps(arr);
                                setDepIds(arr2);
                                form.setFieldsValue({
                                  ids: arr2,
                                });
                              }}
                            >
                              {item.title.props.children}
                            </Tag>
                          ))}
                      </div>
                    </div>
                  </Form.Item>
                </>
              )}
              <Form.Item
                label="Course Cover"
                name="thumb"
                rules={[{ required: true, message: "Please upload a course cover!" }]}
              >
                <div className="d-flex">
                  <Image
                    src={thumb}
                    width={160}
                    height={120}
                    style={{ borderRadius: 6 }}
                    preview={false}
                  />
                  <div className="c-flex ml-8 flex-1">
                    <div className="d-flex mb-28">
                      <div
                        className={
                          thumb === defaultThumb1
                            ? styles["thumb-item-avtive"]
                            : styles["thumb-item"]
                        }
                        onClick={() => {
                          setThumb(defaultThumb1);
                          form.setFieldsValue({
                            thumb: -1,
                          });
                        }}
                      >
                        <Image
                          src={defaultThumb1}
                          width={80}
                          height={60}
                          style={{ borderRadius: 6 }}
                          preview={false}
                        />
                      </div>
                      <div
                        className={
                          thumb === defaultThumb2
                            ? styles["thumb-item-avtive"]
                            : styles["thumb-item"]
                        }
                        onClick={() => {
                          setThumb(defaultThumb2);
                          form.setFieldsValue({
                            thumb: -2,
                          });
                        }}
                      >
                        <Image
                          src={defaultThumb2}
                          width={80}
                          height={60}
                          style={{ borderRadius: 6 }}
                          preview={false}
                        />
                      </div>
                      <div
                        className={
                          thumb === defaultThumb3
                            ? styles["thumb-item-avtive"]
                            : styles["thumb-item"]
                        }
                        onClick={() => {
                          setThumb(defaultThumb3);
                          form.setFieldsValue({
                            thumb: -3,
                          });
                        }}
                      >
                        <Image
                          src={defaultThumb3}
                          width={80}
                          height={60}
                          style={{ borderRadius: 6 }}
                          preview={false}
                        />
                      </div>
                    </div>
                    <div className="d-flex">
                      <UploadImageButton
                        text="Change Poster"
                        onSelected={(url, id) => {
                          setThumb(url);
                          form.setFieldsValue({ thumb: id });
                        }}
                      ></UploadImageButton>
                      <span className="helper-text ml-8">
                        (Recommended size: 400x300px)
                      </span>
                    </div>
                  </div>
                </div>
              </Form.Item>
              <Form.Item
                label="Lesson List"
                name="hasChapter"
                rules={[{ required: true, message: "Please select at least one lesson!" }]}
              >
                <Radio.Group onChange={getChapterType}>
                  <Radio value={0}>NoneChapter</Radio>
                  <Radio value={1} style={{ marginLeft: 22 }}>
                    With Chapters
                  </Radio>
                </Radio.Group>
              </Form.Item>
              {chapterType === 0 && (
                <div className="c-flex mb-24">
                  <Form.Item>
                    <div className="ml-120">
                      <Button
                        onClick={() => setVideoVisible(true)}
                        type="primary"
                      >
                        Add Lesson
                      </Button>
                    </div>
                  </Form.Item>
                  <div className={styles["hous-box"]}>
                    {treeData.length === 0 && (
                      <span className={styles["no-hours"]}>
                        Click the button above to add a lesson
                      </span>
                    )}
                    {treeData.length > 0 && (
                      <TreeHours
                        data={treeData}
                        onRemoveItem={(id: number) => {
                          delHour(id);
                        }}
                        onUpdate={(arr: any[]) => {
                          transHour(arr);
                        }}
                      />
                    )}
                  </div>
                </div>
              )}
              {chapterType === 1 && (
                <div className="c-flex mb-24">
                  {chapters.length > 0 &&
                    chapters.map((item: any, index: number) => {
                      return (
                        <div
                          key={item.hours.length + "Chapter" + index}
                          className={styles["chapter-item"]}
                        >
                          <div className="d-flex">
                            <div className={styles["label"]}>
                              Chapter{index + 1}：
                            </div>
                            <Input
                              value={item.name}
                              className={styles["input"]}
                              onChange={(e) => {
                                setChapterName(index, e.target.value);
                              }}
                              allowClear
                              placeholder="Enter the chapter title"
                            />
                            <Button
                              className="mr-16"
                              type="primary"
                              onClick={() => {
                                setVideoVisible(true);
                                setAddvideoCurrent(index);
                              }}
                            >
                              Add Lesson
                            </Button>
                            <Button onClick={() => delChapter(index)}>
                              DeleteChapter
                            </Button>
                          </div>
                          <div className={styles["chapter-hous-box"]}>
                            {item.hours.length === 0 && (
                              <span className={styles["no-hours"]}>
                                Click the button above to add a lesson
                              </span>
                            )}
                            {item.hours.length > 0 && (
                              <TreeHours
                                data={item.hours}
                                onRemoveItem={(id: number) => {
                                  delChapterHour(index, id);
                                }}
                                onUpdate={(arr: any[]) => {
                                  transChapterHour(index, arr);
                                }}
                              />
                            )}
                          </div>
                        </div>
                      );
                    })}
                  <Form.Item>
                    <div className="ml-120">
                      <Button onClick={() => addNewChapter()}>Add Chapter</Button>
                    </div>
                  </Form.Item>
                </div>
              )}
              <Form.Item label="More options">
                <div
                  className={showDrop ? "drop-item active" : "drop-item"}
                  onClick={() => setShowDrop(!showDrop)}
                >
                  <i
                    style={{ fontSize: 14 }}
                    className="iconfont icon-icon-xiala c-red"
                  />
                  <span>(Course Summary, Courseware)</span>
                </div>
              </Form.Item>
              <div
                className="c-flex"
                style={{ display: showDrop ? "block" : "none" }}
              >
                <Form.Item label="Course Summary" name="short_desc">
                  <Input.TextArea
                    style={{ width: 424, minHeight: 80 }}
                    allowClear
                    placeholder="Enter a course summary (up to 200 characters)"
                    maxLength={200}
                  />
                </Form.Item>
                <Form.Item label="Attachments">
                  <Button
                    onClick={() => setAttachmentVisible(true)}
                    type="primary"
                  >
                    Add Courseware
                  </Button>
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
        </Drawer>
      ) : null}
    </>
  );
};
