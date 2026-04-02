import { createSlice } from "@reduxjs/toolkit";

type SystemConfigStoreInterface = {
  systemPcUrl: string;
  systemH5Url: string;
  systemLogo: string;
  systemName: string;
  pcIndexFooterMsg: string;
  playerPoster: string;
  playerIsEnabledBulletSecret: boolean;
  playerIsDisabledDrag: boolean;
  playerBulletSecretText: string;
  playerBulletSecretColor: string;
  playerBulletSecretOpacity: string;
  resourceUrl?: ResourceUrlModel;
};

let defaultValue: SystemConfigStoreInterface = {
  systemPcUrl: "",
  systemH5Url: "",
  systemLogo: "",
  systemName: "",
  pcIndexFooterMsg: "",
  playerPoster: "",
  playerIsEnabledBulletSecret: false,
  playerIsDisabledDrag: false,
  playerBulletSecretText: "",
  playerBulletSecretColor: "",
  playerBulletSecretOpacity: "",
  resourceUrl: {},
};

const systemConfigSlice = createSlice({
  name: "systemConfig",
  initialState: {
    value: defaultValue,
  },
  reducers: {
    saveConfigAction(stage, e) {
      stage.value = e.payload;
    },
  },
});

export default systemConfigSlice.reducer;
export const { saveConfigAction } = systemConfigSlice.actions;

export type { SystemConfigStoreInterface };
