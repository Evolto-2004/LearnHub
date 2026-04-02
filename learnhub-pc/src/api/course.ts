import client from "./internal/httpClient";

// CoursesDetails
export function detail(id: number) {
  return client.get(`/api/v1/course/${id}`, {});
}

// CoursesLessonDetails
export function play(courseId: number, id: number) {
  return client.get(`/api/v1/course/${courseId}/hour/${id}`, {});
}

// 获取播放地址
export function playUrl(courseId: number, hourId: number) {
  return client.get(`/api/v1/course/${courseId}/hour/${hourId}/play`, {});
}

// 记录Learner观看Duration
export function record(courseId: number, hourId: number, duration: number) {
  return client.post(`/api/v1/course/${courseId}/hour/${hourId}/record`, {
    duration,
  });
}

//观看ping
export function playPing(courseId: number, hourId: number) {
  return client.post(`/api/v1/course/${courseId}/hour/${hourId}/ping`, {});
}

//Recent Learning课程
export function latestLearn() {
  return client.get(`/api/v1/user/latest-learn`, {});
}

//DownloadCourseware
export function downloadAttachment(courseId: number, id: number) {
  return client.get(`/api/v1/course/${courseId}/attach/${id}/download`, {});
}
