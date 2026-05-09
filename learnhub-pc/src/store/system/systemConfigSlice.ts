import { createSlice } from "@reduxjs/toolkit";

type SystemConfigStoreInterface = {
  systemLogo: string;
  playerPoster: string;
  playerIsDisabledDrag: boolean;
  resourceUrl?: ResourceUrlModel;
};

let defaultValue: SystemConfigStoreInterface = {
  systemLogo: "",
  playerPoster: "",
  playerIsDisabledDrag: false,
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
