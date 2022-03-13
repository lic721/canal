import request from '@/utils/request'

export function getDbTransferConfigs(params) {
  return request({
    url: '/dbTransferConfigs',
    method: 'get',
    params: params
  })
}

export function saveIgnoreTransferTables(data) {
  return request({
    url: '/dbIgnoreTransferTables',
    method: 'post',
    data
  })
}

export function addDbTransferConfig(data) {
  return request({
    url: '/dbTransferConfig',
    method: 'post',
    data
  })
}

export function dbTransferConfigDetail(id) {
  return request({
    url: '/dbTransferConfig/' + id,
    method: 'get'
  })
}

export function updateDbTransferConfig(data) {
  return request({
    url: '/dbTransferConfig',
    method: 'put',
    data
  })
}

export function deleteDbTransferConfig(id) {
  return request({
    url: '/dbTransferConfig/' + id,
    method: 'delete'
  })
}
