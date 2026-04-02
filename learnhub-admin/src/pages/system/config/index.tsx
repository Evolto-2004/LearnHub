import { useState, useEffect } from "react";
import {
  Row,
  Form,
  Input,
  Image,
  Button,
  Tabs,
  message,
  Switch,
  Checkbox,
  Slider,
  Space,
  Select,
} from "antd";
import { appConfig, system } from "../../../api/index";
import { UploadImageButton } from "../../../compenents";
import { useDispatch } from "react-redux";
import type { TabsProps } from "antd";
import type { CheckboxChangeEvent } from "antd/es/checkbox";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../../store/system/systemConfigSlice";
import logoIcon from "../../../assets/logo.png";
import memberDefaultAvatar from "../../../assets/thumb/avatar.png";

const SystemConfigPage = () => {
  const dispatch = useDispatch();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [logo, setLogo] = useState("");
  const [thumb, setThumb] = useState("");
  const [avatar, setAvatar] = useState("");
  const [tabKey, setTabKey] = useState(1);
  const [resourceUrl, setResourceUrl] = useState<ResourceUrlModel>({});
  const [nameChecked, setNameChecked] = useState(false);
  const [emailChecked, setEmailChecked] = useState(false);
  const [idCardchecked, setIdCardChecked] = useState(false);

  useEffect(() => {
    getDetail();
  }, [tabKey]);

  const getDetail = () => {
    appConfig.appConfig().then((res: any) => {
      setResourceUrl(res.data.resource_url);
      let configData = res.data.app_config;
      for (let i = 0; i < configData.length; i++) {
        if (configData[i].key_name === "system.name") {
          form.setFieldsValue({
            "system.name": configData[i].key_value,
          });
        } else if (configData[i].key_name === "system.logo") {
          form.setFieldsValue({
            "system.logo": configData[i].key_value,
          });
          console.log(configData[i].key_value);
          if (configData[i].key_value !== "") {
            setLogo(res.data.resource_url[Number(configData[i].key_value)]);
          } else {
            setLogo(logoIcon);
          }
        } else if (configData[i].key_name === "system.pc_url") {
          form.setFieldsValue({
            "system.pc_url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "system.h5_url") {
          form.setFieldsValue({
            "system.h5_url": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.poster") {
          if (configData[i].key_value !== "") {
            setThumb(res.data.resource_url[Number(configData[i].key_value)]);
          }
          form.setFieldsValue({
            "player.poster": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.disabled_drag") {
          let value = 0;
          if (configData[i].key_value === "1") {
            value = 1;
          }
          form.setFieldsValue({
            "player.disabled_drag": value,
          });
        } else if (
          configData[i].key_name === "player.is_enabled_bullet_secret"
        ) {
          let value = 0;
          if (configData[i].key_value === "1") {
            value = 1;
          }
          form.setFieldsValue({
            "player.is_enabled_bullet_secret": value,
          });
        } else if (configData[i].key_name === "player.bullet_secret_text") {
          if (configData[i].key_value.indexOf("{name}") != -1) {
            setNameChecked(true);
          }
          if (configData[i].key_value.indexOf("{email}") != -1) {
            setEmailChecked(true);
          }
          if (configData[i].key_value.indexOf("{idCard}") != -1) {
            setIdCardChecked(true);
          }
          form.setFieldsValue({
            "player.bullet_secret_text": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.bullet_secret_color") {
          form.setFieldsValue({
            "player.bullet_secret_color": configData[i].key_value,
          });
        } else if (configData[i].key_name === "player.bullet_secret_opacity") {
          let value = 0;
          if (configData[i].key_value !== "") {
            value = Number(configData[i].key_value) * 100;
          }
          form.setFieldsValue({
            "player.bullet_secret_opacity": value,
          });
        } else if (configData[i].key_name === "system.pc_index_footer_msg") {
          form.setFieldsValue({
            "system.pc_index_footer_msg": configData[i].key_value,
          });
        } else if (configData[i].key_name === "member.default_avatar") {
          if (configData[i].key_value !== "") {
            setAvatar(res.data.resource_url[Number(configData[i].key_value)]);
          } else {
            setAvatar(memberDefaultAvatar);
          }
          form.setFieldsValue({
            "member.default_avatar": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.access_key") {
          form.setFieldsValue({
            "s3.access_key": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.secret_key") {
          form.setFieldsValue({
            "s3.secret_key": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.bucket") {
          form.setFieldsValue({
            "s3.bucket": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.region") {
          form.setFieldsValue({
            "s3.region": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.endpoint") {
          form.setFieldsValue({
            "s3.endpoint": configData[i].key_value,
          });
        }
      }
    });
  };

  const onSwitchChange = (checked: boolean) => {
    if (checked) {
      form.setFieldsValue({ "player.is_enabled_bullet_secret": 1 });
    } else {
      form.setFieldsValue({ "player.is_enabled_bullet_secret": 0 });
    }
  };

  const onDragChange = (checked: boolean) => {
    if (checked) {
      form.setFieldsValue({ "player.disabled_drag": 1 });
    } else {
      form.setFieldsValue({ "player.disabled_drag": 0 });
    }
  };

  const addName = (e: CheckboxChangeEvent) => {
    var value = form.getFieldValue("player.bullet_secret_text");
    if (e.target.checked) {
      value += "{name}";
    } else {
      value = value.replace("{name}", "");
    }
    form.setFieldsValue({
      "player.bullet_secret_text": value,
    });
    setNameChecked(!nameChecked);
  };

  const addEmail = (e: CheckboxChangeEvent) => {
    var value = form.getFieldValue("player.bullet_secret_text");
    if (e.target.checked) {
      value += "{email}";
    } else {
      value = value.replace("{email}", "");
    }
    form.setFieldsValue({
      "player.bullet_secret_text": value,
    });
    setEmailChecked(!emailChecked);
  };
  const addIdCard = (e: CheckboxChangeEvent) => {
    var value = form.getFieldValue("player.bullet_secret_text");
    if (e.target.checked) {
      value += "{idCard}";
    } else {
      value = value.replace("{idCard}", "");
    }
    form.setFieldsValue({
      "player.bullet_secret_text": value,
    });
    setIdCardChecked(!idCardchecked);
  };

  const onFinish = (values: any) => {
    if (loading) {
      return;
    }
    setLoading(true);
    values["player.bullet_secret_opacity"] =
      values["player.bullet_secret_opacity"] / 100;
    appConfig.saveAppConfig(values).then((res: any) => {
      message.success("Saved successfully!");
      setLoading(false);
      getDetail();
      getSystemConfig();
    });
  };

  const getSystemConfig = async () => {
    system.getSystemConfig().then((res: any) => {
      let data: SystemConfigStoreInterface = {
        systemName: res.data["system.name"],
        systemLogo: res.data["system.logo"],
        systemPcUrl: res.data["system.pc_url"],
        systemH5Url: res.data["system.h5_url"],
        memberDefaultAvatar: res.data["member.default_avatar"],
        courseDefaultThumbs: res.data["default.course_thumbs"],
        departments: res.data["departments"],
        resourceCategories: res.data["resource_categories"],
      };
      dispatch(saveConfigAction(data));
    });
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("Failed:", errorInfo);
  };

  const items: TabsProps["items"] = [
    {
      key: "1",
      label: `Website Settings`,
      children: (
        <Form
          form={form}
          name="basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          {logo && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="PC Portal Logo"
              name="system.logo"
              labelCol={{ style: { marginTop: 4, marginLeft: 24 } }}
            >
              <div className="d-flex">
                <Image preview={false} height={40} src={logo} />
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="Change Logo"
                    onSelected={(url, id) => {
                      setLogo(url);
                      form.setFieldsValue({ "system.logo": id });
                    }}
                  ></UploadImageButton>
                </div>
                <div className="helper-text ml-8">
                  (Recommended size: 240x80px. JPG and PNG supported.)
                </div>
              </div>
            </Form.Item>
          )}
          {!logo && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="PC Portal Logo"
              name="system.logo"
            >
              <div className="d-flex">
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="Change Logo"
                    onSelected={(url, id) => {
                      setLogo(url);
                      form.setFieldsValue({ "system.logo": id });
                    }}
                  ></UploadImageButton>
                </div>
                <div className="helper-text ml-8">
                  (Recommended size: 240x80px. JPG and PNG supported.)
                </div>
              </div>
            </Form.Item>
          )}
          <Form.Item
            style={{ marginBottom: 30 }}
            label="PC Portal URL"
            name="system.pc_url"
          >
            <Input style={{ width: 274 }} placeholder="Enter the PC portal URL" />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="H5 Portal URL"
            name="system.h5_url"
          >
            <Input style={{ width: 274 }} placeholder="Enter the H5 portal URL" />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Portal Title"
            name="system.name"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the portal title"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Portal Footer"
            name="system.pc_index_footer_msg"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the portal footer"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              Save
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "2",
      label: `Playback Settings`,
      children: (
        <Form
          form={form}
          name="n-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item style={{ marginBottom: 30 }} label="Disable seek bar dragging">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="player.disabled_drag" valuePropName="checked">
                <Switch onChange={onDragChange} />
              </Form.Item>
              <div className="helper-text">
                (Disable dragging during first-time learning to prevent skipping.)
              </div>
            </Space>
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="Watermark Overlay">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item
                name="player.is_enabled_bullet_secret"
                valuePropName="checked"
              >
                <Switch onChange={onSwitchChange} />
              </Form.Item>
              <div className="helper-text">
                (Show a randomized watermark overlay to discourage screen recording.)
              </div>
            </Space>
          </Form.Item>
          <Form.Item style={{ marginBottom: 30 }} label="Watermark Text">
            <Space align="baseline" style={{ height: 32 }}>
              <Form.Item name="player.bullet_secret_text">
                <Input
                  style={{ width: 274 }}
                  allowClear
                  placeholder="Customize watermark text"
                  onChange={(e) => {
                    const { value } = e.target;
                    if (!value && e.type !== "change") {
                      setNameChecked(false);
                      setEmailChecked(false);
                      setIdCardChecked(false);
                    }
                  }}
                />
              </Form.Item>
              <Checkbox
                checked={nameChecked}
                className="ml-24"
                onChange={addName}
              >
                Name
              </Checkbox>
              <Checkbox
                checked={emailChecked}
                className="ml-24"
                onChange={addEmail}
              >
                Email
              </Checkbox>
            </Space>
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Watermark Color"
            name="player.bullet_secret_color"
          >
            <Input type="color" style={{ width: 32, padding: 0 }} />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Watermark Opacity"
            name="player.bullet_secret_opacity"
          >
            <Slider style={{ width: 400 }} range defaultValue={[0, 100]} />
          </Form.Item>
          {thumb && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="Player Poster"
              name="player.poster"
              labelCol={{ style: { marginTop: 75, marginLeft: 42 } }}
            >
              <div className="d-flex">
                <Image
                  preview={false}
                  height={180}
                  src={thumb}
                  style={{ borderRadius: 6 }}
                />
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="Change Poster"
                    onSelected={(url, id) => {
                      setThumb(url);
                      form.setFieldsValue({ "player.poster": id });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">
                    (Recommended size: 1920x1080px. Shown before playback starts.)
                  </div>
                </div>
              </div>
            </Form.Item>
          )}
          {!thumb && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="Player Poster"
              name="player.poster"
            >
              <div className="d-flex">
                <div className="d-flex">
                  <UploadImageButton
                    text="Change Poster"
                    onSelected={(url, id) => {
                      setThumb(url);
                      form.setFieldsValue({ "player.poster": id });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">
                    (Recommended size: 1920x1080px. Shown before playback starts.)
                  </div>
                </div>
              </div>
            </Form.Item>
          )}
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              Save
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "3",
      label: `Learner Settings`,
      children: (
        <Form
          form={form}
          name="m-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          {avatar && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="Default Learner Avatar"
              name="member.default_avatar"
              labelCol={{ style: { marginTop: 14, marginLeft: 28 } }}
            >
              <div className="d-flex">
                <Image
                  preview={false}
                  width={60}
                  height={60}
                  src={avatar}
                  style={{ borderRadius: "50%" }}
                />
                <div className="d-flex ml-24">
                  <UploadImageButton
                    text="Change Avatar"
                    onSelected={(url, id) => {
                      setAvatar(url);
                      form.setFieldsValue({ "member.default_avatar": id });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">(Default avatar for new learners)</div>
                </div>
              </div>
            </Form.Item>
          )}
          {!avatar && (
            <Form.Item
              style={{ marginBottom: 30 }}
              label="Default Learner Avatar"
              name="member.default_avatar"
            >
              <div className="d-flex">
                <div className="d-flex">
                  <UploadImageButton
                    text="Change Avatar"
                    onSelected={(url, id) => {
                      setAvatar(url);
                      form.setFieldsValue({ "member.default_avatar": id });
                    }}
                  ></UploadImageButton>
                  <div className="helper-text ml-8">(Default avatar for new learners)</div>
                </div>
              </div>
            </Form.Item>
          )}
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              Save
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: "4",
      label: `S3 Storage`,
      children: (
        <Form
          form={form}
          name="IO-basic"
          labelCol={{ span: 3 }}
          wrapperCol={{ span: 21 }}
          style={{ width: 1000, paddingTop: 30 }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            style={{ marginBottom: 30 }}
            label="AccessKey"
            name="s3.access_key"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the Access Key"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="SecretKey"
            name="s3.secret_key"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the Secret Key"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Bucket"
            name="s3.bucket"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the bucket name"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Region"
            name="s3.region"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the region"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            label="Endpoint"
            name="s3.endpoint"
          >
            <Input
              style={{ width: 274 }}
              allowClear
              placeholder="Enter the endpoint"
            />
          </Form.Item>
          <Form.Item
            style={{ marginBottom: 30 }}
            wrapperCol={{ offset: 3, span: 21 }}
          >
            <Button type="primary" htmlType="submit" loading={loading}>
              Save
            </Button>
          </Form.Item>
        </Form>
      ),
    },
  ];

  const onChange = (key: string) => {
    setTabKey(Number(key));
  };

  return (
    <>
      <Row className="learnhub-main-body">
        <Tabs
          className="float-left"
          defaultActiveKey="1"
          items={items}
          onChange={onChange}
        />
      </Row>
    </>
  );
};

export default SystemConfigPage;
