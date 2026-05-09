import { createSlice } from "@reduxjs/toolkit";

type SystemConfigStoreInterface = {
  systemLogo?: string;
  memberDefaultAvatar?: string;
  courseDefaultThumbs?: string[];
  departments?: any;
  resourceUrl?: ResourceUrlModel;
};

let defaultValue: SystemConfigStoreInterface = {};

const systemConfigSlice = createSlice({
  name: "systemConfig",
  initialState: {
    value: defaultValue,
  },
  reducers: {
    saveConfigAction(stage, e) {
      stage.value = e.payload;
    },
    saveDepartmentsAction(stage, e) {
      stage.value.departments = e.payload;
    },
  },
});

export default systemConfigSlice.reducer;
export const { saveConfigAction, saveDepartmentsAction } = systemConfigSlice.actions;

export type { SystemConfigStoreInterface };
