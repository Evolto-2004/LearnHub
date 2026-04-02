import moment from "moment";

const translatedPhrases: Record<string, string> = {
  "超级管理员": "Super Admin",
  "主面板": "Dashboard",
  "管理员日志-列表": "Admin Logs - List",
  "管理员角色-列表": "Admin Roles - List",
  "管理员角色-新建": "Admin Roles - Create",
  "管理员角色-编辑": "Admin Roles - Edit",
  "管理员角色-删除": "Admin Roles - Delete",
  "管理员-列表": "Administrators - List",
  "管理员-新建": "Administrators - Create",
  "管理员-编辑": "Administrators - Edit",
  "管理员-删除": "Administrators - Delete",
  "系统配置-读取": "System Configuration - Read",
  "系统配置-保存": "System Configuration - Save",
};

const translatedSegments: Record<string, string> = {
  "管理员日志": "Admin Logs",
  "管理员角色": "Admin Roles",
  "系统配置": "System Configuration",
  "线上课": "Courses",
  "资源": "Resources",
  "分类": "Categories",
  "部门": "Departments",
  "课程": "Courses",
  "章节": "Chapters",
  "附件": "Attachments",
  "学员": "Learners",
  "视频": "Videos",
  "图片": "Images",
  "课件": "Courseware",
  "列表": "List",
  "全部分类": "All Categories",
  "新建": "Create",
  "编辑": "Edit",
  "删除": "Delete",
  "批量删除": "Bulk Delete",
  "更新排序": "Update Sort",
  "更新父级": "Update Parent",
  "排序调整": "Reorder",
  "读取": "Read",
  "保存": "Save",
  "上传": "Upload",
  "签名URL": "Signed URL",
  "已上传查询": "Uploaded Check",
  "文件合并": "Merge File",
  "权限": "Permissions",
  "角色": "Role",
  "日志": "Logs",
  "管理员": "Administrator",
};

function escapeRegExp(value: string) {
  return value.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}

export function getToken(): string {
  return window.localStorage.getItem("learnhub-backend-token") || "";
}

export function setToken(token: string) {
  window.localStorage.setItem("learnhub-backend-token", token);
}

export function clearToken() {
  window.localStorage.removeItem("learnhub-backend-token");
}

export function dateFormat(dateStr: string) {
  if (!dateStr) {
    return "-";
  }
  return moment(dateStr).format("YYYY-MM-DD HH:mm");
}

export function timeFormat(dateStr: number) {
  if (!dateStr) {
    return "-";
  }
  var d = moment.duration(dateStr, "seconds");
  let value = d.hours() + "h" + d.minutes() + "m" + d.seconds() + "s";

  if (d.hours() === 0) {
    value = d.minutes() + "m" + d.seconds() + "s";
  } else {
    value = d.hours() + "h" + d.minutes() + "m" + d.seconds() + "s";
  }

  return value;
}

export function generateUUID(): string {
  let guid = "";
  for (let i = 1; i <= 32; i++) {
    let n = Math.floor(Math.random() * 16.0).toString(16);
    guid += n;
    if (i === 8 || i === 12 || i === 16 || i === 20) guid += "-";
  }
  return guid;
}

export function transformBase64ToBlob(
  base64: string,
  mime: string,
  filename: string
): File {
  const arr = base64.split(",");
  const bstr = atob(arr[1]);
  let n = bstr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mime });
}

export function parseVideo(file: File): Promise<VideoParseInfo> {
  return new Promise((resolve, reject) => {
    let video = document.createElement("video");
    video.muted = true;
    video.setAttribute("src", URL.createObjectURL(file));
    video.setAttribute("autoplay", "autoplay");
    video.setAttribute("crossOrigin", "anonymous"); //设置跨域 No则toDataURL导出Image失败
    video.setAttribute("width", "400"); //设置Size，如果不设置，下面的canvas就要按需设置
    video.setAttribute("height", "300");
    video.currentTime = 7; //Video Duration，一定要设置，不然大概率白屏
    video.addEventListener("loadeddata", function () {
      let canvas = document.createElement("canvas"),
        width = video.width, //canvas的尺寸和Image一样
        height = video.height;
      canvas.width = width; //画布Size，默认为Video宽高
      canvas.height = height;
      let ctx = canvas.getContext("2d");
      if (!ctx) {
        return reject("Unable to capture a video frame");
      }
      ctx.drawImage(video, 0, 0, width, height); //绘制canvas
      let dataURL = canvas.toDataURL("image/png"); //转换为base64
      video.remove();
      let info: VideoParseInfo = {
        poster: dataURL,
        duration: parseInt(video.duration + ""),
      };
      return resolve(info);
    });
  });
}

export function getHost() {
  return window.location.protocol + "//" + window.location.host + "/";
}

export function inStrArray(array: string[], value: string): boolean {
  for (let i = 0; i < array.length; i++) {
    if (array[i] === value) {
      return true;
    }
  }
  return false;
}

export function ValidataCredentials(value: any) {
  let regIdCard =
    /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
  if (regIdCard.test(value)) {
    if (value.length === 18) {
      return true;
    }
  }
}

export function checkUrl(value: any) {
  let url = value;
  let str = url.substr(url.length - 1, 1);
  if (str !== "/") {
    url = url + "/";
  }
  return url;
}

export function dateWholeFormat(dateStr: string) {
  if (!dateStr) {
    return "";
  }
  return moment(dateStr).format("YYYY-MM-DD HH:mm:ss");
}

export function transUtcTime(value: string) {
  const specifiedTime = value;
  // 创建一个新的Date对象，传入指定Time
  const specifiedDate = new Date(specifiedTime);
  //将指定Time转换为UTC+0Time
  const utcTime = specifiedDate.toISOString();

  return utcTime;
}

export function translateDisplayText(value?: string | null): string {
  if (!value) {
    return "";
  }

  const normalized = value.trim();
  if (!normalized) {
    return "";
  }

  if (translatedPhrases[normalized]) {
    return translatedPhrases[normalized];
  }

  if (normalized.includes("|")) {
    return normalized
      .split("|")
      .map((item) => translateDisplayText(item))
      .join(" | ");
  }

  if (normalized.includes("-")) {
    return normalized
      .split("-")
      .map((item) => translateDisplayText(item))
      .join(" - ");
  }

  return translatedSegments[normalized] || normalized;
}

export function translateAdminIdentity(value?: string | null): string {
  if (!value) {
    return "";
  }

  const normalized = value.trim();
  return translatedPhrases[normalized] || normalized;
}

export function translateTextContent(value?: string | null): string {
  if (!value) {
    return "";
  }

  let result = value;
  const replacements = {
    ...translatedSegments,
    ...translatedPhrases,
  };

  Object.keys(replacements)
    .sort((a, b) => b.length - a.length)
    .forEach((key) => {
      result = result.replace(
        new RegExp(escapeRegExp(key), "g"),
        replacements[key]
      );
    });

  return result;
}
