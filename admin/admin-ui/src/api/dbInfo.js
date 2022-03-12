import request from '@/utils/request'

export function getDbInfos(params) {
  return request({
    url: '/dbInfos',
    method: 'get',
    params: params
  })
}

export function getAllDbInfos(params) {
  return request({
    url: '/allDbInfos',
    method: 'get',
    params: params
  })
}

export function addDbInfo(data) {
  return request({
    url: '/dbInfo',
    method: 'post',
    data
  })
}

export function dbInfoDetail(id) {
  return request({
    url: '/dbInfo/' + id,
    method: 'get'
  })
}

export function updateDbInfo(data) {
  return request({
    url: '/dbInfo',
    method: 'put',
    data
  })
}

export function deleteDbInfo(id) {
  return request({
    url: '/dbInfo/' + id,
    method: 'delete'
  })
}
