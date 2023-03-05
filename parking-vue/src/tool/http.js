import axios from 'axios'
axios.defaults.baseURL = "http://localhost:7777/parking"
axios.defaults.withCredentials = true
async function get (url, data) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'get',
      url: url,
      params: data,
      dataType: 'json'
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}

async function post (url, data) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'POST',
      url: url,
      data: data,
      dataType: "json"
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}
async function put (url, data) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'put',
      url: url,
      data: data,
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}

async function del (url, param, data) {
  return new Promise((resolve, reject) => {
    axios({
      method: 'delete',
      url: url,
      data: data,
      params: param
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}

export { axios, get, post, put, del }

