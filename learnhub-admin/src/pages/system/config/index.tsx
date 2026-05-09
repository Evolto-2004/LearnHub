import { useState, useEffect } from "react";
import { Row, Form, Input, Image, Button, Tabs, message, Switch } from "antd";
import { ReloadOutlined, SaveOutlined } from "@ant-design/icons";
import { appConfig, system } from "../../../api/index";
import { UploadImageButton } from "../../../compenents";
import { useDispatch } from "react-redux";
import type { TabsProps } from "antd";
import styles from "./index.module.less";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../../store/system/systemConfigSlice";
import logoIcon from "../../../assets/logo.png";
import memberDefaultAvatar from "../../../assets/thumb/avatar.png";

const SETTINGS_CONFIG_KEYS = [
  "system.logo",
  "player.disabled_drag",
  "player.poster",
  "member.default_avatar",
];

const S3_CONFIG_KEYS = [
  "s3.access_key",
  "s3.secret_key",
  "s3.bucket",
  "s3.region",
  "s3.endpoint",
];

const normalizeConfigValue = (value: any) => {
  if (typeof value === "boolean") {
    return value ? "1" : "0";
  }

  if (value === undefined || value === null) {
    return "";
  }

  return String(value);
};

const isDragDisabled = (value: any) => {
  return value === 1 || value === "1" || value === true;
};

