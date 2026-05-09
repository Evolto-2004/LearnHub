import client from "./internal/httpClient";

export function resourceList(
  page: number,
  size: number,
  sortField: string,
  sortAlgo: string,
  name: string,
  type: string
) {
  return client.get("/backend/v1/resource/index", {
    page,
    size,
    sort_field: sortField,
    sort_algo: sortAlgo,
    name,
    type,
  });
}

export function createResource(type: string) {
  return client.get("/backend/v1/resource/create", { type });
}

export function storeResource(
  name: string,
  extension: string,
  size: number,
  disk: string,
  fileId: string,
  path: string,
  url: string,
  extra: object
) {
  let data = Object.assign(
    {},
    {
      name,
      extension,
      size,
      disk,
      file_id: fileId,
      path,
      url,
    },
    extra
  );
  return client.post("/backend/v1/resource/create", data);
}

export function destroyResource(id: number) {
  return client.destroy(`/backend/v1/resource/${id}`);
}

export function destroyResourceMulti(ids: number[]) {
  return client.post(`/backend/v1/resource/destroy-multi`, {
    ids: ids,
  });
}

export function videoDetail(id: number) {
  return client.get(`/backend/v1/resource/${id}`, {});
}

export function videoUpdate(id: number, params: any) {
  return client.put(`/backend/v1/resource/${id}`, params);
}
