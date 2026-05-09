import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { Outlet } from "react-router-dom";
import { login, system } from "../../api";
import { loginAction } from "../../store/user/loginUserSlice";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../store/system/systemConfigSlice";
import { UploadVideoFloatButton } from "../../compenents/upload-video-float-button";
import { getToken } from "../../utils";
import LoadingPage from "../loading";
import ErrorPage from "../error";

interface Props {
  loginData?: any;
  configData?: any;
}

const InitPage = (props: Props) => {
  const dispatch = useDispatch();
  const [initializing, setInitializing] = useState(() => !!getToken());
  const [initFailed, setInitFailed] = useState(false);

  const saveConfig = (configData: any) => {
    let config: SystemConfigStoreInterface = {
      systemName: configData["system.name"],
      systemLogo: configData["system.logo"],
      systemPcUrl: configData["system.pc_url"],
      resourceUrl: configData["resource_url"],
      memberDefaultAvatar: configData["member.default_avatar"],
      courseDefaultThumbs: configData["default.course_thumbs"],
      departments: configData["departments"],
    };
    dispatch(saveConfigAction(config));
  };

  useEffect(() => {
    if (props.loginData) {
      dispatch(loginAction(props.loginData));
    }

    if (props.configData) {
      saveConfig(props.configData);
      setInitializing(false);
      return;
    }

    if (!getToken()) {
      setInitializing(false);
      return;
    }

    const init = async () => {
      try {
        const [configRes, userRes]: any = await Promise.all([
          system.getSystemConfig(),
          login.getUser(),
        ]);
        saveConfig(configRes.data);
        dispatch(loginAction(userRes.data));
      } catch (e) {
        console.error("System initialization failed", e);
        setInitFailed(true);
      } finally {
        setInitializing(false);
      }
    };

    init();
  }, []);

  if (initializing) {
    return <LoadingPage />;
  }

  if (initFailed) {
    return <ErrorPage />;
  }

  return (
    <>
      <Outlet />
      <UploadVideoFloatButton></UploadVideoFloatButton>
    </>
  );
};

export default InitPage;
