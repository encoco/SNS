import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use(config => {
	/*문제없을때 오는곳 오류나면 아래 response문으로 들어감*/
  const token = localStorage.getItem('userInfo');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => Promise.reject(error));

api.interceptors.response.use(response => response, async error => {
  const originalRequest = error.config;
  
  if (error.response.status === 401 && !originalRequest._retry) {
    originalRequest._retry = true;

    try {
      const Authorization = localStorage.getItem('userInfo');
      const response = await axios.post('http://localhost:8080/api/refreshToken', {Authorization});
      console.log(response);
      if (response.status === 200) {
	      localStorage.setItem('accessToken', response.data.accessToken);
	      originalRequest.headers.Authorization = `Bearer ${response.data.accessToken}`;
      }
      
      console.log("saidnsionfiqonfoqpgbneq");
      return api(originalRequest);
      
    } catch (refreshError) {
      console.error('재발급 오류', refreshError);
      
      return Promise.reject(refreshError);
    }
  }
  return Promise.reject(error);
});

export default api;