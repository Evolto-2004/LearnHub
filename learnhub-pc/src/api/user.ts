import client from "./internal/httpClient";

export function detail() {
  return client.get("/api/v1/user/detail", {});
}

// Change Password
export function password(oldPassword: string, newPassword: string) {
  return client.put("/api/v1/user/password", {
    old_password: oldPassword,
    new_password: newPassword,
  });
}

// Learner课程
export function courses(depId: number) {
  return client.get("/api/v1/user/courses", {
    dep_id: depId,
  });
}

// 修改头像
export function avatar(file: any) {
  return client.put("/api/v1/user/avatar", {
    file: file,
  });
}
