import { useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { Outlet } from "react-router-dom";
import {
  SystemConfigStoreInterface,
  saveConfigAction,
} from "../../store/system/systemConfigSlice";
import { loginAction } from "../../store/user/loginUserSlice";

interface Props {
  loginData?: any;
  configData?: any;
}

export const InitPage = (props: Props) => {
  const dispatch = useDispatch();
  const [init, setInit] = useState<boolean>(false);

  useEffect(() => {
    if (props.loginData) {
      dispatch(loginAction(props.loginData));
    }
    if (props.configData) {
      let config: SystemConfigStoreInterface = {
        //System Configuration
        systemLogo: props.configData["system-logo"],
        resourceUrl: props.configData["resource_url"],
        //播放器配置
        playerPoster: props.configData["player-poster"],
        playerIsDisabledDrag:
          props.configData["player-disabled-drag"] &&
          props.configData["player-disabled-drag"] === "1"
            ? true
            : false,
      };
      dispatch(saveConfigAction(config));
    }
    setInit(true);
  }, [props]);

  return (
    <>
      {init && (
        <div style={{ minHeight: 900 }}>
          <Outlet />
        </div>
      )}
    </>
  );
};