const SystemConfigPage = () => {
  const dispatch = useDispatch();
  const [settingsForm] = Form.useForm();
  const [s3Form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [logo, setLogo] = useState("");
  const [thumb, setThumb] = useState("");
  const [avatar, setAvatar] = useState("");

  useEffect(() => {
    getDetail();
  }, []);

  const getDetail = () => {
    return appConfig.appConfig().then((res: any) => {
      let configData = res.data.app_config;
      for (let i = 0; i < configData.length; i++) {
        if (configData[i].key_name === "system.logo") {
          settingsForm.setFieldsValue({
            "system.logo": configData[i].key_value,
          });
          if (configData[i].key_value !== "") {
            setLogo(res.data.resource_url[Number(configData[i].key_value)]);
          } else {
            setLogo(logoIcon);
          }
        } else if (configData[i].key_name === "player.poster") {
          settingsForm.setFieldsValue({
            "player.poster": configData[i].key_value,
          });
          if (configData[i].key_value !== "") {
            setThumb(res.data.resource_url[Number(configData[i].key_value)]);
          } else {
            setThumb("");
          }
        } else if (configData[i].key_name === "player.disabled_drag") {
          let value = 0;
          if (configData[i].key_value === "1") {
            value = 1;
          }
          settingsForm.setFieldsValue({
            "player.disabled_drag": value,
          });
        } else if (configData[i].key_name === "member.default_avatar") {
          settingsForm.setFieldsValue({
            "member.default_avatar": configData[i].key_value,
          });
          if (configData[i].key_value !== "") {
            setAvatar(res.data.resource_url[Number(configData[i].key_value)]);
          } else {
            setAvatar(memberDefaultAvatar);
          }
        } else if (configData[i].key_name === "s3.access_key") {
          s3Form.setFieldsValue({
            "s3.access_key": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.secret_key") {
          s3Form.setFieldsValue({
            "s3.secret_key": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.bucket") {
          s3Form.setFieldsValue({
            "s3.bucket": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.region") {
          s3Form.setFieldsValue({
            "s3.region": configData[i].key_value,
          });
        } else if (configData[i].key_name === "s3.endpoint") {
          s3Form.setFieldsValue({
            "s3.endpoint": configData[i].key_value,
          });
        }
      }
    });
  };

  const saveConfigValues = (values: any, keys: string[]) => {
    if (loading) {
      return;
    }

    setLoading(true);
    const data: Record<string, string> = {};
    keys.forEach((key) => {
      data[key] = normalizeConfigValue(values[key]);
    });

    appConfig
      .saveAppConfig(data)
      .then(() => {
        message.success("Saved successfully!");
        getDetail();
        getSystemConfig();
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const onSettingsFinish = (values: any) => {
    saveConfigValues(values, SETTINGS_CONFIG_KEYS);
  };

  const onS3Finish = (values: any) => {
    saveConfigValues(values, S3_CONFIG_KEYS);
  };

  const onReset = () => {
    if (loading) {
      return;
    }

    getDetail().then(() => {
      message.success("Reset successfully!");
    });
  };

  const getSystemConfig = async () => {
    system.getSystemConfig().then((res: any) => {
      let data: SystemConfigStoreInterface = {
        systemLogo: res.data["system.logo"],
        memberDefaultAvatar: res.data["member.default_avatar"],
        courseDefaultThumbs: res.data["default.course_thumbs"],
        departments: res.data["departments"],
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
      label: `Settings`,
      children: (
        <Form
          form={settingsForm}
          name="settings"
          className={styles.configForm}
          onFinish={onSettingsFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item name="system.logo" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="player.poster" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="player.disabled_drag" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="member.default_avatar" hidden>
            <Input />
          </Form.Item>
          <div className={styles.settingsGrid}>
            <div className={styles.settingsStack}>
              <section className={styles.settingCard}>
                <div className={styles.cardHeader}>
                  <div>
                    <div className={styles.settingTitle}>PC Portal Logo</div>
                    <div className={styles.settingHint}>240x80px / JPG, PNG</div>
                  </div>
                  <UploadImageButton
                    text="Change Logo"
                    onSelected={(url, id) => {
                      setLogo(url);
                      settingsForm.setFieldsValue({ "system.logo": id });
                    }}
                  ></UploadImageButton>
                </div>
                <div className={`${styles.assetFrame} ${styles.logoPreview}`}>
                  <Image
                    preview={false}
                    src={logo || logoIcon}
                    style={{ maxHeight: 56, objectFit: "contain" }}
                  />
                </div>
              </section>

              <section className={styles.settingCard}>
                <div className={styles.cardHeader}>
                  <div>
                    <div className={styles.settingTitle}>Default Learner Avatar</div>
                    <div className={styles.settingHint}>Used for new learners</div>
                  </div>
                  <UploadImageButton
                    text="Change Avatar"
                    onSelected={(url, id) => {
                      setAvatar(url);
                      settingsForm.setFieldsValue({ "member.default_avatar": id });
                    }}
                  ></UploadImageButton>
                </div>
                <div className={styles.avatarRow}>
                  <Image
                    preview={false}
                    width={72}
                    height={72}
                    src={avatar || memberDefaultAvatar}
                    style={{ borderRadius: "50%", objectFit: "cover" }}
                  />
                  <div className={styles.avatarMeta}>
                    <span>Current default avatar</span>
                    <span>Square image recommended</span>
                  </div>
                </div>
              </section>

              <section className={`${styles.settingCard} ${styles.switchCard}`}>
                <div className={styles.cardHeader}>
                  <div>
                    <div className={styles.settingTitle}>Disable seek bar dragging</div>
                    <div className={styles.settingHint}>
                      Prevent skipping during first-time learning
                    </div>
                  </div>
                  <Form.Item noStyle shouldUpdate>
                    {({ getFieldValue }) => {
                      const disabled = isDragDisabled(
                        getFieldValue("player.disabled_drag")
                      );

                      return (
                        <Switch
                          checked={disabled}
                          onChange={(checked) => {
                            settingsForm.setFieldsValue({
                              "player.disabled_drag": checked ? 1 : 0,
                            });
                          }}
                        />
                      );
                    }}
                  </Form.Item>
                </div>
                <Form.Item noStyle shouldUpdate>
                  {({ getFieldValue }) => {
                    const disabled = isDragDisabled(
                      getFieldValue("player.disabled_drag")
                    );

                    return (
                      <div className={styles.switchSummary}>
                        <span>{disabled ? "Dragging disabled" : "Dragging allowed"}</span>
                        <span>
                          {disabled
                            ? "First-time videos play in order"
                            : "Learners can drag the seek bar"}
                        </span>
                      </div>
                    );
                  }}
                </Form.Item>
              </section>
            </div>

            <div className={styles.posterColumn}>
              <section className={`${styles.settingCard} ${styles.posterCard}`}>
                <div className={styles.cardHeader}>
                  <div>
                    <div className={styles.settingTitle}>Player Poster</div>
                    <div className={styles.settingHint}>
                      1920x1080px / shown before playback
                    </div>
                  </div>
                  <UploadImageButton
                    text="Change Poster"
                    onSelected={(url, id) => {
                      setThumb(url);
                      settingsForm.setFieldsValue({ "player.poster": id });
                    }}
                  ></UploadImageButton>
                </div>
                <div className={`${styles.assetFrame} ${styles.posterPreview}`}>
                  {thumb ? (
                    <Image preview={false} src={thumb} />
                  ) : (
                    <div className={styles.emptyPreview}>No poster selected</div>
                  )}
                </div>
              </section>
              <div className={styles.formActions}>
                <Button
                  htmlType="button"
                  icon={<ReloadOutlined />}
                  onClick={onReset}
                >
                  Reset
                </Button>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  icon={<SaveOutlined />}
                >
                  Save
                </Button>
              </div>
            </div>
          </div>
        </Form>
      ),
    },
    {
      key: "2",
      label: `S3 Storage`,
      children: (
        <Form
          form={s3Form}
          name="s3-storage"
          layout="vertical"
          className={styles.configForm}
          onFinish={onS3Finish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <section className={styles.s3Panel}>
            <div className={styles.s3Grid}>
              <Form.Item label="AccessKey" name="s3.access_key">
                <Input allowClear placeholder="Enter the Access Key" />
              </Form.Item>
              <Form.Item label="SecretKey" name="s3.secret_key">
                <Input.Password allowClear placeholder="Enter the Secret Key" />
              </Form.Item>
              <Form.Item label="Bucket" name="s3.bucket">
                <Input allowClear placeholder="Enter the bucket name" />
              </Form.Item>
              <Form.Item label="Region" name="s3.region">
                <Input allowClear placeholder="Enter the region" />
              </Form.Item>
              <Form.Item
                className={styles.endpointField}
                label="Endpoint"
                name="s3.endpoint"
              >
                <Input allowClear placeholder="Enter the endpoint" />
              </Form.Item>
            </div>
          </section>
          <div className={styles.formActions}>
            <Button
              htmlType="button"
              icon={<ReloadOutlined />}
              onClick={onReset}
            >
              Reset
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              icon={<SaveOutlined />}
            >
              Save
            </Button>
          </div>
        </Form>
      ),
    },
  ];

  return (
    <>
      <Row className={`learnhub-main-body ${styles.configPage}`}>
        <Tabs className={styles.configTabs} defaultActiveKey="1" items={items} />
      </Row>
    </>
  );
};

export default SystemConfigPage;
