import request from '@/utils/request'

export function getDbTransferHistoryList(params) {
  return request({
    url: '/dbTransferHistoryList',
    method: 'get',
    params: params
  })
}

export function addDbTransferHistory(data) {
  return request({
    url: '/dbTransferHistory',
    method: 'post',
    data
  })
}

export function dbTransferHistoryDetail(id) {
  return request({
    url: '/dbTransferHistory/' + id,
    method: 'get'
  })
}
